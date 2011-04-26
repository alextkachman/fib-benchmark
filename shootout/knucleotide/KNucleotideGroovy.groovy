package shootout.knucleotide;

/* The Computer Language Benchmarks Game
 http://shootout.alioth.debian.org/

 contributed by James McIlree
 ByteString code thanks to Matthieu Bentot and The Anh Tran
 */

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

@Typed
class KNucleotideGroovy {
    static ArrayList<Callable<Map<ByteString, ByteString>>> createFragmentTasks(final byte[] sequence, int[] fragmentLengths) {
        def tasks = new ArrayList<Callable<Map<ByteString, ByteString>>>()
        for (int fragmentLength : fragmentLengths) {
            for (int index = 0; index < fragmentLength; index++) {
                tasks.add([call: {createFragmentMap(sequence, index, fragmentLength)}])

            }
        }
        tasks
    }

    static Map<ByteString, ByteString> createFragmentMap(byte[] sequence, int offset, int fragmentLength) {
        def map = new HashMap<ByteString, ByteString>()
        int lastIndex = sequence.length - fragmentLength + 1
        ByteString key = new ByteString(fragmentLength)
        for (int index = offset; index < lastIndex; index += fragmentLength) {
            key.calculateHash(sequence, index)
            ByteString fragment = map.get(key)
            if (fragment != null) {
                fragment.count++
            } else {
                map.put(key, key)
                key = new ByteString(fragmentLength)
            }
        }

        map
    }

    // Destructive!
    static Map<ByteString, ByteString> sumTwoMaps(Map<ByteString, ByteString> map1, Map<ByteString, ByteString> map2) {
        for (entry in map2.entrySet()) {
            ByteString sum = map1.get(entry.getKey());
            if (sum != null)
                sum.count += entry.getValue().count
            else
                map1.put(entry.getKey(), entry.getValue())
        }
        map1
    }

    static String writeFrequencies(float totalCount, Map<ByteString, ByteString> frequencies) {
        def list = new TreeSet<ByteString>(frequencies.values())
        StringBuilder sb = new StringBuilder()
        for (ByteString k : list)
            sb.append(String.format("%s %.3f\n", k.toString().toUpperCase(), (float) (k.count) * 100.0f / totalCount))

        sb.append('\n').toString()
    }

    static String writeCount(List<Future<Map<ByteString, ByteString>>> futures, String nucleotideFragment) throws Exception {
        ByteString key = new ByteString(nucleotideFragment.length())
        key.calculateHash(nucleotideFragment.getBytes(), 0)

        int count = 0
        for (Future<Map<ByteString, ByteString>> future : futures) {
            ByteString temp = future.get().get(key)
            if (temp != null) count += temp.count
        }

        count + "\t" + nucleotideFragment.toUpperCase() + '\n';
    }

    static void main(String[] args) throws Exception {

	    // TODO: 
        InputStream stream = KNucleotideGroovy.class.getClassLoader().getResourceAsStream(
		        "shootout/knucleotide/kNucleotide.class")
        String line
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream))

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = new byte[100]
        while ((line = reader.readLine()) != null) {
            if (line.length() > bytes.length)
                bytes = new byte[line.length()];

            int i
            for (i = 0; i < line.length(); i++)
                bytes[i] = (byte) line.charAt(i)
            baos.write(bytes, 0, i)
        }
        byte[] sequence = baos.toByteArray()

        long millis = System.currentTimeMillis()
        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
        int[] fragmentLengths = [1, 2, 3, 4, 6, 12, 18]
        List<Future<Map<ByteString, ByteString>>> futures = pool.invokeAll(createFragmentTasks(sequence, fragmentLengths))
        pool.shutdown()

        StringBuilder sb = new StringBuilder()

        sb.append(writeFrequencies(sequence.length, futures.get(0).get()))
        sb.append(writeFrequencies(sequence.length - 1, sumTwoMaps(futures.get(1).get(), futures.get(2).get())))

        String[] nucleotideFragments = ["ggt", "ggta", "ggtatt", "ggtattttaatt", "ggtattttaatttatagt"]
        for (String nucleotideFragment : nucleotideFragments) {
            sb.append(writeCount(futures, nucleotideFragment))
        }

        /*System.out.print(sb.toString());*/
	    def total = System.currentTimeMillis() - millis
        println "[KNucleotide-Groovy Benchmark Result: $total ]";
    }

    static final class ByteString implements Comparable<ByteString> {
        int hash, count = 1
        final byte[] bytes

        ByteString(int size) {
            bytes = new byte[size]
        }

        void calculateHash(byte[] k, int offset) {
            int temp = 0
            for (int i = 0; i < bytes.length; i++) {
                byte b = k[offset + i]
                bytes[i] = b
                temp = temp * 31 + b
            }
            hash = temp
        }

        int hashCode() {
            hash
        }

        boolean equals(Object obj) {
            Arrays.equals(bytes, ((ByteString) obj).bytes);
        }

        int compareTo(ByteString other) {
            other.count - count;
        }

        String toString() {
            new String(bytes);
        }
    }
}