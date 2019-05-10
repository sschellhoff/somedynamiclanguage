class Testing {
    fun check(a) {
        this.a = a * 2;
        print this.a;
        return a;
    }
}

print Testing;
var t = Testing();
print t;
t.b = 1337;
print t.b;
print t.check(42);
