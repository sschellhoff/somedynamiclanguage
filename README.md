# This is a dynamic language implemented in Java. Currently it has no name.

## Language
The language itself, and it's implementation, is based on Crafting interpreters of Bob Nystrom. But it is not the same as his languages, named Lox. There are many differences in his and my version. So don't expect Lox programs to run in this interpreter without errors.

## Datatypes
* int
* float (double precise)
* string
* boolean
* array
* function
* class
You don't tell the interpreter what type of data you want, as it is a dynamic language. A variable of type int can change it's type at runtime, so you can set a variable to the value 1337 of type int, and to the value "Hello, World" of type string later.

## examples
Look at the 'programs' directory for some examples. Later on, i will write some more.
There is an implementation of a brainfuck interpreter int 'programs/brainfuck' which shows many concepts of this language.
