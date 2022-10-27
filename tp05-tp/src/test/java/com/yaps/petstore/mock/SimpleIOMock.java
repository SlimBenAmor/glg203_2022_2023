package com.yaps.petstore.mock;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.yaps.common.ui.SimpleIOInterface;

/**
 * Simulacre d'entrées/sorties pour le test, non utilisé actuellement.
 */
public class SimpleIOMock implements SimpleIOInterface {
    private List<String> output = new ArrayList<>();
    private Deque<String> input = new ArrayDeque<>();

    public void addToInput(String text) {
        input.add(text);
    }

    @Override
    public void printf(String format, Object... args) {        
       output.add(format.formatted(args));
    }

    @Override
    public void flush() {
        // DO NOTHING
    }

    @Override
    public String readLine() {
       String nextInput = input.pollFirst();
       return nextInput;
    }

    @Override
    public void showMessage(String message) {
        output.add(message);
    }

    @Override
    public String askForString(String message) {
        output.add(message);
        return readLine();
    }

    @Override
    public void waitEnter() {
        // DO NOTHING...
        
    }

    @Override
    public void println(String welcomeText) {
        showMessage(welcomeText+ "\n");
    }

    @Override
    public void println() {
        showMessage("\n");
    }

    @Override
    public int askForInt(String message, int min, int max) {
        String value = askForString(message);
        return Integer.parseInt(value);
    }

    @Override
    public double askForPositiveDouble(String message) {
        String value = askForString(message);
        return Double.parseDouble(value);
    }
}
