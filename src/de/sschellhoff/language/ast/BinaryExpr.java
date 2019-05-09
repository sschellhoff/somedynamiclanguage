package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;
import de.sschellhoff.language.Token;

public class BinaryExpr extends Expr {
	public final Token op;
	public final Expr lhs;
	public final Expr rhs;

	public BinaryExpr(Token op, Expr lhs, Expr rhs) {
		this.op = op;
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitBinaryExpr(this);
	}
}
