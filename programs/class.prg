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
var t4 = Test(Test(Test(null)));
print("Point operators:");
t1.doSomething();
t2.doSomething();
//t3.doSomething(); 
print("conditional null operators");
t1?.doSomething();
t2?.doSomething();
t3?.doSomething();
print("chained conditional null operators");
t4?.abc?.abc.doSomething();
t4?.abc?.abc?.abc?.doSomething();
