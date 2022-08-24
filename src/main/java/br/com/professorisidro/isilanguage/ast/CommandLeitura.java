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
		if (language == "java") {
			return id + "= _key." + (var.getType() == IsiVariable.NUMBER ? "nextDouble();" : "nextLine();");
		} else if (language == "c") {
			if (var.getType() == IsiVariable.NUMBER) {
				return "scanf(\"%lf\", &" + id + ");";
			} else if (var.getType() == IsiVariable.TEXT) {
				return "scanf(\"%[^\\n]s\", &" + id + ");";
			} else if (var.getType() == IsiVariable.BOOLEAN) {
				return "scanf(\"%d\", &" + id + ");";
			}
		}
		return "";
	}

	@Override
	public String toString() {
		return "CommandLeitura [id=" + id + "]";
	}

	public String getId() {
		return id;
	}

	public IsiVariable getVar() {
		return var;
	}

}
