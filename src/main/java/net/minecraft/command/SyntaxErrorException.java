package net.minecraft.command;

public class SyntaxErrorException extends CommandException {

    public SyntaxErrorException() {
        this("commands.generic.snytax", new Object[0]);
    }

    public SyntaxErrorException(String s, Object... aobject) {
        super(s, aobject);
    }

    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
