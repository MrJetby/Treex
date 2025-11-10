package me.jetby.treex.tools.log;


public interface Logger {

    void warn(String message);

    void info(String message);

    void success(String message);

    void error(String message);

    void msg(String message);
}
