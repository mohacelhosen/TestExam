package com.mcu.examportal;

import java.util.Random;

public class SampleTest {
    public static void main(String[] args) {
        System.out.println(generatePassword(8).toString());
    }
    static char[] generatePassword(int len) {
        String capitalLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String smallLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialCharacters = "!@#$%^&*_=+-/.?<>)";

        String values = capitalLetters + smallLetters + numbers + specialCharacters;

        Random random = new Random();

        char[] password = new char[len];

        for (int i = 0; i < len; i++) {
            password[i] = values.charAt(random.nextInt(values.length()));
        }

        // Ensure the password contains at least one uppercase letter, one lowercase letter, one digit, and one special character
        password[0] = capitalLetters.charAt(random.nextInt(capitalLetters.length()));
        password[1] = smallLetters.charAt(random.nextInt(smallLetters.length()));
        password[2] = numbers.charAt(random.nextInt(numbers.length()));
        password[3] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));

        return password;
    }
}
