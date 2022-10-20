package com.yaps.petstore.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Simplified I/O interface.
 */

public class SimpleIO {
    private BufferedReader in;

    public SimpleIO() {
        this.in = new BufferedReader(new InputStreamReader(System.in));
    }

    public void printf(String format, Object... args) {
        System.out.printf(format, args);
    }

    public void flush() {
        System.out.flush();
    }

    public String readLine() {
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
