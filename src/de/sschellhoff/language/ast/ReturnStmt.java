package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;
import de.sschellhoff.language.Token;

public class ReturnStmt extends Stmt {
	public final Token keyword;
	public final Expr value;

	public ReturnStmt(Token keyword, Expr value) {
		this.keyword = keyword;
		this.value = value;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitReturnStmt(this);
	}
}
