package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;
import de.sschellhoff.language.Token;
import java.util.List;

public class CallExpr extends Expr {
	public final Expr callee;
	public final Token paren;
	public final List<Expr> arguments;

	public CallExpr(Expr callee, Token paren, List<Expr> arguments) {
		this.callee = callee;
		this.paren = paren;
		this.arguments = arguments;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitCallExpr(this);
	}
}
