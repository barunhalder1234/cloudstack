package com.cloud.utils;

import org.junit.Assert;
import org.junit.Test;

public class PasswordGeneratorTest {
    @Test
    public void generateRandomPassword() {
        // actual length is requested length + 3
        Assert.assertTrue(PasswordGenerator.generateRandomPassword(0).length() == 3);
        Assert.assertTrue(PasswordGenerator.generateRandomPassword(1).length() == 4);
        String password = PasswordGenerator.generateRandomPassword(0);
        // TODO: this might give more help to bruteforcing than desired
        // the actual behavior is that the first character is a random lowercase
        // char
        Assert.assertTrue(Character.isLowerCase(password.charAt(0)));
        // the second character is a random upper case char
        Assert.assertTrue(Character.isUpperCase(password.charAt(1)));
        // and the third is a digit
        Assert.assertTrue(Character.isDigit(password.charAt(2)));
    }

    @Test
    public void rot13() {
        // only letters are handled, numbers are unchanged
        Assert.assertEquals("1234", PasswordGenerator.rot13("1234"));
        // letters are moved by +-13 characters
        Assert.assertEquals("nop", PasswordGenerator.rot13("abc"));
        // the transformation it is reversable
        Assert.assertEquals("abc", PasswordGenerator.rot13("nop"));
        // which means for any string
        Assert.assertEquals("abcdefghijklmnooprstuvxyzuv1234?",
                PasswordGenerator.rot13(PasswordGenerator
                        .rot13("abcdefghijklmnooprstuvxyzuv1234?")));
        // same for capital letters
        Assert.assertEquals("ABC", PasswordGenerator.rot13("NOP"));
        Assert.assertEquals("NOP", PasswordGenerator.rot13("ABC"));
    }
}
