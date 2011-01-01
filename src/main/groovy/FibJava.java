/**
 * Created by IntelliJ IDEA.
 * User: ait
 * Date: 12/31/10
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class FibJava {
    static int fib(int n) {
        return n >= 2 ? fib(n-1) + fib(n-2) : 1;
    }
}
