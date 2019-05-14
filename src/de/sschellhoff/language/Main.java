package de.sschellhoff.language;

import de.sschellhoff.language.ast.Stmt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        boolean print = false;
        boolean evaluate = true;
        String sourcecode = "";
        String startingPath = "";
        if(args.length != 1) {
            System.err.println("You must specify a file as parameter!");
            System.exit(64);
        } else {
            try {
                Path startingFile = Misc.toAbsolutePath(args[0], Misc.getWorkingDirectory());
                startingPath = Misc.getDirectory(startingFile.toString());
                sourcecode = new String(Files.readAllBytes(startingFile));
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
            Interpreter interpreter = new Interpreter(startingPath, errorWriter);
            Resolver resolver = new Resolver(interpreter, errorWriter);
            resolver.resolve(program);
            if(resolver.hadError()) {
                System.exit(65);
            }
            if(!interpreter.interpret(program)) {
                System.exit(70);
            }
        }
    }
}
