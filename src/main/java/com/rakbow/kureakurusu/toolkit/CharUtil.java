package com.rakbow.kureakurusu.toolkit;

/**
 * @author Rakbow
 * @since 2025/12/15 20:58
 */
public class CharUtil {

    public static boolean isAsciiAlphanumeric(char ch) {
        return isAsciiAlpha(ch) || isAsciiNumeric(ch);
    }

    public static boolean isAsciiAlpha(char ch) {
        return isAsciiAlphaUpper(ch) || isAsciiAlphaLower(ch);
    }

    public static boolean isAsciiAlphaUpper(char ch) {
        return ch >= 'A' && ch <= 'Z';
    }

    public static boolean isAsciiAlphaLower(char ch) {
        return ch >= 'a' && ch <= 'z';
    }

    public static boolean isAsciiNumeric(char ch) {
        return ch >= '0' && ch <= '9';
    }

}
