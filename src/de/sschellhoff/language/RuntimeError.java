package de.sschellhoff.language;

public class RuntimeError extends RuntimeException {
    final Token token;

    public RuntimeError(Token token, String msg) {
        super(msg);
        this.token = token;
    }
}
