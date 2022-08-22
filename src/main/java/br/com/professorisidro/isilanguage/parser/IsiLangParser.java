// Generated from IsiLang.g4 by ANTLR 4.7.1
package br.com.professorisidro.isilanguage.parser;

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

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class IsiLangParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, AP=11, FP=12, DOT=13, OP=14, ATTR=15, VIR=16, ACH=17, FCH=18, 
		OPREL=19, ID=20, NUMBER=21, TEXT=22, WS=23;
	public static final int
		RULE_prog = 0, RULE_decl = 1, RULE_declaravar = 2, RULE_tipo = 3, RULE_bloco = 4, 
		RULE_cmd = 5, RULE_cmdleitura = 6, RULE_cmdescrita = 7, RULE_cmdattrib = 8, 
		RULE_cmdselecao = 9, RULE_cmdenquanto = 10, RULE_expr = 11, RULE_termo = 12;
	public static final String[] ruleNames = {
		"prog", "decl", "declaravar", "tipo", "bloco", "cmd", "cmdleitura", "cmdescrita", 
		"cmdattrib", "cmdselecao", "cmdenquanto", "expr", "termo"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'programa'", "'fimprog'", "'numero'", "'texto'", "'leia'", "'escreva'", 
		"'se'", "'entao'", "'senao'", "'enquanto'", "'('", "')'", "'.'", null, 
		"':='", "','", "'{'", "'}'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, "AP", 
		"FP", "DOT", "OP", "ATTR", "VIR", "ACH", "FCH", "OPREL", "ID", "NUMBER", 
		"TEXT", "WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "IsiLang.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }


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
				warningList.add ("A variavel " + symbol.getName() + " foi declarada e não utilizada.");
			}
			return warningList;
		}
		
		public void generateCode() {
			program.generateTarget();
		}

	public IsiLangParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgContext extends ParserRuleContext {
		public DeclContext decl() {
			return getRuleContext(DeclContext.class,0);
		}
		public BlocoContext bloco() {
			return getRuleContext(BlocoContext.class,0);
		}
		public TerminalNode DOT() { return getToken(IsiLangParser.DOT, 0); }
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterProg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitProg(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(26);
			match(T__0);
			setState(27);
			decl();
			setState(28);
			bloco();
			setState(29);
			match(T__1);
			setState(30);
			match(DOT);

					program.setVarTable(symbolTable);
			        program.setComandos(stack.pop());           	 
				
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclContext extends ParserRuleContext {
		public List<DeclaravarContext> declaravar() {
			return getRuleContexts(DeclaravarContext.class);
		}
		public DeclaravarContext declaravar(int i) {
			return getRuleContext(DeclaravarContext.class,i);
		}
		public DeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitDecl(this);
		}
	}

	public final DeclContext decl() throws RecognitionException {
		DeclContext _localctx = new DeclContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_decl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(34); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(33);
				declaravar();
				}
				}
				setState(36); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__2 || _la==T__3 );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclaravarContext extends ParserRuleContext {
		public TipoContext tipo() {
			return getRuleContext(TipoContext.class,0);
		}
		public List<TerminalNode> ID() { return getTokens(IsiLangParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(IsiLangParser.ID, i);
		}
		public TerminalNode DOT() { return getToken(IsiLangParser.DOT, 0); }
		public List<TerminalNode> VIR() { return getTokens(IsiLangParser.VIR); }
		public TerminalNode VIR(int i) {
			return getToken(IsiLangParser.VIR, i);
		}
		public DeclaravarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaravar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterDeclaravar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitDeclaravar(this);
		}
	}

	public final DeclaravarContext declaravar() throws RecognitionException {
		DeclaravarContext _localctx = new DeclaravarContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_declaravar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(38);
			tipo();
			setState(39);
			match(ID);
			 addVariable(_input.LT(-1).getText()); 
			setState(46);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==VIR) {
				{
				{
				setState(41);
				match(VIR);
				setState(42);
				match(ID);
				 addVariable(_input.LT(-1).getText()); 
				}
				}
				setState(48);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(49);
			match(DOT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TipoContext extends ParserRuleContext {
		public TipoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tipo; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterTipo(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitTipo(this);
		}
	}

	public final TipoContext tipo() throws RecognitionException {
		TipoContext _localctx = new TipoContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_tipo);
		try {
			setState(55);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__2:
				enterOuterAlt(_localctx, 1);
				{
				setState(51);
				match(T__2);
				 _tipo = IsiVariable.NUMBER; 
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 2);
				{
				setState(53);
				match(T__3);
				 _tipo = IsiVariable.TEXT; 
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlocoContext extends ParserRuleContext {
		public List<CmdContext> cmd() {
			return getRuleContexts(CmdContext.class);
		}
		public CmdContext cmd(int i) {
			return getRuleContext(CmdContext.class,i);
		}
		public BlocoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bloco; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterBloco(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitBloco(this);
		}
	}

	public final BlocoContext bloco() throws RecognitionException {
		BlocoContext _localctx = new BlocoContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_bloco);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			 
					curThread = new ArrayList<AbstractCommand>(); 
				    stack.push(curThread);  
				
			setState(59); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(58);
				cmd();
				}
				}
				setState(61); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__9) | (1L << ID))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CmdContext extends ParserRuleContext {
		public CmdleituraContext cmdleitura() {
			return getRuleContext(CmdleituraContext.class,0);
		}
		public CmdescritaContext cmdescrita() {
			return getRuleContext(CmdescritaContext.class,0);
		}
		public CmdattribContext cmdattrib() {
			return getRuleContext(CmdattribContext.class,0);
		}
		public CmdselecaoContext cmdselecao() {
			return getRuleContext(CmdselecaoContext.class,0);
		}
		public CmdenquantoContext cmdenquanto() {
			return getRuleContext(CmdenquantoContext.class,0);
		}
		public CmdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmd; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterCmd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitCmd(this);
		}
	}

	public final CmdContext cmd() throws RecognitionException {
		CmdContext _localctx = new CmdContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_cmd);
		try {
			setState(68);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__4:
				enterOuterAlt(_localctx, 1);
				{
				setState(63);
				cmdleitura();
				}
				break;
			case T__5:
				enterOuterAlt(_localctx, 2);
				{
				setState(64);
				cmdescrita();
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 3);
				{
				setState(65);
				cmdattrib();
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 4);
				{
				setState(66);
				cmdselecao();
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 5);
				{
				setState(67);
				cmdenquanto();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CmdleituraContext extends ParserRuleContext {
		public TerminalNode AP() { return getToken(IsiLangParser.AP, 0); }
		public TerminalNode ID() { return getToken(IsiLangParser.ID, 0); }
		public TerminalNode FP() { return getToken(IsiLangParser.FP, 0); }
		public TerminalNode DOT() { return getToken(IsiLangParser.DOT, 0); }
		public CmdleituraContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmdleitura; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterCmdleitura(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitCmdleitura(this);
		}
	}

	public final CmdleituraContext cmdleitura() throws RecognitionException {
		CmdleituraContext _localctx = new CmdleituraContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_cmdleitura);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(70);
			match(T__4);
			setState(71);
			match(AP);
			setState(72);
			match(ID);
			 
					verificaId(_input.LT(-1).getText());
					_readID = _input.LT(-1).getText();
				
			setState(74);
			match(FP);
			setState(75);
			match(DOT);

					IsiVariable var = (IsiVariable)symbolTable.get(_readID);
					CommandLeitura cmd = new CommandLeitura(_readID, var);
					stack.peek().add(cmd);
				
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CmdescritaContext extends ParserRuleContext {
		public TerminalNode AP() { return getToken(IsiLangParser.AP, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode FP() { return getToken(IsiLangParser.FP, 0); }
		public TerminalNode DOT() { return getToken(IsiLangParser.DOT, 0); }
		public CmdescritaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmdescrita; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterCmdescrita(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitCmdescrita(this);
		}
	}

	public final CmdescritaContext cmdescrita() throws RecognitionException {
		CmdescritaContext _localctx = new CmdescritaContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_cmdescrita);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(78);
			match(T__5);
			setState(79);
			match(AP);
			 _exprContent = ""; 
			setState(81);
			expr();
			setState(82);
			match(FP);
			setState(83);
			match(DOT);

			    	CommandEscrita cmd = new CommandEscrita(_exprContent);
			        stack.peek().add(cmd);
				
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CmdattribContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IsiLangParser.ID, 0); }
		public TerminalNode ATTR() { return getToken(IsiLangParser.ATTR, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode DOT() { return getToken(IsiLangParser.DOT, 0); }
		public CmdattribContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmdattrib; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterCmdattrib(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitCmdattrib(this);
		}
	}

	public final CmdattribContext cmdattrib() throws RecognitionException {
		CmdattribContext _localctx = new CmdattribContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_cmdattrib);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(86);
			match(ID);

					_types.push(new ArrayList<Integer>());
					verificaId(_input.LT(-1).getText());
			        _exprID = _input.LT(-1).getText();
				
			setState(88);
			match(ATTR);
			 _exprContent = ""; 
			setState(90);
			expr();
			setState(91);
			match(DOT);

					verifyTypes("assignment of " + _exprID + " := " + _exprContent);

					CommandAtribuicao cmd = new CommandAtribuicao(_exprID, _exprContent);
					stack.peek().add(cmd);
				
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CmdselecaoContext extends ParserRuleContext {
		public TerminalNode AP() { return getToken(IsiLangParser.AP, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode OPREL() { return getToken(IsiLangParser.OPREL, 0); }
		public TerminalNode FP() { return getToken(IsiLangParser.FP, 0); }
		public List<TerminalNode> ACH() { return getTokens(IsiLangParser.ACH); }
		public TerminalNode ACH(int i) {
			return getToken(IsiLangParser.ACH, i);
		}
		public List<TerminalNode> FCH() { return getTokens(IsiLangParser.FCH); }
		public TerminalNode FCH(int i) {
			return getToken(IsiLangParser.FCH, i);
		}
		public List<CmdContext> cmd() {
			return getRuleContexts(CmdContext.class);
		}
		public CmdContext cmd(int i) {
			return getRuleContext(CmdContext.class,i);
		}
		public CmdselecaoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmdselecao; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterCmdselecao(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitCmdselecao(this);
		}
	}

	public final CmdselecaoContext cmdselecao() throws RecognitionException {
		CmdselecaoContext _localctx = new CmdselecaoContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_cmdselecao);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(94);
			match(T__6);
			setState(95);
			match(AP);
			 
					_exprContent = ""; 
					_types.push(new ArrayList<Integer>());
				
			setState(97);
			expr();
			setState(98);
			match(OPREL);
			 _exprContent += _input.LT(-1).getText(); 
			setState(100);
			expr();
			setState(101);
			match(FP);

					_exprDecision = _exprContent;
					verifyTypes("expression " + _exprDecision);
				
			setState(103);
			match(T__7);
			setState(104);
			match(ACH);

					curThread = new ArrayList<AbstractCommand>(); 
					stack.push(curThread);
				
			setState(107); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(106);
				cmd();
				}
				}
				setState(109); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__9) | (1L << ID))) != 0) );
			setState(111);
			match(FCH);
			 listaTrue = stack.pop(); 
			setState(124);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(113);
				match(T__8);
				setState(114);
				match(ACH);

							curThread = new ArrayList<AbstractCommand>();
							stack.push(curThread);
						
				{
				setState(117); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(116);
					cmd();
					}
					}
					setState(119); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__9) | (1L << ID))) != 0) );
				}
				setState(121);
				match(FCH);

							listaFalse = stack.pop();
							CommandDecisao cmd = new CommandDecisao(_exprDecision, listaTrue, listaFalse);
							stack.peek().add(cmd);
						
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CmdenquantoContext extends ParserRuleContext {
		public TerminalNode AP() { return getToken(IsiLangParser.AP, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode OPREL() { return getToken(IsiLangParser.OPREL, 0); }
		public TerminalNode FP() { return getToken(IsiLangParser.FP, 0); }
		public TerminalNode ACH() { return getToken(IsiLangParser.ACH, 0); }
		public TerminalNode FCH() { return getToken(IsiLangParser.FCH, 0); }
		public List<CmdContext> cmd() {
			return getRuleContexts(CmdContext.class);
		}
		public CmdContext cmd(int i) {
			return getRuleContext(CmdContext.class,i);
		}
		public CmdenquantoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmdenquanto; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterCmdenquanto(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitCmdenquanto(this);
		}
	}

	public final CmdenquantoContext cmdenquanto() throws RecognitionException {
		CmdenquantoContext _localctx = new CmdenquantoContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_cmdenquanto);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(126);
			match(T__9);
			setState(127);
			match(AP);
			 
					_exprContent = ""; 
					_types.push(new ArrayList<Integer>());
				
			setState(129);
			expr();
			setState(130);
			match(OPREL);
			 _exprContent += _input.LT(-1).getText(); 
			setState(132);
			expr();
			setState(133);
			match(FP);

					_exprDecision = _exprContent;
					verifyTypes("expression " + _exprDecision);
				
			setState(135);
			match(ACH);

					curThread = new ArrayList<AbstractCommand>(); 
					stack.push(curThread);
				
			setState(138); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(137);
				cmd();
				}
				}
				setState(140); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__9) | (1L << ID))) != 0) );
			setState(142);
			match(FCH);

					ArrayList<AbstractCommand> comandos = stack.pop();
					CommandEnquanto cmdEnquanto = new CommandEnquanto(_exprDecision, comandos);
			        stack.peek().add(cmdEnquanto);
				
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public List<TermoContext> termo() {
			return getRuleContexts(TermoContext.class);
		}
		public TermoContext termo(int i) {
			return getRuleContext(TermoContext.class,i);
		}
		public List<TerminalNode> OP() { return getTokens(IsiLangParser.OP); }
		public TerminalNode OP(int i) {
			return getToken(IsiLangParser.OP, i);
		}
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitExpr(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			 _types.push(new ArrayList<Integer>()); 
			setState(146);
			termo();
			setState(154);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==OP) {
				{
				{
				setState(147);
				match(OP);

							_operator = _input.LT(-1).getText();
							_exprContent += _operator;
						
				setState(149);
				termo();
				 verifyOperator(); 
				}
				}
				setState(156);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			 verifyTypes("expression " + _exprContent); 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TermoContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(IsiLangParser.ID, 0); }
		public TerminalNode NUMBER() { return getToken(IsiLangParser.NUMBER, 0); }
		public TerminalNode TEXT() { return getToken(IsiLangParser.TEXT, 0); }
		public TermoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_termo; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).enterTermo(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IsiLangListener ) ((IsiLangListener)listener).exitTermo(this);
		}
	}

	public final TermoContext termo() throws RecognitionException {
		TermoContext _localctx = new TermoContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_termo);
		try {
			setState(165);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(159);
				match(ID);

						verificaId(_input.LT(-1).getText());
						_exprContent += _input.LT(-1).getText();
					
				}
				break;
			case NUMBER:
				enterOuterAlt(_localctx, 2);
				{
				setState(161);
				match(NUMBER);

						_exprContent += _input.LT(-1).getText();
						_types.peek().add(IsiVariable.NUMBER);
					
				}
				break;
			case TEXT:
				enterOuterAlt(_localctx, 3);
				{
				setState(163);
				match(TEXT);

						_exprContent += _input.LT(-1).getText();
						_types.peek().add(IsiVariable.TEXT);
					
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\31\u00aa\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\6\3%\n"+
		"\3\r\3\16\3&\3\4\3\4\3\4\3\4\3\4\3\4\7\4/\n\4\f\4\16\4\62\13\4\3\4\3\4"+
		"\3\5\3\5\3\5\3\5\5\5:\n\5\3\6\3\6\6\6>\n\6\r\6\16\6?\3\7\3\7\3\7\3\7\3"+
		"\7\5\7G\n\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3"+
		"\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3"+
		"\13\3\13\3\13\3\13\3\13\3\13\3\13\6\13n\n\13\r\13\16\13o\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\6\13x\n\13\r\13\16\13y\3\13\3\13\3\13\5\13\177\n\13\3"+
		"\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\6\f\u008d\n\f\r\f\16\f"+
		"\u008e\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\7\r\u009b\n\r\f\r\16\r"+
		"\u009e\13\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\5\16\u00a8\n\16\3\16"+
		"\2\2\17\2\4\6\b\n\f\16\20\22\24\26\30\32\2\2\2\u00ab\2\34\3\2\2\2\4$\3"+
		"\2\2\2\6(\3\2\2\2\b9\3\2\2\2\n;\3\2\2\2\fF\3\2\2\2\16H\3\2\2\2\20P\3\2"+
		"\2\2\22X\3\2\2\2\24`\3\2\2\2\26\u0080\3\2\2\2\30\u0093\3\2\2\2\32\u00a7"+
		"\3\2\2\2\34\35\7\3\2\2\35\36\5\4\3\2\36\37\5\n\6\2\37 \7\4\2\2 !\7\17"+
		"\2\2!\"\b\2\1\2\"\3\3\2\2\2#%\5\6\4\2$#\3\2\2\2%&\3\2\2\2&$\3\2\2\2&\'"+
		"\3\2\2\2\'\5\3\2\2\2()\5\b\5\2)*\7\26\2\2*\60\b\4\1\2+,\7\22\2\2,-\7\26"+
		"\2\2-/\b\4\1\2.+\3\2\2\2/\62\3\2\2\2\60.\3\2\2\2\60\61\3\2\2\2\61\63\3"+
		"\2\2\2\62\60\3\2\2\2\63\64\7\17\2\2\64\7\3\2\2\2\65\66\7\5\2\2\66:\b\5"+
		"\1\2\678\7\6\2\28:\b\5\1\29\65\3\2\2\29\67\3\2\2\2:\t\3\2\2\2;=\b\6\1"+
		"\2<>\5\f\7\2=<\3\2\2\2>?\3\2\2\2?=\3\2\2\2?@\3\2\2\2@\13\3\2\2\2AG\5\16"+
		"\b\2BG\5\20\t\2CG\5\22\n\2DG\5\24\13\2EG\5\26\f\2FA\3\2\2\2FB\3\2\2\2"+
		"FC\3\2\2\2FD\3\2\2\2FE\3\2\2\2G\r\3\2\2\2HI\7\7\2\2IJ\7\r\2\2JK\7\26\2"+
		"\2KL\b\b\1\2LM\7\16\2\2MN\7\17\2\2NO\b\b\1\2O\17\3\2\2\2PQ\7\b\2\2QR\7"+
		"\r\2\2RS\b\t\1\2ST\5\30\r\2TU\7\16\2\2UV\7\17\2\2VW\b\t\1\2W\21\3\2\2"+
		"\2XY\7\26\2\2YZ\b\n\1\2Z[\7\21\2\2[\\\b\n\1\2\\]\5\30\r\2]^\7\17\2\2^"+
		"_\b\n\1\2_\23\3\2\2\2`a\7\t\2\2ab\7\r\2\2bc\b\13\1\2cd\5\30\r\2de\7\25"+
		"\2\2ef\b\13\1\2fg\5\30\r\2gh\7\16\2\2hi\b\13\1\2ij\7\n\2\2jk\7\23\2\2"+
		"km\b\13\1\2ln\5\f\7\2ml\3\2\2\2no\3\2\2\2om\3\2\2\2op\3\2\2\2pq\3\2\2"+
		"\2qr\7\24\2\2r~\b\13\1\2st\7\13\2\2tu\7\23\2\2uw\b\13\1\2vx\5\f\7\2wv"+
		"\3\2\2\2xy\3\2\2\2yw\3\2\2\2yz\3\2\2\2z{\3\2\2\2{|\7\24\2\2|}\b\13\1\2"+
		"}\177\3\2\2\2~s\3\2\2\2~\177\3\2\2\2\177\25\3\2\2\2\u0080\u0081\7\f\2"+
		"\2\u0081\u0082\7\r\2\2\u0082\u0083\b\f\1\2\u0083\u0084\5\30\r\2\u0084"+
		"\u0085\7\25\2\2\u0085\u0086\b\f\1\2\u0086\u0087\5\30\r\2\u0087\u0088\7"+
		"\16\2\2\u0088\u0089\b\f\1\2\u0089\u008a\7\23\2\2\u008a\u008c\b\f\1\2\u008b"+
		"\u008d\5\f\7\2\u008c\u008b\3\2\2\2\u008d\u008e\3\2\2\2\u008e\u008c\3\2"+
		"\2\2\u008e\u008f\3\2\2\2\u008f\u0090\3\2\2\2\u0090\u0091\7\24\2\2\u0091"+
		"\u0092\b\f\1\2\u0092\27\3\2\2\2\u0093\u0094\b\r\1\2\u0094\u009c\5\32\16"+
		"\2\u0095\u0096\7\20\2\2\u0096\u0097\b\r\1\2\u0097\u0098\5\32\16\2\u0098"+
		"\u0099\b\r\1\2\u0099\u009b\3\2\2\2\u009a\u0095\3\2\2\2\u009b\u009e\3\2"+
		"\2\2\u009c\u009a\3\2\2\2\u009c\u009d\3\2\2\2\u009d\u009f\3\2\2\2\u009e"+
		"\u009c\3\2\2\2\u009f\u00a0\b\r\1\2\u00a0\31\3\2\2\2\u00a1\u00a2\7\26\2"+
		"\2\u00a2\u00a8\b\16\1\2\u00a3\u00a4\7\27\2\2\u00a4\u00a8\b\16\1\2\u00a5"+
		"\u00a6\7\30\2\2\u00a6\u00a8\b\16\1\2\u00a7\u00a1\3\2\2\2\u00a7\u00a3\3"+
		"\2\2\2\u00a7\u00a5\3\2\2\2\u00a8\33\3\2\2\2\r&\609?Foy~\u008e\u009c\u00a7";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}