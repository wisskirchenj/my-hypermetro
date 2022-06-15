package de.cofinpro.metro.controller;

import de.cofinpro.metro.controller.command.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandLineInterpreterTest {

    @Mock
    Scanner scanner;

    CommandLineInterpreter commandLineInterpreter;

    @BeforeEach
    void setUp() {
        commandLineInterpreter = new CommandLineInterpreter(scanner);
    }


    @Test
    void whenExitWithBlanks_tokenizeWorks() {
        String commandLine = " /exit  ";
        List<String> tokens = commandLineInterpreter.tokenize(commandLine);
        assertEquals(1, tokens.size());
        assertEquals("/exit", tokens.get(0));
    }

    @ParameterizedTest
    @CsvSource({
            "/output m2,/output,m2",
            "/output \"m2\",/output,m2",
            "/output \"line m2\",/output,line m2"
    })
    void whenOutputWithToken_tokenizeWorks(String commandLine, String token1, String token2) {
        List<String> tokens = commandLineInterpreter.tokenize(commandLine);
        assertEquals(2, tokens.size());
        assertEquals(token1, tokens.get(0));
        assertEquals(token2, tokens.get(1));
    }
    @ParameterizedTest
    @CsvSource({
            "/append \"line m2\" station,/append,line m2,station",
            "/append \"line m2\" \"station 1\",/append,line m2,station 1",
            "/append \"line   m2\"  \"eine neue station 1\",/append,line   m2,eine neue station 1",

    })
    void whenThreeTokens_tokenizeWorks(String commandLine, String token1, String token2, String token3) {
        List<String> tokens = commandLineInterpreter.tokenize(commandLine);
        assertEquals(3, tokens.size());
        assertEquals(token1, tokens.get(0));
        assertEquals(token2, tokens.get(1));
        assertEquals(token3, tokens.get(2));
    }

    @Test
    void whenManyTokens_tokenizeWorks() {
        String commandLine = "/append \"line on m2\" s abd \"eine neue station 1\"";
        List<String> tokens = commandLineInterpreter.tokenize(commandLine);
        assertEquals(5, tokens.size());
        assertEquals("/append", tokens.get(0));
        assertEquals("line on m2", tokens.get(1));
        assertEquals("s", tokens.get(2));
        assertEquals("abd", tokens.get(3));
        assertEquals("eine neue station 1", tokens.get(4));
    }

    static Stream<Arguments> whenValidCommand_parseNextReturnsValidCommand() {
        return Stream.of(
                Arguments.of("/exit", ExitCommand.class),
                Arguments.of("/output \"line 1\"", OutputCommand.class),
                Arguments.of("/remove line1 \"station 2\"", RemoveCommand.class),
                Arguments.of("/add-head line2 station", InsertCommand.class),
                Arguments.of("/append line2 station", InsertCommand.class),
                Arguments.of("/connect line1 station line2 station", ConnectCommand.class),
                Arguments.of("/route line1 station \"line 2\" station", RouteCommand.class),
                Arguments.of("/fastest-route \"line 1\" station \"line2\" station", FastestRouteCommand.class)
        );
    }

    @ParameterizedTest
    @MethodSource()
    void whenValidCommand_parseNextReturnsValidCommand(String userEntry, Class<? extends LineCommand> commandClass) {
        when(scanner.nextLine()).thenReturn(userEntry);
        assertEquals(commandClass, commandLineInterpreter.parseNext().getClass());
    }

    @ParameterizedTest
    @ValueSource(strings = {"",
            "/wrong",
            "/output",
            "/exit now",
            "/remove \"one arg\"",
            "/append \"one arg\"",
            "/add-head \"one arg\"",
            "/add-head too many args",
            "/connect too few args",
            "/route too \"few and \" args",
            "/fastest-route too too too many args",
            "/output two args"})
    void whenInvalidCommand_parseNextReturnsInvalidCommand(String userEntry) {
        when(scanner.nextLine()).thenReturn(userEntry);
        assertTrue(commandLineInterpreter.parseNext() instanceof InvalidCommand);
    }
}