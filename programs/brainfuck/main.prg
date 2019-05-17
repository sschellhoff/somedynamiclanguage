import "memory.prg";
import "scanner.prg";
import "parser.prg";
import "eval.prg";

fun main() {
    var sourcecode = " ++++++++++  [   >+++++++>++++++++++>+++>+<<<<-  ]  >++.  >+.  +++++++.  .  +++.  >++.  <<+++++++++++++++. >.  +++.  ------.  --------.  >+.  >.  +++.";
    var scanner = Scanner(sourcecode);
    var parser = Parser(scanner);
    var program = parser.buildProgram();
    if program == null {
        print "error in program!";
        return;
    }
    var memory = Memory();
    eval(program, memory);
    //memory.prnt();
}

main();
