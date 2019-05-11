class Test {
  fun init(abc) {
    this.abc = abc;
  }

  fun doSomething() {
    print this.abc ?? "no value";
  }
}

var t1 = Test(1337);
var t2 = Test(null);
var t3 = null;

t1.doSomething();
t2.doSomething();
//t3.doSomething(); 
t1?.doSomething();
t2?.doSomething();
t3?.doSomething();
