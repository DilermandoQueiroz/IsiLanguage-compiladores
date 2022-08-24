package br.com.professorisidro.isilanguage.datastructures;

public class IsiVariable extends IsiSymbol {

	public static final int NUMBER = 0;
	public static final int TEXT = 1;
	public static final int BOOLEAN = 2;

	private int type;
	private Object value;

	public IsiVariable(String name, int type, Object value) {
		super(name);
		this.type = type;
		this.value = value;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "IsiVariable [name=" + name + ", type=" + type + ", value=" + value + "]";
	}

	public String generateCode(String language) {
		if (language == "java") {
			String str;
			if (type == NUMBER) {
				str = "double ";
			} else if (type == TEXT) {
				str = "String ";
			} else {
				str = "boolean ";
			}
			return str + " " + super.name + ";";
		}
		else if (language == "c") {
			String str;
			if (type == NUMBER) {
				str = "double ";
				return str + " " + super.name + ";";
			} else if (type == TEXT) {
				str = "char[] ";
				return "char " + super.name + "[99999999];";
			} else {
				str = "bool ";
				return str + " " + super.name + ";";
			}
		}
		return "";
	}

}
