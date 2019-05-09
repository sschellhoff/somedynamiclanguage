package de.sschellhoff.language;

public interface ErrorWriter {
    void write(String msg, int line);
}
