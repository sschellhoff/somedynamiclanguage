package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;
import de.sschellhoff.language.Token;

public class AssignExpr extends Expr {
	public final Token name;
	public final Expr value;

	public AssignExpr(Token name, Expr value) {
		this.name = name;
		this.value = value;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitAssignExpr(this);
	}
}
