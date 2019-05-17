fun eval(program, memory) {
    for var i = 0; i < program.size(); i = i + 1 {
        program.get(i)(memory);
    }
}

fun createIncPtr() {
    fun incPtr(memory) {
        memory.incPtr();
    }
    return incPtr;
}

fun createDecPtr() {
    fun decPtr(memory) {
        memory.decPtr();
    }
    return decPtr;
}

fun createIncVal() {
    fun incVal(memory) {
        memory.incVal();
    }
    return incVal;
}

fun createDecVal() {
    fun decVal(memory) {
        memory.decVal();
    }
    return decVal;
}

fun createGetVal() {
    fun getVal(memory) {
        var input = readstring(null);
        var value = substring(input, 0, 1);
        memory.setVal(asciitoint(value));
    }
    return getVal;
}

fun createPutVal() {
    fun putVal(memory) {
        var value = memory.getVal();
        write(inttoascii(value));
    }
    return putVal;
}

fun createLoop(code) {
    fun loop(memory) {
        while true {
            if memory.getVal() == 0 {
                return;
            }
            eval(code, memory);
            if memory.getVal() == 0 {
                return;
            }
        }
    }
    return loop;
}
