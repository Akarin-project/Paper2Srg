package net.minecraft.util;

import java.io.OutputStream;
import java.io.PrintStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingPrintStream extends PrintStream {

    protected static final Logger field_179884_a = LogManager.getLogger();
    protected final String field_179883_b;

    public LoggingPrintStream(String s, OutputStream outputstream) {
        super(outputstream);
        this.field_179883_b = s;
    }

    public void println(String s) {
        this.func_179882_a(s);
    }

    public void println(Object object) {
        this.func_179882_a(String.valueOf(object));
    }

    protected void func_179882_a(String s) {
        LoggingPrintStream.field_179884_a.info("[{}]: {}", this.field_179883_b, s);
    }
}
