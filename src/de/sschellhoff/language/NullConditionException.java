package de.sschellhoff.language;

public class NullConditionException extends RuntimeException {
    NullConditionException() {
        super(null, null, false, false);
    }
}
