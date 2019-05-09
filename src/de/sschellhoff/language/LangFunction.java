package de.sschellhoff.language;

import de.sschellhoff.language.ast.BlockStmt;
import de.sschellhoff.language.ast.FuncDefStmt;

import java.util.List;

public class LangFunction implements LangCallable {
    private final FuncDefStmt declaration;
    private final Environment closure;

    public LangFunction(FuncDefStmt declaration, Environment closure) {
        this.declaration = declaration;
        this.closure = closure;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(closure);
        for(int i = 0; i < declaration.parameters.size(); i++) {
            environment.define(Token.identifier(declaration.parameters.get(i).lexeme), arguments.get(i));
        }
        try {
            interpreter.executeBlock(((BlockStmt) declaration.body).stmts, new Environment(environment));
        } catch(ReturnException re) {
            return re.value;
        }
        return null;
    }

    @Override
    public int arity() {
        return declaration.parameters.size();
    }

    @Override
    public String toString() {
        return "<fun " + declaration.name.lexeme + ">";
    }
}
