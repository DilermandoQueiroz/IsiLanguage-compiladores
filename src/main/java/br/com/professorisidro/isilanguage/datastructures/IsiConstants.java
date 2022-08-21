package br.com.professorisidro.isilanguage.datastructures;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IsiConstants {
    public static final Map<Integer, List<String>> ALLOWED_OPERATORS = new HashMap<Integer,List<String>>();

    static {
        ALLOWED_OPERATORS.put(IsiVariable.NUMBER, Arrays.asList("+", "-", "/", "*"));
        ALLOWED_OPERATORS.put(IsiVariable.TEXT, Arrays.asList("+"));
    }
}
