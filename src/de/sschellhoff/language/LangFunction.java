package de.sschellhoff.language;

import de.sschellhoff.language.ast.BlockStmt;
import de.sschellhoff.language.ast.FuncDefStmt;

import java.util.List;

public class LangFunction implements LangCallable {
    private final FuncDefStmt declaration;
    private final Environment closure;
    private final boolean isInitializer;

    public LangFunction(FuncDefStmt declaration, Environment closure, boolean isInitializer) {
        this.declaration = declaration;
        this.closure = closure;
        this.isInitializer = isInitializer;
    }

    public LangFunction bind(LangInstance instance) {
        Environment environment = new Environment(closure);
        environment.define(Token.identifier("this"), instance);
        return new LangFunction(declaration, environment, isInitializer);
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
            if(isInitializer) {
                return closure.getAt(0, Token.identifier("this"));
            }
            return re.value;
        }
        if(isInitializer) {
            return closure.getAt(0, Token.identifier("this"));
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
