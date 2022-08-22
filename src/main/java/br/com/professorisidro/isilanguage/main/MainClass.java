package br.com.professorisidro.isilanguage.main;

import br.com.professorisidro.isilanguage.cli.IsiCompiler;
import picocli.CommandLine;

/* esta é a classe que é responsável por criar a interação com o usuário
 * instanciando nosso parser e apontando para o arquivo fonte
 * 
 * Arquivo fonte: extensao .isi
 * 
 */
public class MainClass {
	public static void main(String[] args) {
		int exitCode = new CommandLine(new IsiCompiler()).execute(args);
		System.exit(exitCode);
	}
}
