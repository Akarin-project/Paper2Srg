package net.minecraft.command;

public class EntityNotFoundException extends CommandException {

    public EntityNotFoundException(String s) {
        this("commands.generic.entity.notFound", new Object[] { s});
    }

    public EntityNotFoundException(String s, Object... aobject) {
        super(s, aobject);
    }

    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
