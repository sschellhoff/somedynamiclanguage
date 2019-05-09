package de.sschellhoff.language;

public class ConsoleErrorWriter implements ErrorWriter {
    @Override
    public void write(String msg, int line) {
        System.out.println(msg + " at line: " + line);
    }
}
