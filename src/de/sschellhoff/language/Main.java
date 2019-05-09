package de.sschellhoff.language;

import de.sschellhoff.language.ast.Stmt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        boolean print = true;
        boolean evaluate = true;
        String sourcecode = "";
        if(args.length > 1) {
            System.err.println("too many parameters!");
            System.exit(64);
        } else if(args.length == 0) {
            sourcecode = "{ fun test() { return 1337; } { print test(); } }";
        } else {
            try {
                sourcecode = new String(Files.readAllBytes(Paths.get(args[0])));
            } catch (IOException e) {
                System.err.println("cannot open the specified file!");
                System.exit(66);
            }
        }
        ErrorWriter errorWriter = new ConsoleErrorWriter();
        Scanner scanner = new Scanner(sourcecode, errorWriter);
        List<Token> tokens = scanner.scan();
        if ( scanner.hadError() ) {
            System.exit(65);
        }
        Parser parser = new Parser(tokens, errorWriter);
        List<Stmt> program = parser.parse();
        if ( parser.hadError() ) {
            System.exit(65);
        }
        if(print) {
            PrintVisitor printer = new PrintVisitor();
            printer.print(program);
        }
        if(evaluate) {
            Interpreter interpreter = new Interpreter();
            Resolver resolver = new Resolver(interpreter, errorWriter);
            resolver.resolve(program);
            if(resolver.hadError()) {
                System.exit(65);
            }
            if(interpreter.interpret(program)) {
                System.out.println("program done without errors!");
            } else {
                System.exit(70);
            }
        }
    }
}
