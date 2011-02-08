@Typed
def doFastAndEasily() {

    def divisors = { int n, Collection<Integer> bucket = [] ->
        if (n > 3)
            for (candidate in 2..<n)
                if (n % candidate == 0)
                    return doCall(n.intdiv(candidate), bucket << candidate)

        bucket << n
    }

/* now generate XML using groovyÕs dynamic builders */
    (2..10000).each {
        println "$it: ${divisors(it)}"
    }
}

try {
    doFastAndEasily ()
}
catch(e) {
    e.printStackTrace()
}
