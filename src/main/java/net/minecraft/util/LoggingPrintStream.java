package net.minecraft.util;

import java.io.OutputStream;
import java.io.PrintStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingPrintStream extends PrintStream {

    protected static final Logger LOGGER = LogManager.getLogger();
    protected final String domain;

    public LoggingPrintStream(String s, OutputStream outputstream) {
        super(outputstream);
        this.domain = s;
    }

    public void println(String s) {
        this.logString(s);
    }

    public void println(Object object) {
        this.logString(String.valueOf(object));
    }

    protected void logString(String s) {
        LoggingPrintStream.LOGGER.info("[{}]: {}", this.domain, s);
    }
}
