package com.yankaizhang.board.util;


@SuppressWarnings("all")
public class Logger {

    private static final Logger INSTANCE = new Logger();

    private Logger() {}

    public static Logger getInstance(){ return INSTANCE; }

    public void debug(String msg){
        System.out.println("[DEBUG] " + msg);
    }

    public void info(String msg){
        System.out.println("[INFO]  " + msg);
    }
}
