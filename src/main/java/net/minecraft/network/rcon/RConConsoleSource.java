package net.minecraft.network.rcon;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;


public class RConConsoleSource implements ICommandSender {

    private final StringBuffer field_70009_b = new StringBuffer();
    private final MinecraftServer field_184171_b;

    public RConConsoleSource(MinecraftServer minecraftserver) {
        this.field_184171_b = minecraftserver;
    }

    public void func_70007_b() {
        this.field_70009_b.setLength(0);
    }

    public String func_70008_c() {
        return this.field_70009_b.toString();
    }

    public String func_70005_c_() {
        return "Rcon";
    }

    // CraftBukkit start - Send a String
    public void sendMessage(String message) {
        this.field_70009_b.append(message);
    }
    // CraftBukkit end

    public void func_145747_a(ITextComponent ichatbasecomponent) {
        this.field_70009_b.append(ichatbasecomponent.func_150260_c());
    }

    public boolean func_70003_b(int i, String s) {
        return true;
    }

    public World func_130014_f_() {
        return this.field_184171_b.func_130014_f_();
    }

    public boolean func_174792_t_() {
        return true;
    }

    public MinecraftServer func_184102_h() {
        return this.field_184171_b;
    }
}
