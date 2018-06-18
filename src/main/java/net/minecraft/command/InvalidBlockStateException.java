package net.minecraft.command;

public class InvalidBlockStateException extends CommandException {

    public InvalidBlockStateException() {
        this("commands.generic.blockstate.invalid", new Object[0]);
    }

    public InvalidBlockStateException(String s, Object... aobject) {
        super(s, aobject);
    }

    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
