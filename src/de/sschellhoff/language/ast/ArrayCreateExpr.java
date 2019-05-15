package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;
import de.sschellhoff.language.Token;

public class ArrayCreateExpr extends Expr {
	public final Expr size;
	public final Token paren;

	public ArrayCreateExpr(Expr size, Token paren) {
		this.size = size;
		this.paren = paren;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitArrayCreateExpr(this);
	}
}
