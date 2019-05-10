package de.sschellhoff.language.ast;

import de.sschellhoff.language.Visitor;
import de.sschellhoff.language.Token;

public class ImportStmt extends Stmt {
	public final Token keyword;
	public final String filename;

	public ImportStmt(Token keyword, String filename) {
		this.keyword = keyword;
		this.filename = filename;
	}

	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitImportStmt(this);
	}
}
