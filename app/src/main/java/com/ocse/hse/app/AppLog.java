package com.ocse.hse.app;
import android.util.Log;

/**
 * Created by leehaining on 14-4-8.
 */
public class AppLog {
    public static void d(final String debugMsg)
    {
        if (ApplicationConstants.IS_DEBUGGING_ON)
        {
            final String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            final String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            final String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            final int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

            Log.d(ApplicationConstants.LOG_TAG, "#" + lineNumber + " " + className + "." + methodName + "() : " + debugMsg);
        }
    }
    public static void e(final String error)
    {
        if (ApplicationConstants.IS_DEBUGGING_ON)
        {
            final String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            final String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            final String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            final int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

            Log.e(ApplicationConstants.LOG_TAG, "#" + lineNumber + " " + className + "." + methodName + "() : " + error);
        }
    }
    public static void i(final String informationString)
    {
        if (ApplicationConstants.IS_DEBUGGING_ON)
        {
            final String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            final String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            final String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            final int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

            Log.i(ApplicationConstants.LOG_TAG, "#" + lineNumber + " " + className + "." + methodName + "() : " + informationString);
        }
    }
}
