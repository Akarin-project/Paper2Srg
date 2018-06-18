package net.minecraft.command;

public interface ICommandListener {

    void notifyListener(ICommandSender icommandlistener, ICommand icommand, int i, String s, Object... aobject);
}
