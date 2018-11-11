package util;

@SuppressWarnings("unused")
public class ThreadUtils {
    public static String getCurrentMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    public static String getCurrentClassName() {
        return Thread.currentThread().getStackTrace()[2].getClassName();
    }

    public static String getMethodNameCalledThis() {
        StackTraceElement[] steArray = Thread.currentThread().getStackTrace();
        if (steArray.length <= 3) {
            return "";
        }
        return steArray[3].getMethodName();
    }

    public static String getClassNameCalledThis() {
        return getClassNameCalledThis(1);
    }

    public static String getClassNameCalledThis(int returnTimes) {
        StackTraceElement[] steArray = Thread.currentThread().getStackTrace();
        if (steArray.length <= 2 + returnTimes) {
            return "";
        }
        return steArray[2 + returnTimes].getClassName();
    }
}
