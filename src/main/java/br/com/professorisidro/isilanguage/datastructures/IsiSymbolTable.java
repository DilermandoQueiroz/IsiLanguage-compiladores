package br.com.professorisidro.isilanguage.datastructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class IsiSymbolTable {

	private HashMap<String, IsiSymbol> map;

	public IsiSymbolTable() {
		map = new HashMap<String, IsiSymbol>();

	}

	public void add(IsiSymbol symbol) {
		map.put(symbol.getName(), symbol);
	}

	public boolean exists(String symbolName) {
		return map.get(symbolName) != null;
	}

	public IsiSymbol get(String symbolName) {
		//se a variável é usada ela recebe true
		map.get(symbolName).setIsUsed(true);
		return map.get(symbolName);
	}

	public ArrayList<IsiSymbol> getAll() {
		ArrayList<IsiSymbol> lista = new ArrayList<IsiSymbol>();
		for (IsiSymbol symbol : map.values()) {
			lista.add(symbol);
		}
		return lista;
	}

	public List<IsiSymbol> notUsedSymbols() {
		ArrayList<IsiSymbol> symbols = this.getAll();
		List<IsiSymbol> notUsed = symbols.stream().filter(symbol -> !symbol.isUsed).collect(Collectors.toList());
		return notUsed;
	}

}
