package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;
import de.sschellhoff.language.Token;
import java.util.List;

public class ClassDeclStmt extends Stmt {
	public final Token name;
	public final List<FuncDefStmt> methods;

	public ClassDeclStmt(Token name, List<FuncDefStmt> methods) {
		this.name = name;
		this.methods = methods;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitClassDeclStmt(this);
	}
}
