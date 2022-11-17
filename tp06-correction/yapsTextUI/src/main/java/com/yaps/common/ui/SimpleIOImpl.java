package com.yaps.common.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Simplified I/O interface.
 */

public class SimpleIOImpl implements SimpleIOInterface {
    private BufferedReader in;

    public SimpleIOImpl() {
        this.in = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
	public void printf(String format, Object... args) {
        System.out.printf(format, args);
    }

    @Override
	public void flush() {
        System.out.flush();
    }

    @Override
	public String readLine() {
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Display a message on a line
     * 
     * @param message
     */
    @Override
	public void showMessage(String message) {
        printf("%s\n", message);
    }

    /**
     * Asks for a given answer (which can be empty)
     * 
     * @param message : message to display
     * @return
     */
    @Override
	public String askForString(String message) {
        printf(message+ ": ");
        flush();
        return readLine().trim();
    }

    @Override
	public void waitEnter() {
        showMessage("\n\tPress enter to continue...");
        readLine();
    }

    @Override
	public void println(String welcomeText) {
        System.out.println(welcomeText);
    }

    @Override
	public void println() {
        System.out.println();
    }

    /**
     * Ask for an int between two values (inclusives)
     * @param message message to display
     * @param min minimal value
     * @param max maximal value
     * @return the chosen value.
     */
    @Override
	public int askForInt(String message, int min, int max) {
        boolean answerNotFound = true;
        // dummy init
        // (java can't figure out that if answerNotFound is false, answer has a value)
        int answer=0;
        do {
            println(message);
            printf("please enter a value between %s and %s: ", min, max);
            String answerString = readLine();
            try {
                answer = Integer.parseInt(answerString);
                if (min <= answer && answer <= max) {
                    answerNotFound = false;
                }
            } catch (NumberFormatException e) {
                println("Please enter an integer");
            }           
        } while (answerNotFound);
        return answer;
    }

    @Override
	public double askForPositiveDouble(String message) {
        Double result = null;// dirty trick...
        do {
            final String errorMessage = "please enter a positive number";
            String string = askForString(message);
            try {
                result = Double.valueOf(string);
                if (result < 0.0) {
                    println(errorMessage);
                }
            } catch (NumberFormatException exception) {
                println(errorMessage);
            }            
        } while (result == null || result < 0.0);
        return result;
    }

}
