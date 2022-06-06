package de.cofinpro.metro.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;

import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

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
}