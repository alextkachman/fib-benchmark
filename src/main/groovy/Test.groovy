for(i in 0..<10) {
    def start = System.currentTimeMillis()
    FibGroovy.fib(40)
    println("Groovy:  ${System.currentTimeMillis() - start}ms")

    start = System.currentTimeMillis()
    FibGroovypp.fib(40)
    println("Groovy++: ${System.currentTimeMillis() - start}ms")

    start = System.currentTimeMillis()
    FibJava.fib(40)
    println("Java:     ${System.currentTimeMillis() - start}ms")
}
