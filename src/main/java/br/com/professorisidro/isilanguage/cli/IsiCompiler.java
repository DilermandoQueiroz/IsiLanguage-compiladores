package br.com.professorisidro.isilanguage.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "isicompiler", subcommands = { CommandLine.HelpCommand.class,
        CompileCommand.class, ServerCommand.class,
        InterpretCommand.class }, description = "CLI for IsiLanguage compiler")
public class IsiCompiler {
}
