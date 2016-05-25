package net.mcsproject.master.logging;

public class Log {

    public static void info(String msg) {
        System.out.println("[" + Thread.currentThread().getName() + "] INFO - " + msg);
    }

    public static void warning(String msg) {
        System.out.println("[" + Thread.currentThread().getName() + "] WARN - " + msg);
    }

    public static void error(String msg) {
        System.out.println("[" + Thread.currentThread().getName() + "] ERR! - " + msg);
    }

}
