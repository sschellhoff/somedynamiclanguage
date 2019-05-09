package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;

public abstract class Expr{
	public abstract <R> R accept(Visitor<R> visitor);
}
