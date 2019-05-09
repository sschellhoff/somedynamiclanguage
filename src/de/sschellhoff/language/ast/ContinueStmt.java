package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;

public class ContinueStmt extends Stmt {

	public ContinueStmt() {
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitContinueStmt(this);
	}
}
