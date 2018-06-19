package net.minecraft.command;

public class CommandException extends Exception {

    private final Object[] field_74845_a;

    public CommandException(String s, Object... aobject) {
        super(s);
        this.field_74845_a = aobject;
    }

    public Object[] func_74844_a() {
        return this.field_74845_a;
    }

    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
