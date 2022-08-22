package br.com.professorisidro.isilanguage.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "isicompiler", subcommands = { CommandLine.HelpCommand.class,
                CompileCommand.class, ServerCommand.class }, description = "CLI for IsiLanguage compiler")
public class IsiCompiler {
}
