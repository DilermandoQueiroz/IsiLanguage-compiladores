package br.com.professorisidro.isilanguage.ast;

import br.com.professorisidro.isilanguage.datastructures.IsiVariable;

public class CommandEscrita extends AbstractCommand {

	private String id;
	private IsiVariable var;

	public CommandEscrita(String id, IsiVariable var) {
		this.id = id;
		this.var = var;
	}

	@Override
	public String generateCode(String language) {
		if (language == "java") {
			return "System.out.println(" + id + ");";
		} else if (language == "c") {
			if (var.getType() == IsiVariable.NUMBER) {
				return "printf(\"%lf\\n\", " + id + ");";
			} else if (var.getType() == IsiVariable.TEXT) {
				return "printf(\"%s\\n\", " + id + ");";
			} else if (var.getType() == IsiVariable.BOOLEAN) {
				return "printf(\"%d\\n\", " + id + ");";
			}
		}
		return "";
	}

	@Override
	public String toString() {
		return "CommandEscrita [id=" + id + "]";
	}

	public String getId() {
		return id;
	}

}
