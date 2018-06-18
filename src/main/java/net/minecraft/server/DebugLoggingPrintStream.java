package net.minecraft.server;

import java.io.OutputStream;

import net.minecraft.util.LoggingPrintStream;

public class DebugLoggingPrintStream extends LoggingPrintStream {

    public DebugLoggingPrintStream(String s, OutputStream outputstream) {
        super(s, outputstream);
    }

    protected void logString(String s) {
        StackTraceElement[] astacktraceelement = Thread.currentThread().getStackTrace();
        StackTraceElement stacktraceelement = astacktraceelement[Math.min(3, astacktraceelement.length)];

        DebugLoggingPrintStream.LOGGER.info("[{}]@.({}:{}): {}", this.domain, stacktraceelement.getFileName(), Integer.valueOf(stacktraceelement.getLineNumber()), s);
    }
}
