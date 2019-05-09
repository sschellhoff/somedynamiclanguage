package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;

public class WhileStmt extends Stmt {
	public final Expr condition;
	public final Stmt body;

	public WhileStmt(Expr condition, Stmt body) {
		this.condition = condition;
		this.body = body;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitWhileStmt(this);
	}
}
