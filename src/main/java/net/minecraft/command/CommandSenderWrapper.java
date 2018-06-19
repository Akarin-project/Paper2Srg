package net.minecraft.command;

import java.util.Objects;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class CommandSenderWrapper implements ICommandSender {

    public final ICommandSender field_193043_a;
    @Nullable
    private final Vec3d field_194002_b;
    @Nullable
    private final BlockPos field_194003_c;
    @Nullable
    private final Integer field_194004_d;
    @Nullable
    private final Entity field_194005_e;
    @Nullable
    private final Boolean field_194006_f;

    public CommandSenderWrapper(ICommandSender icommandlistener, @Nullable Vec3d vec3d, @Nullable BlockPos blockposition, @Nullable Integer integer, @Nullable Entity entity, @Nullable Boolean obool) {
        this.field_193043_a = icommandlistener;
        this.field_194002_b = vec3d;
        this.field_194003_c = blockposition;
        this.field_194004_d = integer;
        this.field_194005_e = entity;
        this.field_194006_f = obool;
    }

    public static CommandSenderWrapper func_193998_a(ICommandSender icommandlistener) {
        return icommandlistener instanceof CommandSenderWrapper ? (CommandSenderWrapper) icommandlistener : new CommandSenderWrapper(icommandlistener, (Vec3d) null, (BlockPos) null, (Integer) null, (Entity) null, (Boolean) null);
    }

    public CommandSenderWrapper func_193997_a(Entity entity, Vec3d vec3d) {
        return this.field_194005_e == entity && Objects.equals(this.field_194002_b, vec3d) ? this : new CommandSenderWrapper(this.field_193043_a, vec3d, new BlockPos(vec3d), this.field_194004_d, entity, this.field_194006_f);
    }

    public CommandSenderWrapper func_193999_a(int i) {
        return this.field_194004_d != null && this.field_194004_d.intValue() <= i ? this : new CommandSenderWrapper(this.field_193043_a, this.field_194002_b, this.field_194003_c, Integer.valueOf(i), this.field_194005_e, this.field_194006_f);
    }

    public CommandSenderWrapper func_194001_a(boolean flag) {
        return this.field_194006_f != null && (!this.field_194006_f.booleanValue() || flag) ? this : new CommandSenderWrapper(this.field_193043_a, this.field_194002_b, this.field_194003_c, this.field_194004_d, this.field_194005_e, Boolean.valueOf(flag));
    }

    public CommandSenderWrapper func_194000_i() {
        return this.field_194002_b != null ? this : new CommandSenderWrapper(this.field_193043_a, this.func_174791_d(), this.func_180425_c(), this.field_194004_d, this.field_194005_e, this.field_194006_f);
    }

    public String func_70005_c_() {
        return this.field_194005_e != null ? this.field_194005_e.func_70005_c_() : this.field_193043_a.func_70005_c_();
    }

    public ITextComponent func_145748_c_() {
        return this.field_194005_e != null ? this.field_194005_e.func_145748_c_() : this.field_193043_a.func_145748_c_();
    }

    public void func_145747_a(ITextComponent ichatbasecomponent) {
        if (this.field_194006_f == null || this.field_194006_f.booleanValue()) {
            this.field_193043_a.func_145747_a(ichatbasecomponent);
        }
    }

    public boolean func_70003_b(int i, String s) {
        return this.field_194004_d != null && this.field_194004_d.intValue() < i ? false : this.field_193043_a.func_70003_b(i, s);
    }

    public BlockPos func_180425_c() {
        return this.field_194003_c != null ? this.field_194003_c : (this.field_194005_e != null ? this.field_194005_e.func_180425_c() : this.field_193043_a.func_180425_c());
    }

    public Vec3d func_174791_d() {
        return this.field_194002_b != null ? this.field_194002_b : (this.field_194005_e != null ? this.field_194005_e.func_174791_d() : this.field_193043_a.func_174791_d());
    }

    public World func_130014_f_() {
        return this.field_194005_e != null ? this.field_194005_e.func_130014_f_() : this.field_193043_a.func_130014_f_();
    }

    @Nullable
    public Entity func_174793_f() {
        return this.field_194005_e != null ? this.field_194005_e.func_174793_f() : this.field_193043_a.func_174793_f();
    }

    public boolean func_174792_t_() {
        return this.field_194006_f != null ? this.field_194006_f.booleanValue() : this.field_193043_a.func_174792_t_();
    }

    public void func_174794_a(CommandResultStats.Type commandobjectiveexecutor_enumcommandresult, int i) {
        if (this.field_194005_e != null) {
            this.field_194005_e.func_174794_a(commandobjectiveexecutor_enumcommandresult, i);
        } else {
            this.field_193043_a.func_174794_a(commandobjectiveexecutor_enumcommandresult, i);
        }
    }

    @Nullable
    public MinecraftServer func_184102_h() {
        return this.field_193043_a.func_184102_h();
    }
}
