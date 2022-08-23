package br.com.professorisidro.isilanguage.cli;

import java.util.List;
import java.util.Scanner;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import br.com.professorisidro.isilanguage.ast.AbstractCommand;
import br.com.professorisidro.isilanguage.ast.CommandAtribuicao;
import br.com.professorisidro.isilanguage.ast.CommandDecisao;
import br.com.professorisidro.isilanguage.ast.CommandEnquanto;
import br.com.professorisidro.isilanguage.ast.CommandEscrita;
import br.com.professorisidro.isilanguage.ast.CommandLeitura;
import br.com.professorisidro.isilanguage.datastructures.IsiSymbol;
import br.com.professorisidro.isilanguage.datastructures.IsiSymbolTable;
import br.com.professorisidro.isilanguage.datastructures.IsiVariable;
import br.com.professorisidro.isilanguage.exceptions.IsiSemanticException;
import br.com.professorisidro.isilanguage.parser.IsiLangLexer;
import br.com.professorisidro.isilanguage.parser.IsiLangParser;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "interpret", description = "Interprets a program written in IsiLanguage")
public class InterpretCommand implements Runnable {

    @Parameters(arity = "1", paramLabel = "<programPath>", description = "path to the program to be interpreted")
    private String program;

    private final ScriptEngineManager mgr = new ScriptEngineManager();
    private final ScriptEngine engine = mgr.getEngineByName("JavaScript");

    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            IsiLangLexer lexer = new IsiLangLexer(CharStreams.fromFileName(program));
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            IsiLangParser parser = new IsiLangParser(tokenStream);

            parser.prog();

            System.out.println("Compilation Successful");

            interpret(parser.getSymbolTable(), parser.getComandos(), scanner);
        } catch (IsiSemanticException ex) {
            System.err.println("Semantic error - " + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("ERROR " + ex.getMessage());
        }
    }

    private void interpret(IsiSymbolTable table, List<AbstractCommand> commands, Scanner scanner) throws Exception {
        for (AbstractCommand acmd : commands) {
            if (acmd instanceof CommandAtribuicao) {
                CommandAtribuicao cmd = (CommandAtribuicao) acmd;
                IsiVariable variable = (IsiVariable) table.get(cmd.getId());
                Object value = eval(table, cmd.getExpr());

                switch (variable.getType()) {
                    case IsiVariable.NUMBER:
                        variable.setValue((double) value);
                        break;
                    case IsiVariable.TEXT:
                        variable.setValue((String) value);
                        break;

                    default:
                        throw new RuntimeException("Unknown variable type");
                }

            } else if (acmd instanceof CommandEscrita) {
                CommandEscrita cmd = (CommandEscrita) acmd;
                System.out.println(eval(table, cmd.getId()));
            } else if (acmd instanceof CommandLeitura) {
                CommandLeitura cmd = (CommandLeitura) acmd;
                IsiVariable variable = (IsiVariable) table.get(cmd.getId());

                switch (cmd.getVar().getType()) {
                    case IsiVariable.NUMBER:
                        variable.setValue(scanner.nextDouble());
                        break;
                    case IsiVariable.TEXT:
                        variable.setValue(scanner.nextLine());
                        break;
                    default:
                        throw new RuntimeException("Unknown variable type");
                }
            } else if (acmd instanceof CommandDecisao) {
                CommandDecisao cmd = (CommandDecisao) acmd;
                boolean result = (boolean) eval(table, cmd.getCondition());
                if (result)
                    interpret(table, cmd.getListaTrue(), scanner);
                else
                    interpret(table, cmd.getListaFalse(), scanner);
            } else if (acmd instanceof CommandEnquanto) {
                CommandEnquanto cmd = (CommandEnquanto) acmd;
                boolean result = (boolean) eval(table, cmd.getCondicao());
                while (result) {
                    interpret(table, cmd.getComandos(), scanner);
                    result = (boolean) eval(table, cmd.getCondicao());
                }
            }
        }
    }

    private Object eval(IsiSymbolTable table, String expr) throws Exception {
        StringBuilder expression = new StringBuilder();
        for (IsiSymbol symbol : table.getAll()) {
            if (symbol instanceof IsiVariable) {
                IsiVariable variable = (IsiVariable) symbol;
                String declaration = String.format("var %s = %s;", variable.getName(), variable.getValue());
                expression.append(declaration);
            }
        }
        expression.append(expr);

        return engine.eval(expression.toString());
    }

}
