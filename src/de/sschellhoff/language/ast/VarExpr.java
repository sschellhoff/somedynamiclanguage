package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;
import de.sschellhoff.language.Token;

public class VarExpr extends Expr {
	public final Token name;

	public VarExpr(Token name) {
		this.name = name;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitVarExpr(this);
	}
}
