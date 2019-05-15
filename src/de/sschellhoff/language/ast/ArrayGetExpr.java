package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;
import de.sschellhoff.language.Token;

public class ArrayGetExpr extends Expr {
	public final Expr array;
	public final Token paren;
	public final Expr idx;

	public ArrayGetExpr(Expr array, Token paren, Expr idx) {
		this.array = array;
		this.paren = paren;
		this.idx = idx;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitArrayGetExpr(this);
	}
}
