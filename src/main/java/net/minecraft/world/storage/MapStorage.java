package net.minecraft.world.storage;

import com.destroystokyo.paper.exception.ServerInternalException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagShort;

public class MapStorage {

    private final ISaveHandler saveHandler;
    protected Map<String, WorldSavedData> loadedDataMap = Maps.newHashMap();
    public final List<WorldSavedData> loadedDataList = Lists.newArrayList(); // Spigot
    private final Map<String, Short> idCounts = Maps.newHashMap();

    public MapStorage(ISaveHandler idatamanager) {
        this.saveHandler = idatamanager;
        this.loadIdCounts();
    }

    @Nullable
    public WorldSavedData getOrLoadData(Class<? extends WorldSavedData> oclass, String s) {
        WorldSavedData persistentbase = (WorldSavedData) this.loadedDataMap.get(s);

        if (persistentbase != null) {
            return persistentbase;
        } else {
            if (this.saveHandler != null) {
                try {
                    File file = this.saveHandler.getMapFileFromName(s);

                    if (file != null && file.exists()) {
                        try {
                            persistentbase = (WorldSavedData) oclass.getConstructor(new Class[] { String.class}).newInstance(new Object[] { s});
                        } catch (Exception exception) {
                            throw new RuntimeException("Failed to instantiate " + oclass, exception);
                        }

                        FileInputStream fileinputstream = new FileInputStream(file);
                        NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed((InputStream) fileinputstream);

                        fileinputstream.close();
                        persistentbase.readFromNBT(nbttagcompound.getCompoundTag("data"));
                    }
                } catch (Exception exception1) {
                    exception1.printStackTrace();
                    ServerInternalException.reportInternalException(exception1); // Paper
                }
            }

            if (persistentbase != null) {
                this.loadedDataMap.put(s, persistentbase);
                this.loadedDataList.add(persistentbase);
            }

            return persistentbase;
        }
    }

    public void setData(String s, WorldSavedData persistentbase) {
        if (this.loadedDataMap.containsKey(s)) {
            this.loadedDataList.remove(this.loadedDataMap.remove(s));
        }

        this.loadedDataMap.put(s, persistentbase);
        this.loadedDataList.add(persistentbase);
    }

    public void saveAllData() {
        for (int i = 0; i < this.loadedDataList.size(); ++i) {
            WorldSavedData persistentbase = (WorldSavedData) this.loadedDataList.get(i);

            if (persistentbase.isDirty()) {
                this.saveData(persistentbase);
                persistentbase.setDirty(false);
            }
        }

    }

    private void saveData(WorldSavedData persistentbase) {
        if (this.saveHandler != null) {
            try {
                File file = this.saveHandler.getMapFileFromName(persistentbase.mapName);

                if (file != null) {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();

                    nbttagcompound.setTag("data", persistentbase.writeToNBT(new NBTTagCompound()));
                    FileOutputStream fileoutputstream = new FileOutputStream(file);

                    CompressedStreamTools.writeCompressed(nbttagcompound, (OutputStream) fileoutputstream);
                    fileoutputstream.close();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                ServerInternalException.reportInternalException(exception); // Paper
            }

        }
    }

    private void loadIdCounts() {
        try {
            this.idCounts.clear();
            if (this.saveHandler == null) {
                return;
            }

            File file = this.saveHandler.getMapFileFromName("idcounts");

            if (file != null && file.exists()) {
                DataInputStream datainputstream = new DataInputStream(new FileInputStream(file));
                NBTTagCompound nbttagcompound = CompressedStreamTools.read(datainputstream);

                datainputstream.close();
                Iterator iterator = nbttagcompound.getKeySet().iterator();

                while (iterator.hasNext()) {
                    String s = (String) iterator.next();
                    NBTBase nbtbase = nbttagcompound.getTag(s);

                    if (nbtbase instanceof NBTTagShort) {
                        NBTTagShort nbttagshort = (NBTTagShort) nbtbase;
                        short short0 = nbttagshort.getShort();

                        this.idCounts.put(s, Short.valueOf(short0));
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public int getUniqueDataId(String s) {
        Short oshort = (Short) this.idCounts.get(s);

        if (oshort == null) {
            oshort = Short.valueOf((short) 0);
        } else {
            oshort = Short.valueOf((short) (oshort.shortValue() + 1));
        }

        this.idCounts.put(s, oshort);
        if (this.saveHandler == null) {
            return oshort.shortValue();
        } else {
            try {
                File file = this.saveHandler.getMapFileFromName("idcounts");

                if (file != null) {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    Iterator iterator = this.idCounts.keySet().iterator();

                    while (iterator.hasNext()) {
                        String s1 = (String) iterator.next();

                        nbttagcompound.setShort(s1, ((Short) this.idCounts.get(s1)).shortValue());
                    }

                    DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file));

                    CompressedStreamTools.write(nbttagcompound, (DataOutput) dataoutputstream);
                    dataoutputstream.close();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            return oshort.shortValue();
        }
    }
}
