class FibGroovy {
    static int fibStaticTernary (int n) {
        n >= 2 ? fibStaticTernary(n-1) + fibStaticTernary(n-2) : 1
    }

    static int fibStaticIf (int n) {
        if(n >= 2) fibStaticIf(n-1) + fibStaticIf(n-2) else 1
    }

    int fibTernary (int n) {
        n >= 2 ? fibTernary(n-1) + fibTernary(n-2) : 1
    }

    int fibIf (int n) {
        if(n >= 2) fibIf(n-1) + fibIf(n-2) else 1
    }
}
