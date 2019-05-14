package de.sschellhoff.language;

import de.sschellhoff.language.ast.Stmt;

import java.util.List;

public class Frontend {
    final Interpreter interpreter;
    final ErrorWriter errorWriter;
    Frontend(Interpreter interpreter, ErrorWriter errorWriter) {
        this.interpreter = interpreter;
        this.errorWriter = errorWriter;
    }

    public List<Stmt> build(String sourcecode) {
        Scanner scanner = new Scanner(sourcecode, errorWriter);
        List<Token> tokens = scanner.scan();
        if(scanner.hadError()) {
            System.exit(65);
        }
        Parser parser = new Parser(tokens, errorWriter);
        List<Stmt> program = parser.parse();
        if(parser.hadError()) {
            System.exit(65);
        }
        Resolver resolver = new Resolver(interpreter, errorWriter);
        resolver.resolve(program);
        if(resolver.hadError()) {
            System.exit(65);
        }
        return program;
    }
}
