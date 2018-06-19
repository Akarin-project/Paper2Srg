package net.minecraft.world.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IFixType;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.structure.template.TemplateManager;
import org.bukkit.craftbukkit.entity.CraftPlayer;

// CraftBukkit start
import java.util.UUID;
import org.bukkit.craftbukkit.entity.CraftPlayer;
// CraftBukkit end

public class SaveHandler implements ISaveHandler, IPlayerFileData {

    private static final Logger field_151478_a = LogManager.getLogger();
    private final File field_75770_b;
    private final File field_75771_c;
    private final File field_75768_d;
    private final long field_75769_e = MinecraftServer.func_130071_aq();
    private final String field_75767_f;
    private final TemplateManager field_186342_h;
    protected final DataFixer field_186341_a;
    private UUID uuid = null; // CraftBukkit

    public SaveHandler(File file, String s, boolean flag, DataFixer dataconvertermanager) {
        this.field_186341_a = dataconvertermanager;
        this.field_75770_b = new File(file, s);
        this.field_75770_b.mkdirs();
        this.field_75771_c = new File(this.field_75770_b, "playerdata");
        this.field_75768_d = new File(this.field_75770_b, "data");
        this.field_75768_d.mkdirs();
        this.field_75767_f = s;
        if (flag) {
            this.field_75771_c.mkdirs();
            this.field_186342_h = new TemplateManager((new File(this.field_75770_b, "structures")).toString(), dataconvertermanager);
        } else {
            this.field_186342_h = null;
        }

        this.func_75766_h();
    }

    private void func_75766_h() {
        try {
            File file = new File(this.field_75770_b, "session.lock");
            DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file));

            try {
                dataoutputstream.writeLong(this.field_75769_e);
            } finally {
                dataoutputstream.close();
            }

        } catch (IOException ioexception) {
            ioexception.printStackTrace();
            throw new RuntimeException("Failed to check session lock for world located at " + this.field_75770_b + ", aborting. Stop the server and delete the session.lock in this world to prevent further issues."); // Spigot
        }
    }

    public File func_75765_b() {
        return this.field_75770_b;
    }

    public void func_75762_c() throws MinecraftException {
        try {
            File file = new File(this.field_75770_b, "session.lock");
            DataInputStream datainputstream = new DataInputStream(new FileInputStream(file));

            try {
                if (datainputstream.readLong() != this.field_75769_e) {
                    throw new MinecraftException("The save for world located at " + this.field_75770_b + " is being accessed from another location, aborting");  // Spigot
                }
            } finally {
                datainputstream.close();
            }

        } catch (IOException ioexception) {
            throw new MinecraftException("Failed to check session lock for world located at " + this.field_75770_b + ", aborting. Stop the server and delete the session.lock in this world to prevent further issues."); // Spigot
        }
    }

    public IChunkLoader func_75763_a(WorldProvider worldprovider) {
        throw new RuntimeException("Old Chunk Storage is no longer supported.");
    }

    @Nullable
    public WorldInfo func_75757_d() {
        File file = new File(this.field_75770_b, "level.dat");

        if (file.exists()) {
            WorldInfo worlddata = SaveFormatOld.func_186353_a(file, this.field_186341_a);

            if (worlddata != null) {
                return worlddata;
            }
        }

        file = new File(this.field_75770_b, "level.dat_old");
        return file.exists() ? SaveFormatOld.func_186353_a(file, this.field_186341_a) : null;
    }

    public void func_75755_a(WorldInfo worlddata, @Nullable NBTTagCompound nbttagcompound) {
        NBTTagCompound nbttagcompound1 = worlddata.func_76082_a(nbttagcompound);
        NBTTagCompound nbttagcompound2 = new NBTTagCompound();

        nbttagcompound2.func_74782_a("Data", nbttagcompound1);

        try {
            File file = new File(this.field_75770_b, "level.dat_new");
            File file1 = new File(this.field_75770_b, "level.dat_old");
            File file2 = new File(this.field_75770_b, "level.dat");

            CompressedStreamTools.func_74799_a(nbttagcompound2, (OutputStream) (new FileOutputStream(file)));
            if (file1.exists()) {
                file1.delete();
            }

            file2.renameTo(file1);
            if (file2.exists()) {
                file2.delete();
            }

            file.renameTo(file2);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public void func_75761_a(WorldInfo worlddata) {
        this.func_75755_a(worlddata, (NBTTagCompound) null);
    }

    public void func_75753_a(EntityPlayer entityhuman) {
        if(!com.destroystokyo.paper.PaperConfig.savePlayerData) return; // Paper - Make player data saving configurable
        try {
            NBTTagCompound nbttagcompound = entityhuman.func_189511_e(new NBTTagCompound());
            File file = new File(this.field_75771_c, entityhuman.func_189512_bd() + ".dat.tmp");
            File file1 = new File(this.field_75771_c, entityhuman.func_189512_bd() + ".dat");

            CompressedStreamTools.func_74799_a(nbttagcompound, (OutputStream) (new FileOutputStream(file)));
            if (file1.exists()) {
                file1.delete();
            }

            file.renameTo(file1);
        } catch (Exception exception) {
            SaveHandler.field_151478_a.error("Failed to save player data for {}", entityhuman.func_70005_c_(), exception); // Paper
        }

    }

    @Nullable
    public NBTTagCompound func_75752_b(EntityPlayer entityhuman) {
        NBTTagCompound nbttagcompound = null;

        try {
            File file = new File(this.field_75771_c, entityhuman.func_189512_bd() + ".dat");
            // Spigot Start
            boolean usingWrongFile = false;
            if ( org.bukkit.Bukkit.getOnlineMode() && !file.exists() ) // Paper - Check online mode first
            {
                file = new File( this.field_75771_c, UUID.nameUUIDFromBytes( ( "OfflinePlayer:" + entityhuman.func_70005_c_() ).getBytes( "UTF-8" ) ).toString() + ".dat");
                if ( file.exists() )
                {
                    usingWrongFile = true;
                    org.bukkit.Bukkit.getServer().getLogger().warning( "Using offline mode UUID file for player " + entityhuman.func_70005_c_() + " as it is the only copy we can find." );
                }
            }
            // Spigot End

            if (file.exists() && file.isFile()) {
                nbttagcompound = CompressedStreamTools.func_74796_a((InputStream) (new FileInputStream(file)));
            }
            // Spigot Start
            if ( usingWrongFile )
            {
                file.renameTo( new File( file.getPath() + ".offline-read" ) );
            }
            // Spigot End
        } catch (Exception exception) {
            SaveHandler.field_151478_a.warn("Failed to load player data for {}", entityhuman.func_70005_c_());
        }

        if (nbttagcompound != null) {
            // CraftBukkit start
            if (entityhuman instanceof EntityPlayerMP) {
                CraftPlayer player = (CraftPlayer) entityhuman.getBukkitEntity();
                // Only update first played if it is older than the one we have
                long modified = new File(this.field_75771_c, entityhuman.func_110124_au().toString() + ".dat").lastModified();
                if (modified < player.getFirstPlayed()) {
                    player.setFirstPlayed(modified);
                }
            }
            // CraftBukkit end
            entityhuman.func_70020_e(this.field_186341_a.func_188257_a((IFixType) FixTypes.PLAYER, nbttagcompound));
        }

        return nbttagcompound;
    }

    // CraftBukkit start
    public NBTTagCompound getPlayerData(String s) {
        try {
            File file1 = new File(this.field_75771_c, s + ".dat");

            if (file1.exists()) {
                return CompressedStreamTools.func_74796_a((InputStream) (new FileInputStream(file1)));
            }
        } catch (Exception exception) {
            field_151478_a.warn("Failed to load player data for " + s);
        }

        return null;
    }
    // CraftBukkit end

    public IPlayerFileData func_75756_e() {
        return this;
    }

    public String[] func_75754_f() {
        String[] astring = this.field_75771_c.list();

        if (astring == null) {
            astring = new String[0];
        }

        for (int i = 0; i < astring.length; ++i) {
            if (astring[i].endsWith(".dat")) {
                astring[i] = astring[i].substring(0, astring[i].length() - 4);
            }
        }

        return astring;
    }

    public void func_75759_a() {}

    public File func_75758_b(String s) {
        return new File(this.field_75768_d, s + ".dat");
    }

    public TemplateManager func_186340_h() {
        return this.field_186342_h;
    }

    // CraftBukkit start
    public UUID getUUID() {
        if (uuid != null) return uuid;
        File file1 = new File(this.field_75770_b, "uid.dat");
        if (file1.exists()) {
            DataInputStream dis = null;
            try {
                dis = new DataInputStream(new FileInputStream(file1));
                return uuid = new UUID(dis.readLong(), dis.readLong());
            } catch (IOException ex) {
                field_151478_a.warn("Failed to read " + file1 + ", generating new random UUID", ex);
            } finally {
                if (dis != null) {
                    try {
                        dis.close();
                    } catch (IOException ex) {
                        // NOOP
                    }
                }
            }
        }
        uuid = UUID.randomUUID();
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(new FileOutputStream(file1));
            dos.writeLong(uuid.getMostSignificantBits());
            dos.writeLong(uuid.getLeastSignificantBits());
        } catch (IOException ex) {
            field_151478_a.warn("Failed to write " + file1, ex);
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException ex) {
                    // NOOP
                }
            }
        }
        return uuid;
    }

    public File getPlayerDir() {
        return field_75771_c;
    }
    // CraftBukkit end
}
