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

    private final Map<String, Template> templates = Maps.newHashMap();
    private final String baseFolder;
    private final DataFixer fixer;

    public TemplateManager(String s, DataFixer dataconvertermanager) {
        this.baseFolder = s;
        this.fixer = dataconvertermanager;
    }

    public Template getTemplate(@Nullable MinecraftServer minecraftserver, ResourceLocation minecraftkey) {
        Template definedstructure = this.get(minecraftserver, minecraftkey);

        if (definedstructure == null) {
            definedstructure = new Template();
            this.templates.put(minecraftkey.getResourcePath(), definedstructure);
        }

        return definedstructure;
    }

    @Nullable
    public Template get(@Nullable MinecraftServer minecraftserver, ResourceLocation minecraftkey) {
        String s = minecraftkey.getResourcePath();

        if (this.templates.containsKey(s)) {
            return (Template) this.templates.get(s);
        } else {
            if (minecraftserver == null) {
                this.readTemplateFromJar(minecraftkey);
            } else {
                this.readTemplate(minecraftkey);
            }

            return this.templates.containsKey(s) ? (Template) this.templates.get(s) : null;
        }
    }

    public boolean readTemplate(ResourceLocation minecraftkey) {
        String s = minecraftkey.getResourcePath();
        File file = new File(this.baseFolder, s + ".nbt");

        if (!file.exists()) {
            return this.readTemplateFromJar(minecraftkey);
        } else {
            FileInputStream fileinputstream = null;

            boolean flag;

            try {
                fileinputstream = new FileInputStream(file);
                this.readTemplateFromStream(s, (InputStream) fileinputstream);
                return true;
            } catch (Throwable throwable) {
                flag = false;
            } finally {
                IOUtils.closeQuietly(fileinputstream);
            }

            return flag;
        }
    }

    private boolean readTemplateFromJar(ResourceLocation minecraftkey) {
        String s = minecraftkey.getResourceDomain();
        String s1 = minecraftkey.getResourcePath();
        InputStream inputstream = null;

        boolean flag;

        try {
            inputstream = MinecraftServer.class.getResourceAsStream("/assets/" + s + "/structures/" + s1 + ".nbt");
            this.readTemplateFromStream(s1, inputstream);
            return true;
        } catch (Throwable throwable) {
            flag = false;
        } finally {
            IOUtils.closeQuietly(inputstream);
        }

        return flag;
    }

    private void readTemplateFromStream(String s, InputStream inputstream) throws IOException {
        NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(inputstream);

        if (!nbttagcompound.hasKey("DataVersion", 99)) {
            nbttagcompound.setInteger("DataVersion", 500);
        }

        Template definedstructure = new Template();

        definedstructure.read(this.fixer.process((IFixType) FixTypes.STRUCTURE, nbttagcompound));
        this.templates.put(s, definedstructure);
    }

    public boolean writeTemplate(@Nullable MinecraftServer minecraftserver, ResourceLocation minecraftkey) {
        String s = minecraftkey.getResourcePath();

        if (minecraftserver != null && this.templates.containsKey(s)) {
            File file = new File(this.baseFolder);

            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return false;
                }
            } else if (!file.isDirectory()) {
                return false;
            }

            File file1 = new File(file, s + ".nbt");
            Template definedstructure = (Template) this.templates.get(s);
            FileOutputStream fileoutputstream = null;

            boolean flag;

            try {
                NBTTagCompound nbttagcompound = definedstructure.writeToNBT(new NBTTagCompound());

                fileoutputstream = new FileOutputStream(file1);
                CompressedStreamTools.writeCompressed(nbttagcompound, (OutputStream) fileoutputstream);
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

    public void remove(ResourceLocation minecraftkey) {
        this.templates.remove(minecraftkey.getResourcePath());
    }
}
