package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;
import de.sschellhoff.language.Token;

public class NullCondOpExpr extends Expr {
	public final Expr object;
	public final Token name;

	public NullCondOpExpr(Expr object, Token name) {
		this.object = object;
		this.name = name;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitNullCondOpExpr(this);
	}
}
