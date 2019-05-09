package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;
import de.sschellhoff.language.Token;
import java.util.List;

public class FuncDefStmt extends Stmt {
	public final Token name;
	public final List<Token> parameters;
	public final Stmt body;

	public FuncDefStmt(Token name, List<Token> parameters, Stmt body) {
		this.name = name;
		this.parameters = parameters;
		this.body = body;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitFuncDefStmt(this);
	}
}
