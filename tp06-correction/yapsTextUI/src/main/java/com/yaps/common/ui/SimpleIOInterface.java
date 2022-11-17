package com.yaps.common.ui;

public interface SimpleIOInterface {

	void printf(String format, Object... args);

	void flush();

	String readLine();

	/**
	 * Display a message on a line
	 * 
	 * @param message
	 */
	void showMessage(String message);

	/**
	 * Asks for a given answer (which can be empty)
	 * 
	 * @param message : message to display
	 * @return
	 */
	String askForString(String message);

	void waitEnter();

	void println(String welcomeText);

	void println();

	/**
	 * Ask for an int between two values (inclusives)
	 * @param message message to display
	 * @param min minimal value
	 * @param max maximal value
	 * @return the chosen value.
	 */
	int askForInt(String message, int min, int max);

	double askForPositiveDouble(String message);

}