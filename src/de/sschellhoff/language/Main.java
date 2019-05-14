package de.sschellhoff.language;

import de.sschellhoff.language.ast.Stmt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        if(args.length != 1) {
            System.err.println("You must specify a file as parameter!");
            System.exit(64);
        } else {
            try {
                Path startingFile = Misc.toAbsolutePath(args[0], Misc.getWorkingDirectory());
                String sourcecode = new String(Files.readAllBytes(startingFile));
                ErrorWriter errorWriter = new ConsoleErrorWriter();
                Interpreter interpreter = new Interpreter(startingFile.toString(), errorWriter);
                Frontend frontend = new Frontend(interpreter, errorWriter);
                List<Stmt> program = frontend.build(sourcecode);
                if(!interpreter.interpret(program)) {
                    System.exit(70);
                }
            } catch (IOException e) {
                System.err.println("cannot open the specified file!");
                System.exit(66);
            }
        }
    }
}
