package pt.isel.utils;

import java.util.Scanner;

/**
 * Some utilities for console input and output.
 */
public class ConsoleUI {

    /**
     * Requests the user to insert an Integer value and returns it.
     *
     * @param message message to display
     * @return user input
     */
    public static Integer requestInteger(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);
        return scanner.nextInt();
    }

    /**
     * Requests the user to insert a Double value and returns it.
     *
     * @param message message to display
     * @return user input
     */
    public static Double requestDouble(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);
        return scanner.nextDouble();
    }

    /**
     * Requests the user to insert a String value and returns it.
     *
     * @param message message to display
     * @return user input
     */
    public static String requestString(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);
        return scanner.nextLine();
    }

    /**
     * Requests the user to insert a Boolean value and returns it.
     *
     * @param message message to display
     * @return user input
     */
    public static Boolean requestBoolean(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);
        return scanner.nextBoolean();
    }
}
