package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;
import java.util.List;

public class BlockStmt extends Stmt {
	public final List<Stmt> stmts;

	public BlockStmt(List<Stmt> stmts) {
		this.stmts = stmts;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitBlockStmt(this);
	}
}
