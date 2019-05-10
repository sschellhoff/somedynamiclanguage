package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;
import de.sschellhoff.language.Token;

public class SetExpr extends Expr {
	public final Expr object;
	public final Token name;
	public final Expr value;

	public SetExpr(Expr object, Token name, Expr value) {
		this.object = object;
		this.name = name;
		this.value = value;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitSetExpr(this);
	}
}
