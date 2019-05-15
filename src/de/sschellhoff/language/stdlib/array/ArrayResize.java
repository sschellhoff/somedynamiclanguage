package de.sschellhoff.language.stdlib.array;

import de.sschellhoff.language.Interpreter;
import de.sschellhoff.language.extension.ExtensionFunction;
import de.sschellhoff.language.extension.ExtensionRuntimeError;

import java.util.List;

public class ArrayResize implements ExtensionFunction {
    @Override
    public String getName() {
        return "arrayresize";
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Object array = arguments.get(0);
        Object size = arguments.get(1);
        if(array instanceof Object[]) {
            if(size instanceof Integer) {
                int newSize = (Integer)size;
                if(newSize <= 0) {
                    throw new ExtensionRuntimeError("Array-size must be greater than 0");
                }
                Object[] oldArray = (Object[])array;
                int oldSize = oldArray.length;
                Object[] newArray = new Object[newSize];
                System.arraycopy(oldArray, 0, newArray, 0, Math.min(oldSize, newSize));
                return newArray;
            }
            throw new ExtensionRuntimeError("Size parameter must be of type int");
        }
        throw new ExtensionRuntimeError("You can only resize arrays");
    }

    @Override
    public int arity() {
        return 2;
    }
}
