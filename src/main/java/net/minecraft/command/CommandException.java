package net.minecraft.command;

public class CommandException extends Exception {

    private final Object[] errorObjects;

    public CommandException(String s, Object... aobject) {
        super(s);
        this.errorObjects = aobject;
    }

    public Object[] getErrorObjects() {
        return this.errorObjects;
    }

    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
