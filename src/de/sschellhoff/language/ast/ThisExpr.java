package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;
import de.sschellhoff.language.Token;

public class ThisExpr extends Expr {
	public final Token keyword;

	public ThisExpr(Token keyword) {
		this.keyword = keyword;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitThisExpr(this);
	}
}
