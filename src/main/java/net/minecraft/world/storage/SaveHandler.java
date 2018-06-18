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

    private static final Logger LOGGER = LogManager.getLogger();
    private final File worldDirectory;
    private final File playersDirectory;
    private final File mapDataDir;
    private final long initializationTime = MinecraftServer.getCurrentTimeMillis();
    private final String saveDirectoryName;
    private final TemplateManager structureTemplateManager;
    protected final DataFixer dataFixer;
    private UUID uuid = null; // CraftBukkit

    public SaveHandler(File file, String s, boolean flag, DataFixer dataconvertermanager) {
        this.dataFixer = dataconvertermanager;
        this.worldDirectory = new File(file, s);
        this.worldDirectory.mkdirs();
        this.playersDirectory = new File(this.worldDirectory, "playerdata");
        this.mapDataDir = new File(this.worldDirectory, "data");
        this.mapDataDir.mkdirs();
        this.saveDirectoryName = s;
        if (flag) {
            this.playersDirectory.mkdirs();
            this.structureTemplateManager = new TemplateManager((new File(this.worldDirectory, "structures")).toString(), dataconvertermanager);
        } else {
            this.structureTemplateManager = null;
        }

        this.setSessionLock();
    }

    private void setSessionLock() {
        try {
            File file = new File(this.worldDirectory, "session.lock");
            DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file));

            try {
                dataoutputstream.writeLong(this.initializationTime);
            } finally {
                dataoutputstream.close();
            }

        } catch (IOException ioexception) {
            ioexception.printStackTrace();
            throw new RuntimeException("Failed to check session lock for world located at " + this.worldDirectory + ", aborting. Stop the server and delete the session.lock in this world to prevent further issues."); // Spigot
        }
    }

    public File getWorldDirectory() {
        return this.worldDirectory;
    }

    public void checkSessionLock() throws MinecraftException {
        try {
            File file = new File(this.worldDirectory, "session.lock");
            DataInputStream datainputstream = new DataInputStream(new FileInputStream(file));

            try {
                if (datainputstream.readLong() != this.initializationTime) {
                    throw new MinecraftException("The save for world located at " + this.worldDirectory + " is being accessed from another location, aborting");  // Spigot
                }
            } finally {
                datainputstream.close();
            }

        } catch (IOException ioexception) {
            throw new MinecraftException("Failed to check session lock for world located at " + this.worldDirectory + ", aborting. Stop the server and delete the session.lock in this world to prevent further issues."); // Spigot
        }
    }

    public IChunkLoader getChunkLoader(WorldProvider worldprovider) {
        throw new RuntimeException("Old Chunk Storage is no longer supported.");
    }

    @Nullable
    public WorldInfo loadWorldInfo() {
        File file = new File(this.worldDirectory, "level.dat");

        if (file.exists()) {
            WorldInfo worlddata = SaveFormatOld.getWorldData(file, this.dataFixer);

            if (worlddata != null) {
                return worlddata;
            }
        }

        file = new File(this.worldDirectory, "level.dat_old");
        return file.exists() ? SaveFormatOld.getWorldData(file, this.dataFixer) : null;
    }

    public void saveWorldInfoWithPlayer(WorldInfo worlddata, @Nullable NBTTagCompound nbttagcompound) {
        NBTTagCompound nbttagcompound1 = worlddata.cloneNBTCompound(nbttagcompound);
        NBTTagCompound nbttagcompound2 = new NBTTagCompound();

        nbttagcompound2.setTag("Data", nbttagcompound1);

        try {
            File file = new File(this.worldDirectory, "level.dat_new");
            File file1 = new File(this.worldDirectory, "level.dat_old");
            File file2 = new File(this.worldDirectory, "level.dat");

            CompressedStreamTools.writeCompressed(nbttagcompound2, (OutputStream) (new FileOutputStream(file)));
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

    public void saveWorldInfo(WorldInfo worlddata) {
        this.saveWorldInfoWithPlayer(worlddata, (NBTTagCompound) null);
    }

    public void writePlayerData(EntityPlayer entityhuman) {
        if(!com.destroystokyo.paper.PaperConfig.savePlayerData) return; // Paper - Make player data saving configurable
        try {
            NBTTagCompound nbttagcompound = entityhuman.writeToNBT(new NBTTagCompound());
            File file = new File(this.playersDirectory, entityhuman.getCachedUniqueIdString() + ".dat.tmp");
            File file1 = new File(this.playersDirectory, entityhuman.getCachedUniqueIdString() + ".dat");

            CompressedStreamTools.writeCompressed(nbttagcompound, (OutputStream) (new FileOutputStream(file)));
            if (file1.exists()) {
                file1.delete();
            }

            file.renameTo(file1);
        } catch (Exception exception) {
            SaveHandler.LOGGER.error("Failed to save player data for {}", entityhuman.getName(), exception); // Paper
        }

    }

    @Nullable
    public NBTTagCompound readPlayerData(EntityPlayer entityhuman) {
        NBTTagCompound nbttagcompound = null;

        try {
            File file = new File(this.playersDirectory, entityhuman.getCachedUniqueIdString() + ".dat");
            // Spigot Start
            boolean usingWrongFile = false;
            if ( org.bukkit.Bukkit.getOnlineMode() && !file.exists() ) // Paper - Check online mode first
            {
                file = new File( this.playersDirectory, UUID.nameUUIDFromBytes( ( "OfflinePlayer:" + entityhuman.getName() ).getBytes( "UTF-8" ) ).toString() + ".dat");
                if ( file.exists() )
                {
                    usingWrongFile = true;
                    org.bukkit.Bukkit.getServer().getLogger().warning( "Using offline mode UUID file for player " + entityhuman.getName() + " as it is the only copy we can find." );
                }
            }
            // Spigot End

            if (file.exists() && file.isFile()) {
                nbttagcompound = CompressedStreamTools.readCompressed((InputStream) (new FileInputStream(file)));
            }
            // Spigot Start
            if ( usingWrongFile )
            {
                file.renameTo( new File( file.getPath() + ".offline-read" ) );
            }
            // Spigot End
        } catch (Exception exception) {
            SaveHandler.LOGGER.warn("Failed to load player data for {}", entityhuman.getName());
        }

        if (nbttagcompound != null) {
            // CraftBukkit start
            if (entityhuman instanceof EntityPlayerMP) {
                CraftPlayer player = (CraftPlayer) entityhuman.getBukkitEntity();
                // Only update first played if it is older than the one we have
                long modified = new File(this.playersDirectory, entityhuman.getUniqueID().toString() + ".dat").lastModified();
                if (modified < player.getFirstPlayed()) {
                    player.setFirstPlayed(modified);
                }
            }
            // CraftBukkit end
            entityhuman.readFromNBT(this.dataFixer.process((IFixType) FixTypes.PLAYER, nbttagcompound));
        }

        return nbttagcompound;
    }

    // CraftBukkit start
    public NBTTagCompound getPlayerData(String s) {
        try {
            File file1 = new File(this.playersDirectory, s + ".dat");

            if (file1.exists()) {
                return CompressedStreamTools.readCompressed((InputStream) (new FileInputStream(file1)));
            }
        } catch (Exception exception) {
            LOGGER.warn("Failed to load player data for " + s);
        }

        return null;
    }
    // CraftBukkit end

    public IPlayerFileData getPlayerNBTManager() {
        return this;
    }

    public String[] getAvailablePlayerDat() {
        String[] astring = this.playersDirectory.list();

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

    public void flush() {}

    public File getMapFileFromName(String s) {
        return new File(this.mapDataDir, s + ".dat");
    }

    public TemplateManager getStructureTemplateManager() {
        return this.structureTemplateManager;
    }

    // CraftBukkit start
    public UUID getUUID() {
        if (uuid != null) return uuid;
        File file1 = new File(this.worldDirectory, "uid.dat");
        if (file1.exists()) {
            DataInputStream dis = null;
            try {
                dis = new DataInputStream(new FileInputStream(file1));
                return uuid = new UUID(dis.readLong(), dis.readLong());
            } catch (IOException ex) {
                LOGGER.warn("Failed to read " + file1 + ", generating new random UUID", ex);
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
            LOGGER.warn("Failed to write " + file1, ex);
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
        return playersDirectory;
    }
    // CraftBukkit end
}
