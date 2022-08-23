package br.com.professorisidro.isilanguage.ast;

import br.com.professorisidro.isilanguage.datastructures.IsiVariable;

public class CommandLeitura extends AbstractCommand {

	private String id;
	private IsiVariable var;

	public CommandLeitura(String id, IsiVariable var) {
		this.id = id;
		this.var = var;
	}

	@Override
	public String generateCode(String language) {
		if (language == "Java") {
			return id + "= _key." + (var.getType() == IsiVariable.NUMBER ? "nextDouble();" : "nextLine();");
		}
		else if (language == "C") {
			return "";
		}
		return "";
	}

	@Override
	public String toString() {
		return "CommandLeitura [id=" + id + "]";
	}

}
