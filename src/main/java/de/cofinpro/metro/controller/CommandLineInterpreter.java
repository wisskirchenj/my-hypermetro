package de.cofinpro.metro.controller;

import de.cofinpro.metro.controller.command.*;
import de.cofinpro.metro.io.StationsPrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandLineInterpreter {

    private final Scanner scanner;
    private StationsPrinter printer;

    public CommandLineInterpreter(Scanner scanner) {
        this.scanner = scanner;
    }

    public void setPrinter(StationsPrinter printer) {
        this.printer = printer;
    }

    public LineCommand parseNext() {
        List<String> tokens = tokenize(scanner.nextLine());
        if (tokens.isEmpty()) {
            return new InvalidCommand(printer);
        }
        return switch (tokens.get(0)) {
            case "/append" -> tokens.size() == 3 ? new InsertCommand(CommandType.APPEND, printer, tokens.get(1), tokens.get(2))
                    : new InvalidCommand(printer);
            case "/add-head" -> tokens.size() == 3 ? new InsertCommand(CommandType.ADD_HEAD, printer, tokens.get(1), tokens.get(2))
                    : new InvalidCommand(printer);
            case "/remove" -> tokens.size() == 3 ? new RemoveCommand(printer, tokens.get(1), tokens.get(2))
                    : new InvalidCommand(printer);
            case "/output" -> tokens.size() == 2 ? new OutputCommand(printer, tokens.get(1)) : new InvalidCommand(printer);
            case "/exit" -> tokens.size() == 1 ? new ExitCommand() : new InvalidCommand(printer);
            default -> new InvalidCommand(printer);
        };
    }

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
