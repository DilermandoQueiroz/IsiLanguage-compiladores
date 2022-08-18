package br.com.professorisidro.isilanguage.ast;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import br.com.professorisidro.isilanguage.datastructures.IsiSymbol;
import br.com.professorisidro.isilanguage.datastructures.IsiSymbolTable;

public class IsiProgram {

	private IsiSymbolTable varTable;
	private ArrayList<AbstractCommand> comandos;
	private String programName;

	public void generateTarget() {
		StringBuilder str = new StringBuilder();
		str.append("import java.util.Scanner;\n");
		str.append("public class MainClass {\n");
		str.append("\tpublic static void main(String args[]) {\n");
		str.append("\t\tScanner _key = new Scanner(System.in);\n");
		for (IsiSymbol symbol : varTable.getAll()) {
			str.append("\t\t" + symbol.generateJavaCode() + "\n");
		}
		for (AbstractCommand command : comandos) {
			str.append("\t\t" + command.generateJavaCode() + "\n");
		}
		str.append("\t}");
		str.append("}");

		try {
			FileWriter fr = new FileWriter(new File("MainClass.java"));
			fr.write(str.toString());
			fr.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public IsiSymbolTable getVarTable() {
		return varTable;
	}

	public void setVarTable(IsiSymbolTable varTable) {
		this.varTable = varTable;
	}

	public ArrayList<AbstractCommand> getComandos() {
		return comandos;
	}

	public void setComandos(ArrayList<AbstractCommand> comandos) {
		this.comandos = comandos;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

}
