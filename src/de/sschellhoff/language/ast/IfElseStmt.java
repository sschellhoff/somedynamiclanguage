package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;

public class IfElseStmt extends Stmt {
	public final Expr condition;
	public final Stmt thenBranch;
	public final Stmt elseBranch;

	public IfElseStmt(Expr condition, Stmt thenBranch, Stmt elseBranch) {
		this.condition = condition;
		this.thenBranch = thenBranch;
		this.elseBranch = elseBranch;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitIfElseStmt(this);
	}
}
