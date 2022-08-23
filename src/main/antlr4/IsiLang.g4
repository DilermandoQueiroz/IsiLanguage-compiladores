grammar IsiLang;

@header {
	import br.com.professorisidro.isilanguage.datastructures.IsiSymbol;
	import br.com.professorisidro.isilanguage.datastructures.IsiVariable;
	import br.com.professorisidro.isilanguage.datastructures.IsiSymbolTable;
	import br.com.professorisidro.isilanguage.datastructures.IsiConstants;
	import br.com.professorisidro.isilanguage.exceptions.IsiSemanticException;
	import br.com.professorisidro.isilanguage.ast.IsiProgram;
	import br.com.professorisidro.isilanguage.ast.AbstractCommand;
	import br.com.professorisidro.isilanguage.ast.CommandLeitura;
	import br.com.professorisidro.isilanguage.ast.CommandEscrita;
	import br.com.professorisidro.isilanguage.ast.CommandAtribuicao;
	import br.com.professorisidro.isilanguage.ast.CommandDecisao;
	import br.com.professorisidro.isilanguage.ast.CommandEnquanto;
	import br.com.professorisidro.isilanguage.helper.IsiWarning;
	import java.util.ArrayList;
	import java.util.Stack;
}

@members {
	private int _tipo;
	private String _varName;
	private String _varValue;
	private String _operator;
	private IsiSymbolTable symbolTable = new IsiSymbolTable();
	private IsiSymbol symbol;
	private Stack<ArrayList<Integer>> _types = new Stack<ArrayList<Integer>>();

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

		if (!_types.isEmpty()) {
			IsiVariable variable = (IsiVariable) symbolTable.get(id);
			_types.peek().add(variable.getType());
		}
	}

	public void verifyTypes(String msg) {
		boolean valid = true;
		ArrayList<Integer> types = _types.pop();
		int expectedType = types.get(0);
		for (int type : types) {
			valid &= type == expectedType;
		}

		if (!valid) {
			throw new IsiSemanticException("Unmatching types at " + msg);
		}

		if (!_types.isEmpty()) {
			_types.peek().add(expectedType);
		}
	}

	public void verifyOperator() {
		ArrayList<Integer> types = _types.peek();
		int len = types.size();
		if (len < 2) return;

		int type1 = types.get(len - 1);
		int type2 = types.get(len - 2);

		if (type1 != type2) return;

		if (!IsiConstants.OP_PER_TYPE.get(type1).contains(_operator)) {
			String t = IsiConstants.NAME_PER_TYPE.get(type1);
			String msg = String.format("Invalid operator '%s' for type '%s' in expression %s",
				_operator, t, _exprContent);
			throw new IsiSemanticException(msg);
		}
	}

	public void addVariable(String variable) {
		_varName = variable;
		_varValue = null;
		if (!symbolTable.exists(_varName)) {
			symbol = new IsiVariable(_varName, _tipo, _varValue);
			symbolTable.add(symbol);
		} else {
			throw new IsiSemanticException("Symbol "+_varName+" already declared");
		}
	}
	
	public void exibeComandos() {
		for (AbstractCommand c : program.getComandos()) {
			System.out.println(c);
		}
	}

	public ArrayList<String> warnings() {
		ArrayList<String> warningList = new ArrayList<String>();
		for (IsiSymbol symbol : symbolTable.notUsedSymbols()) {
			warningList.add ("Variable " + symbol.getName() + " was declared and is not being used");
		}
		return warningList;
	}

	public ArrayList<AbstractCommand> getComandos() {
		return program.getComandos();
	}

	public IsiSymbolTable getSymbolTable() {
		return symbolTable;
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
	ID { addVariable(_input.LT(-1).getText()); }
	(
		VIR
		ID { addVariable(_input.LT(-1).getText()); }
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
	expr
	FP
	DOT {
    	CommandEscrita cmd = new CommandEscrita(_exprContent);
        stack.peek().add(cmd);
	};

cmdattrib:
	ID {
		_types.push(new ArrayList<Integer>());
		verificaId(_input.LT(-1).getText());
        _exprID = _input.LT(-1).getText();
	}
	ATTR { _exprContent = ""; }
	expr
	DOT {
		verifyTypes("assignment of " + _exprID + " := " + _exprContent);

		CommandAtribuicao cmd = new CommandAtribuicao(_exprID, _exprContent);
		stack.peek().add(cmd);
	};

cmdselecao:
	'se'
	AP { 
		_exprContent = ""; 
		_types.push(new ArrayList<Integer>());
	}
	expr
	OPREL { _exprContent += _input.LT(-1).getText(); }
	expr
	FP {
		_exprDecision = _exprContent;
		verifyTypes("expression " + _exprDecision);
	}
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
	'enquanto'
	AP { 
		_exprContent = ""; 
		_types.push(new ArrayList<Integer>());
	}
	expr
	OPREL { _exprContent += _input.LT(-1).getText(); }
	expr
	FP {
		_exprDecision = _exprContent;
		verifyTypes("expression " + _exprDecision);
	}
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
	{ _types.push(new ArrayList<Integer>()); }
	termo
	(
		OP {
			_operator = _input.LT(-1).getText();
			_exprContent += _operator;
		} 
		termo
		{ verifyOperator(); }
	)*
	{ verifyTypes("expression " + _exprContent); };

termo:
	ID {
		verificaId(_input.LT(-1).getText());
		_exprContent += _input.LT(-1).getText();
	}
	| NUMBER {
		_exprContent += _input.LT(-1).getText();
		_types.peek().add(IsiVariable.NUMBER);
	}
	| TEXT {
		_exprContent += _input.LT(-1).getText();
		_types.peek().add(IsiVariable.TEXT);
	};

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