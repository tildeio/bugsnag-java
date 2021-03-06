package com.bugsnag;

import java.lang.Thread.UncaughtExceptionHandler;

class ExceptionHandler implements UncaughtExceptionHandler {
    private UncaughtExceptionHandler originalHandler;
    private Client client;

    public static void install(Client client) {
        UncaughtExceptionHandler currentHandler = Thread.getDefaultUncaughtExceptionHandler();
        if(currentHandler instanceof ExceptionHandler) {
            currentHandler = ((ExceptionHandler)currentHandler).getOriginalHandler();
        }

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(currentHandler, client));
    }

    public static void remove() {
        UncaughtExceptionHandler currentHandler = Thread.getDefaultUncaughtExceptionHandler();
        if(currentHandler instanceof ExceptionHandler) {
            Thread.setDefaultUncaughtExceptionHandler(((ExceptionHandler)currentHandler).getOriginalHandler());
        }
    }

    public ExceptionHandler(UncaughtExceptionHandler originalHandler, Client client) {
        this.originalHandler = originalHandler;
        this.client = client;
    }

    public void uncaughtException(Thread t, Throwable e) {
        client.autoNotify(e);
        originalHandler.uncaughtException(t, e);
    }

    public UncaughtExceptionHandler getOriginalHandler() {
        return this.originalHandler;
    }
}