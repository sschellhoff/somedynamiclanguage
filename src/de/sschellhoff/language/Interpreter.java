package de.sschellhoff.language;

import de.sschellhoff.language.ast.*;

import javax.security.auth.callback.LanguageCallback;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter implements Visitor<Object> {
    private final ErrorWriter errorWriter;
    private final Environment globals = new Environment();
    private Environment environment = globals;
    private final Map<Expr, Integer> locals = new HashMap<>();
    private String currentPath;

    public Interpreter(String startingPath, ErrorWriter errorWriter) {
        this.errorWriter = errorWriter;
        this.currentPath = startingPath;
        globals.define(Token.identifier("clock"), new LangCallable() {

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return (double)System.currentTimeMillis() / 1000.0;
            }

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public String toString() { return "< fun clock>"; }
        });
        globals.define(Token.identifier("str"), new LangCallable() {
            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                if(arguments.size() != 1) {
                    throw new RuntimeError(Token.identifier("str"), "wrong number of arguments");
                }
                Object parameter = arguments.get(0);
                if(parameter == null) {
                    return "null";
                } else {
                    return parameter.toString();
                }
            }

            @Override
            public int arity() {
                return 1;
            }
        });
    }

    public boolean interpret(List<Stmt> stmts) {
        try {
            for(Stmt stmt : stmts) {
                execute(stmt);
            }
        } catch (RuntimeError error) {
            System.err.println(error.getMessage() + " at line: " + error.token.line);
            return false;
        }
        return true;
    }

    private void execute(Stmt stmt) {
        stmt.accept(this);
    }

    public void executeBlock(List<Stmt> stmts, Environment env) {
        Environment previousEnvironment = this.environment;
        try {
            this.environment = env;
            for(Stmt st : stmts) {
                execute(st);
            }
        } finally {
            this.environment = previousEnvironment;
        }
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    private boolean evaluateTruth(Object object) {
        if ( object instanceof Boolean ) {
            return (boolean)object;
        }
        return object != null;
    }

    private boolean isEqual(Object a, Object b) {
        if ( a == null && b == null ) {
            return true;
        }
        if ( a == null ) {
            return false;
        }
        return a.equals(b);
    }

    private boolean isNumber(Object o) {
        return isInt(o) || isFloat(o);
    }

    private boolean isFloat(Object o) {
        return o instanceof Double;
    }

    private boolean isInt(Object o) {
        return o instanceof Integer;
    }

    private boolean isString(Object o) {
        return o instanceof String;
    }

    private String getString(Object o) {
        return o == null ? "null" : o.toString();
    }

    private Class combineNumberTypes(Object a, Object b) {
        if(isFloat(a) || isFloat(b)) {
            return Double.TYPE;
        }
        return Integer.TYPE;
    }

    public void resolve(Expr expr, int depth) {
        locals.put(expr, depth);
    }

    private Object lookUpVariable(Token name, Expr expr) {
        Integer distance = locals.get(expr);
        if(distance != null) {
            return environment.getAt(distance, name);
        } else {
            return globals.get(name);
        }
    }

    @Override
    public Object visitAssignExpr(AssignExpr expr) {
        Object value = evaluate(expr.value);

        Integer distance = locals.get(expr);
        if(distance != null) {
            environment.assignAt(distance, expr.name, value);
        } else {
            globals.assign(expr.name, value);
        }
        return value;
    }

    @Override
    public Object visitBinaryExpr(BinaryExpr expr) {
        Object lhs = evaluate(expr.lhs);
        switch(expr.op.type) {
            case MINUS: {
                if (!isNumber(lhs)) {
                    throw new RuntimeError(expr.op, "Left operand must be a number!");
                }
                Object rhs = evaluate(expr.rhs);
                if (!isNumber(rhs)) {
                    throw new RuntimeError(expr.op, "Right operand must be a number!");
                }
                if(isFloat(lhs) && isFloat(rhs)) {
                    return (double)lhs - (double)rhs;
                } else if(isInt(lhs) && isInt(rhs)) {
                    return (int) lhs - (int) rhs;
                } else if(isFloat(lhs) && isInt(rhs)) {
                    return (double) lhs - (int) rhs;
                } else if(isInt(lhs) && isFloat(rhs)) {
                    return (int) lhs - (double) rhs;
                } else {
                    throw new RuntimeError(expr.op, "Cannot subtract the given types");
                }
            }
            case STAR: {
                if (!isNumber(lhs)) {
                    throw new RuntimeError(expr.op, "Left operand must be a number!");
                }
                Object rhs = evaluate(expr.rhs);
                if (!isNumber(rhs)) {
                    throw new RuntimeError(expr.op, "Right operand must be a number!");
                }
                if(isFloat(lhs) && isFloat(rhs)) {
                    return (double)lhs * (double)rhs;
                } else if(isInt(lhs) && isInt(rhs)) {
                    return (int) lhs * (int) rhs;
                } else if(isFloat(lhs) && isInt(rhs)) {
                    return (double) lhs * (int) rhs;
                } else if(isInt(lhs) && isFloat(rhs)) {
                    return (int) lhs * (double) rhs;
                } else {
                    throw new RuntimeError(expr.op, "Cannot multiply the given types");
                }
            }
            case SLASH: {
                if (!isNumber(lhs)) {
                    throw new RuntimeError(expr.op, "Left operand must be a number!");
                }
                Object rhs = evaluate(expr.rhs);
                if (!isNumber(rhs)) {
                    throw new RuntimeError(expr.op, "Right operand must be a number!");
                }
                if(isFloat(lhs) && isFloat(rhs)) {
                    return (double)lhs / (double)rhs;
                } else if(isInt(lhs) && isInt(rhs)) {
                    return (int) lhs / (int) rhs;
                } else if(isFloat(lhs) && isInt(rhs)) {
                    return (double) lhs / (int) rhs;
                } else if(isInt(lhs) && isFloat(rhs)) {
                    return (int) lhs / (double) rhs;
                } else {
                    throw new RuntimeError(expr.op, "Cannot divide the given types");
                }
            }
            case MOD: {
                if(!isInt(lhs)) {
                    throw new RuntimeError(expr.op, "Left operand must be an integer!");
                }
                Object rhs = evaluate(expr.rhs);
                if(!isInt(rhs)) {
                    throw new RuntimeError(expr.op, "Right operand must be an integer!");
                }
                return (int) lhs % (int) rhs;
            }
            case PLUS: {
                Object rhs = evaluate(expr.rhs);
                if (isNumber(lhs)) {
                    if(isString(rhs)) {
                        return getString(lhs) + getString(rhs);
                    } else if(!isNumber(rhs)) {
                        throw new RuntimeError(expr.op, "Right operand must be a number!");
                    }
                    if(isFloat(lhs) && isFloat(rhs)) {
                        return (double)lhs + (double)rhs;
                    } else if(isInt(lhs) && isInt(rhs)) {
                        return (int) lhs + (int) rhs;
                    } else if(isFloat(lhs) && isInt(rhs)) {
                        return (double) lhs + (int) rhs;
                    } else if(isInt(lhs) && isFloat(rhs)) {
                        return (int) lhs + (double) rhs;
                    } else {
                        throw new RuntimeError(expr.op, "Cannot add the given types");
                    }
                } else if (isString(lhs) || isString(rhs)) {
                    return getString(lhs) + getString(rhs);
                }
                throw new RuntimeError(expr.op, "Cannot add the given types!");
            }
            case GREATER: {
                if (!isNumber(lhs)) {
                    throw new RuntimeError(expr.op, "Left operand must be a number!");
                }
                Object rhs = evaluate(expr.rhs);
                if(!isNumber(rhs)) {
                    throw new RuntimeError(expr.op, "Right operand must be a number!");
                }
                if(isFloat(lhs) && isFloat(rhs)) {
                    return (double)lhs > (double)rhs;
                } else if(isInt(lhs) && isInt(rhs)) {
                    return (int) lhs > (int) rhs;
                } else if(isFloat(lhs) && isInt(rhs)) {
                    return (double) lhs > (int) rhs;
                } else if(isInt(lhs) && isFloat(rhs)) {
                    return (int) lhs > (double) rhs;
                } else {
                    throw new RuntimeError(expr.op, "Cannot compare the given types");
                }
            }
            case LESS: {
                if (!isNumber(lhs)) {
                    throw new RuntimeError(expr.op, "Left operand must be a number!");
                }
                Object rhs = evaluate(expr.rhs);
                if (!isNumber(rhs)) {
                    throw new RuntimeError(expr.op, "Right operand must be a number!");
                }
                if(isFloat(lhs) && isFloat(rhs)) {
                    return (double)lhs < (double)rhs;
                } else if(isInt(lhs) && isInt(rhs)) {
                    return (int) lhs < (int) rhs;
                } else if(isFloat(lhs) && isInt(rhs)) {
                    return (double) lhs < (int) rhs;
                } else if(isInt(lhs) && isFloat(rhs)) {
                    return (int) lhs < (double) rhs;
                } else {
                    throw new RuntimeError(expr.op, "Cannot compare the given types");
                }
            }
            case GREATER_EQUAL: {
                if (!isNumber(lhs)) {
                    throw new RuntimeError(expr.op, "Left operand must be a number!");
                }
                Object rhs = evaluate(expr.rhs);
                if (!isNumber(rhs)) {
                    throw new RuntimeError(expr.op, "Right operand must be a number!");
                }
                if(isFloat(lhs) && isFloat(rhs)) {
                    return (double)lhs >= (double)rhs;
                } else if(isInt(lhs) && isInt(rhs)) {
                    return (int) lhs >= (int) rhs;
                } else if(isFloat(lhs) && isInt(rhs)) {
                    return (double) lhs >= (int) rhs;
                } else if(isInt(lhs) && isFloat(rhs)) {
                    return (int) lhs >= (double) rhs;
                } else {
                    throw new RuntimeError(expr.op, "Cannot compare the given types");
                }
            }
            case LESS_EQUAL: {
                if (!isNumber(lhs)) {
                throw new RuntimeError(expr.op, "Left operand must be a number!");
                }
                Object rhs = evaluate(expr.rhs);
                if (!isNumber(rhs)) {
                    throw new RuntimeError(expr.op, "Right operand must be a number!");
                }
                if(isFloat(lhs) && isFloat(rhs)) {
                    return (double)lhs <= (double)rhs;
                } else if(isInt(lhs) && isInt(rhs)) {
                    return (int) lhs <= (int) rhs;
                } else if(isFloat(lhs) && isInt(rhs)) {
                    return (double) lhs <= (int) rhs;
                } else if(isInt(lhs) && isFloat(rhs)) {
                    return (int) lhs <= (double) rhs;
                } else {
                    throw new RuntimeError(expr.op, "Cannot compare the given types");
                }
            }
            case EXCL_EQUAL: {
                Object rhs = evaluate(expr.rhs);
                return !isEqual(lhs, rhs);
            }
            case EQUAL_EQUAL: {
                Object rhs = evaluate(expr.rhs);
                return isEqual(lhs, rhs);
            }
            case AND: {
                if(evaluateTruth(lhs)) {
                    return evaluateTruth(evaluate(expr.rhs));
                }
                return false;
            }
            case OR: {
                if (!evaluateTruth(lhs)) {
                    return evaluateTruth(evaluate(expr.rhs));
                }
                return true;
            }
            case NULL_COALESCING: {
                if(evaluateTruth(lhs)) {
                    return lhs;
                } else {
                    return evaluate(expr.rhs);
                }
            }
        }
        return null;
    }

    @Override
    public Object visitUnaryExpr(UnaryExpr expr) {
        Object e = evaluate(expr.expr);
        switch(expr.op.type) {
            case MINUS:
                if ( !isNumber(e) ) {
                    throw new RuntimeError(expr.op, "Operand must be a number");
                }
                return -(double)e;
            case EXCL_MARK:
                return !evaluateTruth(e);
        }
        return null;
    }

    @Override
    public Object visitLiteralExpr(LiteralExpr expr) {
        return expr.value;
    }

    @Override
    public Object visitGroupingExpr(GroupingExpr expr) {
        return evaluate(expr.expr);
    }

    @Override
    public Object visitVarDeclStmt(VarDeclStmt stmt) {
        Object value = stmt.initializer == null ? null : evaluate(stmt.initializer);
        environment.define(stmt.name, value);
        return null;
    }

    @Override
    public Object visitBlockStmt(BlockStmt stmt) {
        executeBlock(stmt.stmts, new Environment(environment));
        return null;
    }

    @Override
    public Object visitIfElseStmt(IfElseStmt stmt) {
        if(evaluateTruth(evaluate(stmt.condition))) {
            execute(stmt.thenBranch);
        } else if(stmt.elseBranch != null) {
            execute(stmt.elseBranch);
        }
        return null;
    }

    @Override
    public Object visitWhileStmt(WhileStmt stmt) {
        while(evaluateTruth(evaluate(stmt.condition))) {
            try {
                execute(stmt.body);
            } catch (BreakException be) {
                break;
            } catch (ContinueException ce) {
                continue;
            }
        }
        return null;
    }

    @Override
    public Object visitBreakStmt(BreakStmt stmt) {
        throw new BreakException();
    }

    @Override
    public Object visitContinueStmt(ContinueStmt stmt) {
        throw new ContinueException();
    }

    @Override
    public Object visitFuncDefStmt(FuncDefStmt stmt) {
        LangFunction function = new LangFunction(stmt, environment, false);
        environment.define(stmt.name, function);
        return null;
    }

    @Override
    public Object visitReturnStmt(ReturnStmt stmt) {
        Object value = null;
        if(stmt.value != null) {
            value = evaluate(stmt.value);
        }
        throw new ReturnException(value);
    }

    @Override
    public Object visitClassDeclStmt(ClassDeclStmt stmt) {
        environment.define(stmt.name, null);
        Map<String, LangFunction> methods = new HashMap<>();
        for(FuncDefStmt method: stmt.methods) {
            LangFunction function = new LangFunction(method, environment, method.name.lexeme.equals("init"));
            methods.put(method.name.lexeme, function);
        }
        LangClass _class = new LangClass(stmt.name, methods);
        environment.assign(stmt.name, _class);
        return null;
    }

    @Override
    public Object visitImportStmt(ImportStmt stmt) {
        String oldPath = currentPath;
        try {
            Path filepath = Misc.toAbsolutePath(stmt.filename, currentPath);
            currentPath = Misc.getDirectory(filepath).toString();

            String sourcecode = new String(Files.readAllBytes(filepath));
            Scanner scanner = new Scanner(sourcecode, errorWriter);
            List<Token> tokens = scanner.scan();
            if(scanner.hadError()) {
                throw new RuntimeError(stmt.keyword, "Cannot scan file");
            }
            Parser parser = new Parser(tokens, errorWriter);
            List<Stmt> program = parser.parse();
            if(parser.hadError()) {
                throw new RuntimeError(stmt.keyword, "Cannot parse file");
            }
            Resolver resolver = new Resolver(this, errorWriter);
            resolver.resolve(program);
            if(resolver.hadError()) {
                throw new RuntimeError(stmt.keyword, "Cannot resolve file");
            }
            if(!this.interpret(program)) {
                throw new RuntimeError(stmt.keyword, "Cannot execute file");
            }
        } catch (IOException e) {
            throw new RuntimeError(stmt.keyword, "cannot open file '" + stmt.filename + "'");
        } finally {
            currentPath = oldPath;
        }
        return null;
    }

    @Override
    public Object visitVarExpr(VarExpr expr) {
        return lookUpVariable(expr.name, expr);
    }

    @Override
    public Object visitTernaryExpr(TernaryExpr expr) {
        if(evaluateTruth(evaluate(expr.condition))) {
            return evaluate(expr.then_case);
        } else {
            return evaluate(expr.else_case);
        }
    }

    @Override
    public Object visitCallExpr(CallExpr expr) {
        Object callee = evaluate(expr.callee);

        List<Object> arguments = new ArrayList<>();
        for(Expr argument : expr.arguments) {
            arguments.add(evaluate(argument));
        }

        if(!(callee instanceof LangCallable)) {
            throw new RuntimeError(expr.paren, "You tried to call something that is no function");
        }

        LangCallable function = (LangCallable)callee;

        if(arguments.size() != function.arity()) {
            throw new RuntimeError(expr.paren, "Expected " + function.arity() + " arguments but got " + arguments.size());
        }

        return function.call(this, arguments);
    }

    @Override
    public Object visitGetExpr(GetExpr expr) {
        Object object = evaluate(expr.object);
        if(object instanceof LangInstance) {
            return ((LangInstance)object).get(expr.name);
        }
        throw new RuntimeError(expr.name, "You can only access the properties of instances");
    }

    @Override
    public Object visitSetExpr(SetExpr expr) {
        Object object = evaluate(expr.object);
        if(!(object instanceof LangInstance)) {
            throw new RuntimeError(expr.name, "You can only access the properties of instances");
        }

        Object value = evaluate(expr.value);
        ((LangInstance)object).set(expr.name, value);
        return value;
    }

    @Override
    public Object visitThisExpr(ThisExpr expr) {
        return lookUpVariable(expr.keyword, expr);
    }

    @Override
    public Object visitNullCondOpExpr(NullCondOpExpr expr) {
        Object object = evaluate(expr.object);
        if(object == null) {
            throw new NullConditionException();
        } else if(object instanceof LangInstance) {
            return ((LangInstance)object).get(expr.name);
        }
        throw new RuntimeError(expr.name, "You can only access properties of instances");
    }

    @Override
    public Object visitNullCondTopExpr(NullCondTopExpr expr) {
        try {
            return evaluate(expr.expr);
        } catch(NullConditionException nce) {
            return null;
        }
    }

    @Override
    public Object visitExprStmt(ExprStmt stmt) {
        evaluate(stmt.expr);
        return null;
    }

    @Override
    public Object visitPrintStmt(PrintStmt stmt) {
        Object result = evaluate(stmt.expr);
        if(result == null) {
            System.out.println("null");
        } else {
            System.out.println(result.toString());
        }
        return null;
    }
}
