package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;

public abstract class Stmt{
	public abstract <R> R accept(Visitor<R> visitor);
}
