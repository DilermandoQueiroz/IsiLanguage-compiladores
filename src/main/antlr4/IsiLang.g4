grammar IsiLang;

@header {
	import br.com.professorisidro.isilanguage.datastructures.IsiSymbol;
	import br.com.professorisidro.isilanguage.datastructures.IsiVariable;
	import br.com.professorisidro.isilanguage.datastructures.IsiSymbolTable;
	import br.com.professorisidro.isilanguage.exceptions.IsiSemanticException;
	import br.com.professorisidro.isilanguage.ast.IsiProgram;
	import br.com.professorisidro.isilanguage.ast.AbstractCommand;
	import br.com.professorisidro.isilanguage.ast.CommandLeitura;
	import br.com.professorisidro.isilanguage.ast.CommandEscrita;
	import br.com.professorisidro.isilanguage.ast.CommandAtribuicao;
	import br.com.professorisidro.isilanguage.ast.CommandDecisao;
	import br.com.professorisidro.isilanguage.ast.CommandEnquanto;
	import java.util.ArrayList;
	import java.util.Stack;
}

@members {
	private int _tipo;
	private String _varName;
	private String _varValue;
	private IsiSymbolTable symbolTable = new IsiSymbolTable();
	private IsiSymbol symbol;

	private IsiProgram program = new IsiProgram();
	private ArrayList<AbstractCommand> curThread;
	private Stack<ArrayList<AbstractCommand>> stack = new Stack<ArrayList<AbstractCommand>>();
	private String _readID;
	private String _writeID;
	private String _exprID;
	private String _exprContent;
	private String _exprDecision;
	private ArrayList<AbstractCommand> listaTrue;
	private ArrayList<AbstractCommand> listaFalse;
	
	public void verificaId(String id) {
		if (!symbolTable.exists(id)) {
			throw new IsiSemanticException("Symbol " + id + " not declared");
		}
	}
	
	public void exibeComandos() {
		for (AbstractCommand c : program.getComandos()) {
			System.out.println(c);
		}
	}
	
	public void generateCode() {
		program.generateTarget();
	}
}

prog:
	'programa'
	decl
	bloco
	'fimprog'
	DOT	{
		program.setVarTable(symbolTable);
        program.setComandos(stack.pop());           	 
	};

decl: (declaravar)+;

declaravar:
	tipo 
	ID {
		_varName = _input.LT(-1).getText();
		_varValue = null;
		symbol = new IsiVariable(_varName, _tipo, _varValue);
		if (!symbolTable.exists(_varName)) {
			symbolTable.add(symbol);	
		} else {
			throw new IsiSemanticException("Symbol "+_varName+" already declared");
		}
	}
	(
		VIR
		ID {
			_varName = _input.LT(-1).getText();
			_varValue = null;
			symbol = new IsiVariable(_varName, _tipo, _varValue);
			if (!symbolTable.exists(_varName)) {
				symbolTable.add(symbol);	
			} else {
				throw new IsiSemanticException("Symbol "+_varName+" already declared");
			}
		}
	)*
	DOT;

tipo:
	'numero' { _tipo = IsiVariable.NUMBER; }
	| 'texto' { _tipo = IsiVariable.TEXT; };

bloco:
	{ 
		curThread = new ArrayList<AbstractCommand>(); 
	    stack.push(curThread);  
	}
	(cmd)+;

cmd: cmdleitura | cmdescrita | cmdattrib | cmdselecao | cmdenquanto;

cmdleitura:
	'leia'
	AP
	ID { 
		verificaId(_input.LT(-1).getText());
		_readID = _input.LT(-1).getText();
	}
	FP
	DOT {
		IsiVariable var = (IsiVariable)symbolTable.get(_readID);
		CommandLeitura cmd = new CommandLeitura(_readID, var);
		stack.peek().add(cmd);
	};

cmdescrita:
	'escreva'
	AP { _exprContent = ""; }
	expr { _writeID = _input.LT(-1).getText(); }
	FP
	DOT {
    	CommandEscrita cmd = new CommandEscrita(_writeID);
        stack.peek().add(cmd);
	};

cmdattrib:
	ID {
		verificaId(_input.LT(-1).getText());
        _exprID = _input.LT(-1).getText();
	}
	ATTR { _exprContent = ""; }
	expr
	DOT {
		CommandAtribuicao cmd = new CommandAtribuicao(_exprID, _exprContent);
		stack.peek().add(cmd);
	};

cmdselecao:
	'se'
	AP { _exprContent = ""; }
	expr
	OPREL { _exprContent += _input.LT(-1).getText(); }
	expr
	FP { _exprDecision = _exprContent; }
	'entao'
	ACH {
		curThread = new ArrayList<AbstractCommand>(); 
		stack.push(curThread);
	}
	(cmd)+
	FCH { listaTrue = stack.pop(); }
	(
		'senao'
		ACH {
			curThread = new ArrayList<AbstractCommand>();
			stack.push(curThread);
		}
		(cmd+)
		FCH {
			listaFalse = stack.pop();
			CommandDecisao cmd = new CommandDecisao(_exprDecision, listaTrue, listaFalse);
			stack.peek().add(cmd);
		}
	)?;

cmdenquanto:
	'enquanto' { _exprContent = ""; }
	AP
	expr
	OPREL { _exprContent += _input.LT(-1).getText(); }
	expr
	FP { _exprDecision = _exprContent; }
	ACH {
		curThread = new ArrayList<AbstractCommand>(); 
		stack.push(curThread);
	}
	(cmd)+
	FCH {
		ArrayList<AbstractCommand> comandos = stack.pop();
		CommandEnquanto cmdEnquanto = new CommandEnquanto(_exprDecision, comandos);
        stack.peek().add(cmdEnquanto);
	}
	;

expr:
	termo (OP { _exprContent += _input.LT(-1).getText();} termo)*;

termo:
	ID { 
		verificaId(_input.LT(-1).getText());
		_exprContent += _input.LT(-1).getText();
	}
	| NUMBER { _exprContent += _input.LT(-1).getText(); }
	| TEXT { _exprContent += _input.LT(-1).getText(); };

AP: '(';

FP: ')';

DOT: '.';

OP: '+' | '-' | '*' | '/';

ATTR: ':=';

VIR: ',';

ACH: '{';

FCH: '}';

OPREL: '>' | '<' | '>=' | '<=' | '==' | '!=';

ID: [a-z] ([a-z] | [A-Z] | [0-9])*;

NUMBER: [0-9]+ ('.' [0-9]+)?;

TEXT: '"' (~["\r\n])* '"';

WS: (' ' | '\t' | '\n' | '\r') -> skip;