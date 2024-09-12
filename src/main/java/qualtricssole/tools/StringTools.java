/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qualtricssole.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Luis D
 */
public class StringTools {

    public static int getNumberOfRepeatedWords(String filter, String fullText) {
        Pattern pattern = Pattern.compile(filter);
        Matcher matcher = pattern.matcher(fullText);
        long counts = matcher.results().count();;
        String temp = fullText;

        return (int) counts;
    }

    public static String getFirstName(String fullName) {
        String[] tempoParts = fullName.split(" ");
        String firstname = "";
        String lastname = "";

        for (int x = 0; x < tempoParts.length; x++) {
            if (tempoParts.length == 2) {
                if (x == 0) {
                    firstname = tempoParts[x];
                } else {
                    lastname = tempoParts[x];
                }
            } else if (tempoParts.length == 3) {
                if (x == 0) {
                    firstname += tempoParts[x];
                } else if (x == 1) {
                    lastname += tempoParts[x];
                } else {
                    lastname += " " + tempoParts[x];
                }
            } else {
                if (x == 0) {
                    firstname += tempoParts[x];
                } else if (x == 1) {
                    firstname += " " + tempoParts[x];
                } else if (x == 2) {
                    lastname += tempoParts[x];
                } else {
                    lastname += " " + tempoParts[x];
                }
            }
        }

        return firstname;
    }

    public static String getLastName(String fullName) {
        String[] tempoParts = fullName.split(" ");
        String firstname = "";
        String lastname = "";

        for (int x = 0; x < tempoParts.length; x++) {
            if (tempoParts.length == 2) {
                if (x == 0) {
                    firstname = tempoParts[x];
                } else {
                    lastname = tempoParts[x];
                }
            } else if (tempoParts.length == 3) {
                if (x == 0) {
                    firstname += tempoParts[x];
                } else if (x == 1) {
                    lastname += tempoParts[x];
                } else {
                    lastname += " " + tempoParts[x];
                }
            } else {
                if (x == 0) {
                    firstname += tempoParts[x];
                } else if (x == 1) {
                    firstname += " " + tempoParts[x];
                } else if (x == 2) {
                    lastname += tempoParts[x];
                } else {
                    lastname += " " + tempoParts[x];
                }
            }
        }

        return lastname;
    }
}
