import "../collection/dynarray.prg";
import "eval.prg";

class Parser {
    fun init(scanner) {
        this.scanner = scanner;
        this.c = null;
    }

    fun buildProgram() {
        var result = DynArray(8);
        while((this.c = this.scanner.getNextToken()) != null) {
            if this.c == "+" {
                result.append(createIncVal());
            } else if this.c == "-" {
                    result.append(createDecVal());
            } else if this.c == ">" {
                    result.append(createIncPtr());
            } else if this.c == "<" {
                    result.append(createDecPtr());
            } else if this.c == "." {
                    result.append(createPutVal());
            } else if this.c == "," {
                    result.append(createGetVal());
            } else if this.c == "[" {
                var loopBody = this.parseLoop();
                if loopBody == null {
                    print "loop body is null";
                    return null;
                }
                result.append(loopBody);
            } else {
                return result;
            }
        }
        return result;
    }

    fun parseLoop() {
        while true {
            if this.c == null {
                print "unexpected program end";
                return null;
            }
            var loopBody = this.buildProgram();
            if loopBody == null {
                print "error in loop body";
                return null;
            }
            if this.c == "]" {
                return createLoop(loopBody);
            }
            print "loop must end with ]";
            return null;
        }
    }
}
