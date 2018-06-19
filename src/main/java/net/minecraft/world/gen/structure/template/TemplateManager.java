package net.minecraft.world.gen.structure.template;

import com.google.common.collect.Maps;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.io.IOUtils;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IFixType;

public class TemplateManager {

    private final Map<String, Template> field_186240_a = Maps.newHashMap();
    private final String field_186241_b;
    private final DataFixer field_191154_c;

    public TemplateManager(String s, DataFixer dataconvertermanager) {
        this.field_186241_b = s;
        this.field_191154_c = dataconvertermanager;
    }

    public Template func_186237_a(@Nullable MinecraftServer minecraftserver, ResourceLocation minecraftkey) {
        Template definedstructure = this.func_189942_b(minecraftserver, minecraftkey);

        if (definedstructure == null) {
            definedstructure = new Template();
            this.field_186240_a.put(minecraftkey.func_110623_a(), definedstructure);
        }

        return definedstructure;
    }

    @Nullable
    public Template func_189942_b(@Nullable MinecraftServer minecraftserver, ResourceLocation minecraftkey) {
        String s = minecraftkey.func_110623_a();

        if (this.field_186240_a.containsKey(s)) {
            return (Template) this.field_186240_a.get(s);
        } else {
            if (minecraftserver == null) {
                this.func_186236_a(minecraftkey);
            } else {
                this.func_186235_b(minecraftkey);
            }

            return this.field_186240_a.containsKey(s) ? (Template) this.field_186240_a.get(s) : null;
        }
    }

    public boolean func_186235_b(ResourceLocation minecraftkey) {
        String s = minecraftkey.func_110623_a();
        File file = new File(this.field_186241_b, s + ".nbt");

        if (!file.exists()) {
            return this.func_186236_a(minecraftkey);
        } else {
            FileInputStream fileinputstream = null;

            boolean flag;

            try {
                fileinputstream = new FileInputStream(file);
                this.func_186239_a(s, (InputStream) fileinputstream);
                return true;
            } catch (Throwable throwable) {
                flag = false;
            } finally {
                IOUtils.closeQuietly(fileinputstream);
            }

            return flag;
        }
    }

    private boolean func_186236_a(ResourceLocation minecraftkey) {
        String s = minecraftkey.func_110624_b();
        String s1 = minecraftkey.func_110623_a();
        InputStream inputstream = null;

        boolean flag;

        try {
            inputstream = MinecraftServer.class.getResourceAsStream("/assets/" + s + "/structures/" + s1 + ".nbt");
            this.func_186239_a(s1, inputstream);
            return true;
        } catch (Throwable throwable) {
            flag = false;
        } finally {
            IOUtils.closeQuietly(inputstream);
        }

        return flag;
    }

    private void func_186239_a(String s, InputStream inputstream) throws IOException {
        NBTTagCompound nbttagcompound = CompressedStreamTools.func_74796_a(inputstream);

        if (!nbttagcompound.func_150297_b("DataVersion", 99)) {
            nbttagcompound.func_74768_a("DataVersion", 500);
        }

        Template definedstructure = new Template();

        definedstructure.func_186256_b(this.field_191154_c.func_188257_a((IFixType) FixTypes.STRUCTURE, nbttagcompound));
        this.field_186240_a.put(s, definedstructure);
    }

    public boolean func_186238_c(@Nullable MinecraftServer minecraftserver, ResourceLocation minecraftkey) {
        String s = minecraftkey.func_110623_a();

        if (minecraftserver != null && this.field_186240_a.containsKey(s)) {
            File file = new File(this.field_186241_b);

            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return false;
                }
            } else if (!file.isDirectory()) {
                return false;
            }

            File file1 = new File(file, s + ".nbt");
            Template definedstructure = (Template) this.field_186240_a.get(s);
            FileOutputStream fileoutputstream = null;

            boolean flag;

            try {
                NBTTagCompound nbttagcompound = definedstructure.func_189552_a(new NBTTagCompound());

                fileoutputstream = new FileOutputStream(file1);
                CompressedStreamTools.func_74799_a(nbttagcompound, (OutputStream) fileoutputstream);
                return true;
            } catch (Throwable throwable) {
                flag = false;
            } finally {
                IOUtils.closeQuietly(fileoutputstream);
            }

            return flag;
        } else {
            return false;
        }
    }

    public void func_189941_a(ResourceLocation minecraftkey) {
        this.field_186240_a.remove(minecraftkey.func_110623_a());
    }
}
