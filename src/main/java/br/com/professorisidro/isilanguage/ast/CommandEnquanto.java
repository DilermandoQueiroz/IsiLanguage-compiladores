package br.com.professorisidro.isilanguage.ast;

import java.util.ArrayList;

public class CommandEnquanto extends AbstractCommand {

    private String condicao;
    private ArrayList<AbstractCommand> comandos;

    public CommandEnquanto(String condicao, ArrayList<AbstractCommand> comandos) {
        this.condicao = condicao;
        this.comandos = comandos;
    }

    @Override
    public String generateCode(String language) {
        StringBuilder str = new StringBuilder();
        str.append("while (" + condicao + ") {\n");

        for (AbstractCommand cmd : comandos) {
            str.append("\t\t\t" + cmd.generateCode(language) + "\n");
        }

        str.append("\t\t}");

        return str.toString();
    }

    @Override
    public String toString() {
        return "CommandEnquanto [condicao=" + condicao + ", comandos=" + comandos + "]";
    }

    public String getCondicao() {
        return condicao;
    }

    public ArrayList<AbstractCommand> getComandos() {
        return comandos;
    }

}