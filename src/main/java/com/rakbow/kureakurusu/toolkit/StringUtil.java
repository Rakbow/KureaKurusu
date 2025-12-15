package com.rakbow.kureakurusu.toolkit;

import org.springframework.lang.Nullable;

/**
 * @author Rakbow
 * @since 2025/12/15 20:40
 */
public class StringUtil {

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen = length(cs);
        if (strLen != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

        }
        return true;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.isEmpty();
    }

    public static int length(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    public static boolean equals(CharSequence cs1, CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        } else if (cs1 != null && cs2 != null) {
            if (cs1.length() != cs2.length()) {
                return false;
            } else if (cs1 instanceof String && cs2 instanceof String) {
                return cs1.equals(cs2);
            } else {
                int length = cs1.length();

                for(int i = 0; i < length; ++i) {
                    if (cs1.charAt(i) != cs2.charAt(i)) {
                        return false;
                    }
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public static boolean hasLength(@Nullable String str) {
        return str != null && !str.isEmpty();
    }

    public static String camelToUnderline(String param) {
        if (isBlank(param)) {
            return "";
        } else {
            int len = param.length();
            StringBuilder sb = new StringBuilder(len);

            for(int i = 0; i < len; ++i) {
                char c = param.charAt(i);
                if (Character.isUpperCase(c) && i > 0) {
                    sb.append('_');
                }

                sb.append(Character.toLowerCase(c));
            }

            return sb.toString();
        }
    }

    public static String uncapitalize(String str) {
        int strLen = length(str);
        if (strLen == 0) {
            return str;
        } else {
            int firstCodePoint = str.codePointAt(0);
            int newCodePoint = Character.toLowerCase(firstCodePoint);
            if (firstCodePoint == newCodePoint) {
                return str;
            } else {
                int[] newCodePoints = new int[strLen];
                int outOffset = 0;
                newCodePoints[outOffset++] = newCodePoint;

                int codePoint;
                for(int inOffset = Character.charCount(firstCodePoint); inOffset < strLen; inOffset += Character.charCount(codePoint)) {
                    codePoint = str.codePointAt(inOffset);
                    newCodePoints[outOffset++] = codePoint;
                }

                return new String(newCodePoints, 0, outOffset);
            }
        }
    }

}
