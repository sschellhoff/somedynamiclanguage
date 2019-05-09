package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;

public class BreakStmt extends Stmt {

	public BreakStmt() {
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitBreakStmt(this);
	}
}
