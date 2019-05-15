package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;
import de.sschellhoff.language.Token;

public class ArraySetExpr extends Expr {
	public final Expr array;
	public final Token paren;
	public final Expr idx;
	public final Expr value;

	public ArraySetExpr(Expr array, Token paren, Expr idx, Expr value) {
		this.array = array;
		this.paren = paren;
		this.idx = idx;
		this.value = value;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitArraySetExpr(this);
	}
}
