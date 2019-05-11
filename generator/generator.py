class AstType():
    def __init__(self, name, package = "", imports = [], fields=None, baseclass=None):
        self.name = name
        self.package = package
        self.imports = imports
        self.fields = fields
        self.baseclass = baseclass

    def __str__(self):
        extends_string = ""
        if self.baseclass != None:
            extends_string = " extends " + self.baseclass + " "
        result = ""
        if self.package != "":
            result += "package " + self.package + ";\n\n"
        if len(self.imports) > 0:
            for imp in self.imports:
                result += "import " + imp + ";\n"
            result += "\n"
        result += "public "
        if self.fields == None:
            result += "abstract "
        result += "class " + self.name + extends_string + "{\n"
        if self.fields != None:
            for field in self.fields:
                result += "\tpublic final " + field[0] + " " + field[1] + ";\n"
            result += "\n\tpublic " + self.name + "(" + parameter_string(self.fields) + ") {\n"
            for field in self.fields:
                result += "\t\t" + "this." + field[1] + " = " + field[1] + ";\n"
            result += "\t}\n"
            result += "\n\tpublic <R> R accept(Visitor<R> visitor) {\n"
            result += "\t\treturn visitor.visit" + self.name + "(this);\n"
            result += "\t}\n"
        else:
            result += "\tpublic abstract <R> R accept(Visitor<R> visitor);\n"
        result += "}\n"
        return result

class VisitorInterface():
    def __init__(self, types, types_package, package):
        self.types = types
        self.types_package = types_package
        self.package = package

    def __str__(self):
        result = "package " + self.package + ";\n\n"
        for t in self.types:
            if t.baseclass == None:
                continue
            type_name = t.name
            result += "import " + self.types_package + "." + type_name + ";\n"
        result += "\n"
        result += "public interface Visitor<R> {\n"
        for t in self.types:
            if t.baseclass == None:
                continue
            type_name = t.name
            result += "\tR visit" + type_name + "(" + type_name + " " + t.baseclass.lower() + ");\n"
        result += "}\n"
        return result

def parameter_string(fields):
    if len(fields) == 0:
        return ""
    result = fields[0][0] + " " + fields[0][1]
    for i in range(1, len(fields)):
        field = fields[i]
        result += ", " + field[0] + " " + field[1]
    return result

def main():
    base_path = "../src/de/sschellhoff/language/"
    base_package = "de.sschellhoff.language"
    path = base_path + "ast/"
    package = base_package + ".ast"
    ast_data = []
    ast_data.append(("Expr", [], None, None))
    ast_data.append(("AssignExpr", [base_package + ".Token"], [("Token", "name"), ("Expr", "value")], "Expr"))
    ast_data.append(("BinaryExpr", [base_package + ".Token"], [("Token", "op"), ("Expr", "lhs"), ("Expr", "rhs")], "Expr"))
    ast_data.append(("UnaryExpr", [base_package + ".Token"], [("Token", "op"), ("Expr", "expr")], "Expr"))
    ast_data.append(("LiteralExpr", ["java.lang.Object"], [("Object", "value")], "Expr"))
    ast_data.append(("GroupingExpr", [], [("Expr", "expr")], "Expr"))
    ast_data.append(("VarExpr", [base_package + ".Token"], [("Token", "name")], "Expr"))
    ast_data.append(("TernaryExpr", [], [("Expr", "condition"), ("Expr", "then_case"), ("Expr", "else_case")], "Expr"))
    ast_data.append(("CallExpr", [base_package + ".Token", "java.util.List"], [("Expr", "callee"), ("Token", "paren"), ("List<Expr>", "arguments")], "Expr"))
    ast_data.append(("GetExpr", [base_package + ".Token"], [("Expr", "object"), ("Token", "name")], "Expr"))
    ast_data.append(("SetExpr", [base_package + ".Token"], [("Expr", "object"), ("Token", "name"), ("Expr", "value")], "Expr"))
    ast_data.append(("ThisExpr", [base_package + ".Token"], [("Token", "keyword")], "Expr"))
    ast_data.append(("NullCondOpExpr", [base_package + ".Token"], [("Expr", "object"), ("Token", "name")], "Expr"))

    ast_data.append(("Stmt", [], None, None))
    ast_data.append(("ExprStmt", [], [("Expr", "expr")], "Stmt"))
    ast_data.append(("PrintStmt", [], [("Expr", "expr")], "Stmt"))
    ast_data.append(("VarDeclStmt", [base_package + ".Token"], [("Token", "name"), ("Expr", "initializer")], "Stmt"))
    ast_data.append(("BlockStmt", ["java.util.List"], [("List<Stmt>", "stmts")], "Stmt"));
    ast_data.append(("IfElseStmt", [], [("Expr", "condition"), ("Stmt", "thenBranch"), ("Stmt", "elseBranch")], "Stmt"))
    ast_data.append(("WhileStmt", [], [("Expr", "condition"), ("Stmt", "body")], "Stmt"))
    ast_data.append(("BreakStmt", [], [], "Stmt"))
    ast_data.append(("ContinueStmt", [], [], "Stmt"))
    ast_data.append(("FuncDefStmt", [base_package + ".Token", "java.util.List"], [("Token", "name"), ("List<Token>", "parameters"), ("Stmt", "body")], "Stmt"))
    ast_data.append(("ReturnStmt", [base_package + ".Token"], [("Token", "keyword"), ("Expr", "value")], "Stmt"))
    ast_data.append(("ClassDeclStmt", [base_package + ".Token", "java.util.List"], [("Token", "name"), ("List<FuncDefStmt>", "methods")], "Stmt"))
    ast_data.append(("ImportStmt", [base_package + ".Token"], [("Token", "keyword"), ("String", "filename")], "Stmt"))
    types = []
    for data in ast_data:
        ast_type = AstType(data[0], package, [base_package + ".Visitor"] + data[1], data[2], data[3])
        types.append(ast_type)
        with open(path + ast_type.name + ".java", "w") as f:
            f.write(str(ast_type))
    with open(base_path + "Visitor.java", "w") as f:
        f.write(str(VisitorInterface(types, package, base_package)))

if __name__ == '__main__':
    main()
