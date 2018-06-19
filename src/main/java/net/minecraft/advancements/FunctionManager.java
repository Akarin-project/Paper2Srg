package net.minecraft.advancements;

import com.google.common.collect.Maps;
import com.google.common.io.Files;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.command.FunctionObject;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class FunctionManager implements ITickable {

    private static final Logger field_193067_a = LogManager.getLogger();
    private final File field_193068_b;
    private final MinecraftServer field_193069_c;
    private final Map<ResourceLocation, FunctionObject> field_193070_d = Maps.newHashMap();
    private String field_193071_e = "-";
    private FunctionObject field_193072_f;
    private final ArrayDeque<FunctionManager.a> field_194020_g = new ArrayDeque();
    private boolean field_194021_h = false;
    // CraftBukkit start
    private final ICommandSender field_193073_g = new CustomFunctionListener();

    public class CustomFunctionListener implements ICommandSender {

        protected org.bukkit.command.CommandSender sender = new org.bukkit.craftbukkit.command.CraftFunctionCommandSender(this);
        // CraftBukkit end

        @Override
        public String func_70005_c_() {
            return FunctionManager.this.field_193071_e;
        }

        @Override
        public boolean func_70003_b(int i, String s) {
            return i <= 2;
        }

        @Override
        public World func_130014_f_() {
            return FunctionManager.this.field_193069_c.worlds.get(0); // CraftBukkit
        }

        @Override
        public MinecraftServer func_184102_h() {
            return FunctionManager.this.field_193069_c;
        }
    };

    public FunctionManager(@Nullable File file, MinecraftServer minecraftserver) {
        this.field_193068_b = file;
        this.field_193069_c = minecraftserver;
        this.func_193059_f();
    }

    @Nullable
    public FunctionObject func_193058_a(ResourceLocation minecraftkey) {
        return this.field_193070_d.get(minecraftkey);
    }

    public ICommandManager func_193062_a() {
        return this.field_193069_c.func_71187_D();
    }

    public int func_193065_c() {
        return this.field_193069_c.worlds.get(0).func_82736_K().func_180263_c("maxCommandChainLength"); // CraftBukkit
    }

    public Map<ResourceLocation, FunctionObject> func_193066_d() {
        return this.field_193070_d;
    }

    @Override
    public void func_73660_a() {
        String s = this.field_193069_c.worlds.get(0).func_82736_K().func_82767_a("gameLoopFunction"); // CraftBukkit

        if (!s.equals(this.field_193071_e)) {
            this.field_193071_e = s;
            this.field_193072_f = this.func_193058_a(new ResourceLocation(s));
        }

        if (this.field_193072_f != null) {
            this.func_194019_a(this.field_193072_f, this.field_193073_g);
        }

    }

    public int func_194019_a(FunctionObject customfunction, ICommandSender icommandlistener) {
        int i = this.func_193065_c();

        if (this.field_194021_h) {
            if (this.field_194020_g.size() < i) {
                this.field_194020_g.addFirst(new FunctionManager.a(this, icommandlistener, new CustomFunction.d(customfunction)));
            }

            return 0;
        } else {
            int j;

            try {
                this.field_194021_h = true;
                int k = 0;
                CustomFunction.c[] acustomfunction_c = customfunction.a();

                for (j = acustomfunction_c.length - 1; j >= 0; --j) {
                    this.field_194020_g.push(new FunctionManager.a(this, icommandlistener, acustomfunction_c[j]));
                }

                do {
                    if (this.field_194020_g.isEmpty()) {
                        j = k;
                        return j;
                    }

                    this.field_194020_g.removeFirst().a(this.field_194020_g, i);
                    ++k;
                } while (k < i);

                j = k;
            } finally {
                this.field_194020_g.clear();
                this.field_194021_h = false;
            }

            return j;
        }
    }

    public void func_193059_f() {
        this.field_193070_d.clear();
        this.field_193072_f = null;
        this.field_193071_e = "-";
        this.func_193061_h();
    }

    private void func_193061_h() {
        if (this.field_193068_b != null) {
            this.field_193068_b.mkdirs();
            Iterator iterator = FileUtils.listFiles(this.field_193068_b, new String[] { "mcfunction"}, true).iterator();

            while (iterator.hasNext()) {
                File file = (File) iterator.next();
                String s = FilenameUtils.removeExtension(this.field_193068_b.toURI().relativize(file.toURI()).toString());
                String[] astring = s.split("/", 2);

                if (astring.length == 2) {
                    ResourceLocation minecraftkey = new ResourceLocation(astring[0], astring[1]);

                    try {
                        this.field_193070_d.put(minecraftkey, FunctionObject.func_193527_a(this, Files.readLines(file, StandardCharsets.UTF_8)));
                    } catch (Throwable throwable) {
                        FunctionManager.field_193067_a.error("Couldn\'t read custom function " + minecraftkey + " from " + file, throwable);
                    }
                }
            }

            if (!this.field_193070_d.isEmpty()) {
                FunctionManager.field_193067_a.info("Loaded " + this.field_193070_d.size() + " custom command functions");
            }

        }
    }

    public static class a {

        private final FunctionManager a;
        private final ICommandSender b;
        private final FunctionObject.c c;

        public a(FunctionManager customfunctiondata, ICommandSender icommandlistener, FunctionObject.c customfunction_c) {
            this.a = customfunctiondata;
            this.b = icommandlistener;
            this.c = customfunction_c;
        }

        public void a(ArrayDeque<FunctionManager.a> arraydeque, int i) {
            this.c.a(this.a, this.b, arraydeque, i);
        }

        @Override
        public String toString() {
            return this.c.toString();
        }
    }
}
