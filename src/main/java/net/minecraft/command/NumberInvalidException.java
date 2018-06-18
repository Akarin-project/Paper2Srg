package net.minecraft.command;

public class NumberInvalidException extends CommandException {

    public NumberInvalidException() {
        this("commands.generic.num.invalid", new Object[0]);
    }

    public NumberInvalidException(String s, Object... aobject) {
        super(s, aobject);
    }

    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
