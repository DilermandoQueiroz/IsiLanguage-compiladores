package br.com.professorisidro.isilanguage.datastructures;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IsiConstants {
    public static final Map<Integer, List<String>> OP_PER_TYPE = new HashMap<Integer,List<String>>();
    public static final Map<Integer, String> NAME_PER_TYPE = new HashMap<Integer,String>();

    static {
        OP_PER_TYPE.put(IsiVariable.NUMBER, Arrays.asList("+", "-", "/", "*"));
        OP_PER_TYPE.put(IsiVariable.TEXT, Arrays.asList("+"));

        NAME_PER_TYPE.put(IsiVariable.NUMBER, "numero");
        NAME_PER_TYPE.put(IsiVariable.TEXT, "texto");
    }
}
