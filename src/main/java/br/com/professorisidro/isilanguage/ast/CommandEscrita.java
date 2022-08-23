package br.com.professorisidro.isilanguage.ast;

public class CommandEscrita extends AbstractCommand {

	private String id;

	public CommandEscrita(String id) {
		this.id = id;
	}

	@Override
	public String generateCode(String language) {
		if (language == "Java") {
			return "System.out.println(" + id + ");";
		}
		else if (language == "C") {
			return "print(" + id + ");";
		}
		return "";
	}

	@Override
	public String toString() {
		return "CommandEscrita [id=" + id + "]";
	}

}
