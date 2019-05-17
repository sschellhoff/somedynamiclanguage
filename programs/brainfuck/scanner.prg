class Scanner {
    fun init(program) {
        this.program = program;
        this.current = 0;
    }

    fun isAtEnd() {
        return this.current >= stringlength(this.program);
    }

    fun getNextToken() {
        if(this.isAtEnd()) {
            return null;
        }
        var c = substring(this.program, this.current, this.current + 1);
        this.current = this.current + 1;
        if c == "<" || c == ">" || c == "+" || c == "-" || c == "." || c == "," || c == "[" || c == "]" {
            return c;
        }
        return this.getNextToken();
    }
}

