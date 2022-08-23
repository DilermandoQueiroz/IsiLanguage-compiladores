package br.com.professorisidro.isilanguage.datastructures;

public abstract class IsiSymbol {

	protected String name;
	protected boolean isUsed;

	public abstract String generateCode(String language);

	public IsiSymbol(String name) {
		this.name = name;
		this.isUsed = false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getIsUsed() {
		return isUsed;
	}
	
	public void setIsUsed(boolean used) {
		this.isUsed = used;
	}

	@Override
	public String toString() {
		return "IsiSymbol [name=" + name + "]";
	}

}
