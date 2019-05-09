package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;
import de.sschellhoff.language.Token;

public class VarDeclStmt extends Stmt {
	public final Token name;
	public final Expr initializer;

	public VarDeclStmt(Token name, Expr initializer) {
		this.name = name;
		this.initializer = initializer;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitVarDeclStmt(this);
	}
}
