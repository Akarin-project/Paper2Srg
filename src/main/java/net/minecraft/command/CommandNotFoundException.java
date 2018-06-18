package net.minecraft.command;

public class CommandNotFoundException extends CommandException {

    public CommandNotFoundException() {
        this("commands.generic.notFound", new Object[0]);
    }

    public CommandNotFoundException(String s, Object... aobject) {
        super(s, aobject);
    }

    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
