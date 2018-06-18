package net.minecraft.command;

public class WrongUsageException extends SyntaxErrorException {

    public WrongUsageException(String s, Object... aobject) {
        super(s, aobject);
    }

    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
