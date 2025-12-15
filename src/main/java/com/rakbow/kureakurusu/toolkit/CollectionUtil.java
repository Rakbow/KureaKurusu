package com.rakbow.kureakurusu.toolkit;

/**
 * @author Rakbow
 * @since 2025/12/15 20:55
 */
public class CollectionUtil {

    public static boolean contains(Object[] array, Object objectToFind) {
        return indexOf(array, objectToFind) != -1;
    }

    public static int indexOf(Object[] array, Object objectToFind) {
        return indexOf(array, objectToFind, 0);
    }

    public static int indexOf(Object[] array, Object objectToFind, int startIndex) {
        if (array == null) {
            return -1;
        } else {
            if (startIndex < 0) {
                startIndex = 0;
            }

            if (objectToFind == null) {
                for(int i = startIndex; i < array.length; ++i) {
                    if (array[i] == null) {
                        return i;
                    }
                }
            } else {
                for(int i = startIndex; i < array.length; ++i) {
                    if (objectToFind.equals(array[i])) {
                        return i;
                    }
                }
            }

            return -1;
        }
    }

}
