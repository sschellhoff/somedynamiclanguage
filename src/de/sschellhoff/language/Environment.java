package de.sschellhoff.language;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    public final Environment enclosingEnvironment;
    private final Map<String, Object> values = new HashMap<>();

    public Environment() {
        this.enclosingEnvironment = null;
    }

    public Environment(Environment enclosingEnvironment) {
        this.enclosingEnvironment = enclosingEnvironment;
    }

    public void define(Token name, Object value) {
        if (values.containsKey(name.lexeme)) {
            throw new RuntimeError(name, "cannot redefine variable '" + name.lexeme + "'");
        }
        values.put(name.lexeme, value);
    }

    public Object get(Token name) {
        if(values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }
        if(enclosingEnvironment != null) {
            return enclosingEnvironment.get(name);
        }
        throw new RuntimeError(name, "undefined variable '" + name.lexeme + "'");
    }

    public Object getAt(int distance, Token name) {
        return ancestor(distance).values.get(name.lexeme);
    }

    public void assign(Token name, Object value) {
        if(values.containsKey(name.lexeme)) {
            values.put(name.lexeme, value);
            return;
        }
        if(enclosingEnvironment != null) {
            enclosingEnvironment.assign(name, value);
            return;
        }
        throw new RuntimeError(name, "undefined variable '" + name.lexeme + "'");

    }

    public void assignAt(int distance, Token name, Object value) {
        ancestor(distance).values.put(name.lexeme, value);
    }


    private Environment ancestor(int distance) {
        Environment env = this;
        for(int i = 0; i < distance; i++) {
            env = env.enclosingEnvironment;
        }
        return env;
    }

    @Override
    public String toString() {
        return "[ " + values.toString() + " ]" + (enclosingEnvironment != null ? " -> " + enclosingEnvironment.toString() : "");
    }
}
