package de.cofinpro.metro.controller;

import de.cofinpro.metro.controller.command.*;
import de.cofinpro.metro.io.MetroPrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * CommandLineInterpreter class used in the MetroController, that parses a user entered command line and creates
 * a LineCommand form the parse result.
 * It represents the Client in the Command-pattern as it creates the ConcreteCommands and sets their receiver,
 * the MetroPrinter. The command is then returned to the calling MetroController - the Invoker in the pattern setting.
 */
public class CommandLineInterpreter {

    private final Scanner scanner;
    private MetroPrinter printer;

    public CommandLineInterpreter(Scanner scanner) {
        this.scanner = scanner;
    }

    public void setPrinter(MetroPrinter printer) {
        this.printer = printer;
    }

    /**
     * core method of the CommandLineInterpreter that parses the next user input line by use of the given scanner (constructor).
     * It recognizes the defined commands and - if the argument number for the command matches - it creates and
     * returns a new Command. In ay other case an InvalidCommand is created and returned.
     * @return the created command found as parse result.
     */
    public LineCommand parseNext() {
        List<String> tokens = tokenize(scanner.nextLine());
        if (tokens.isEmpty()) {
            return new InvalidCommand(printer);
        }
        return switch (tokens.get(0)) {
            case "/append", "add" -> tokens.size() == 3 ? new InsertCommand(CommandType.APPEND, printer, tokens.get(1), tokens.get(2))
                    : new InvalidCommand(printer);
            case "/add-head" -> tokens.size() == 3 ? new InsertCommand(CommandType.ADD_HEAD, printer, tokens.get(1), tokens.get(2))
                    : new InvalidCommand(printer);
            case "/connect" -> tokens.size() == 5
                    ? new ConnectCommand(printer, tokens.get(1), tokens.get(2), tokens.get(3), tokens.get(4))
                    : new InvalidCommand(printer);
            case "/route" -> tokens.size() == 5
                    ? new RouteCommand(printer, tokens.get(1), tokens.get(2), tokens.get(3), tokens.get(4))
                    : new InvalidCommand(printer);
            case "/remove" -> tokens.size() == 3 ? new RemoveCommand(printer, tokens.get(1), tokens.get(2))
                    : new InvalidCommand(printer);
            case "/output" -> tokens.size() == 2 ? new OutputCommand(printer, tokens.get(1)) : new InvalidCommand(printer);
            case "/exit" -> tokens.size() == 1 ? new ExitCommand() : new InvalidCommand(printer);
            default -> new InvalidCommand(printer);
        };
    }

    /**
     * tokenizer method, that tokenizes a command line given in 2 steps:
     * 1. it is quote-tokenized to prevent spaces in string from separating tokens
     * 2. the not-quote enclosed parts (the even tokens) are whitespace-tokenized.
     * @param commandLine the user entered command line
     * @return a list of all tokens.
     */
    List<String> tokenize(String commandLine) {
        String[] quoteTokens = commandLine.split("\"");
        List<String> tokens = new ArrayList<>();
        for (int i = 0; i < quoteTokens.length; i++) {
            if (i % 2 == 0 && !quoteTokens[i].isBlank()) {
                tokens.addAll(List.of(quoteTokens[i].trim().split("\\s+")));
            }
            if (i % 2 == 1) {
                tokens.add(quoteTokens[i]);
            }
        }
        return tokens;
    }
}
