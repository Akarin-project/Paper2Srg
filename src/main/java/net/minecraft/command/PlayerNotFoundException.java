package net.minecraft.command;

public class PlayerNotFoundException extends CommandException {

    public PlayerNotFoundException(String s) {
        super(s, new Object[0]);
    }

    public PlayerNotFoundException(String s, Object... aobject) {
        super(s, aobject);
    }

    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
