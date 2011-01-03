for(i in 0..<10) {
    def start = System.currentTimeMillis()
    FibGroovy.fibStaticTernary(40)
    println("Groovy(static ternary):  ${System.currentTimeMillis() - start}ms")

    start = System.currentTimeMillis()
    FibGroovy.fibStaticIf(40)
    println("Groovy(static if):  ${System.currentTimeMillis() - start}ms")

    start = System.currentTimeMillis()
    new FibGroovy().fibTernary(40)
    println("Groovy(instance ternary):  ${System.currentTimeMillis() - start}ms")

    start = System.currentTimeMillis()
    new FibGroovy().fibIf(40)
    println("Groovy(instance if):  ${System.currentTimeMillis() - start}ms")

    start = System.currentTimeMillis()
    FibGroovypp.fib(40)
    println("Groovy++: ${System.currentTimeMillis() - start}ms")

    start = System.currentTimeMillis()
    FibJava.fib(40)
    println("Java:     ${System.currentTimeMillis() - start}ms")
}
