package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;
import java.lang.Object;

public class LiteralExpr extends Expr {
	public final Object value;

	public LiteralExpr(Object value) {
		this.value = value;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitLiteralExpr(this);
	}
}
