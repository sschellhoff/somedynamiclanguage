package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;
import de.sschellhoff.language.Token;

public class SuperExpr extends Expr {
	public final Token keyword;
	public final Token method;

	public SuperExpr(Token keyword, Token method) {
		this.keyword = keyword;
		this.method = method;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitSuperExpr(this);
	}
}
