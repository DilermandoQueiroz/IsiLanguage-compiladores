package br.com.professorisidro.isilanguage.helper;

import java.util.ArrayList;

public class IsiWarning {
	public static void printWarning(ArrayList<String> warningList) {
		if(warningList.size() > 0) {
        	System.out.println("***WARNING***");
        	for(String warning : warningList) {
        		System.out.println(warning);
        	}
        	System.out.println("******");
        }
	}
}
