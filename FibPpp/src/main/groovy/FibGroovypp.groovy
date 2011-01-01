@Typed class FibGroovypp {
    static int fib (int n) {
        n >= 2 ? fib(n-1) + fib(n-2) : 1
    }
}
