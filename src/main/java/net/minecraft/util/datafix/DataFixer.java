package net.minecraft.util.datafix;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Util;

public class DataFixer implements IDataFixer {

    private static final Logger LOGGER = LogManager.getLogger();
    private final Map<IFixType, List<IDataWalker>> walkerMap = Maps.newHashMap();
    private final Map<IFixType, List<IFixableData>> fixMap = Maps.newHashMap();
    private final int version;

    public DataFixer(int i) {
        this.version = i;
    }

    public NBTTagCompound process(IFixType dataconvertertype, NBTTagCompound nbttagcompound) {
        int i = nbttagcompound.hasKey("DataVersion", 99) ? nbttagcompound.getInteger("DataVersion") : -1;

        return i >= 1343 ? nbttagcompound : this.process(dataconvertertype, nbttagcompound, i);
    }

    public NBTTagCompound process(IFixType dataconvertertype, NBTTagCompound nbttagcompound, int i) {
        if (i < this.version) {
            nbttagcompound = this.processFixes(dataconvertertype, nbttagcompound, i);
            nbttagcompound = this.processWalkers(dataconvertertype, nbttagcompound, i);
        }

        return nbttagcompound;
    }

    private NBTTagCompound processFixes(IFixType dataconvertertype, NBTTagCompound nbttagcompound, int i) {
        List list = (List) this.fixMap.get(dataconvertertype);

        if (list != null) {
            for (int j = 0; j < list.size(); ++j) {
                IFixableData idataconverter = (IFixableData) list.get(j);

                if (idataconverter.getFixVersion() > i) {
                    nbttagcompound = idataconverter.fixTagCompound(nbttagcompound);
                }
            }
        }

        return nbttagcompound;
    }

    private NBTTagCompound processWalkers(IFixType dataconvertertype, NBTTagCompound nbttagcompound, int i) {
        List list = (List) this.walkerMap.get(dataconvertertype);

        if (list != null) {
            for (int j = 0; j < list.size(); ++j) {
                nbttagcompound = ((IDataWalker) list.get(j)).process(this, nbttagcompound, i);
            }
        }

        return nbttagcompound;
    }

    public void registerWalker(FixTypes dataconvertertypes, IDataWalker datainspector) {
        this.registerVanillaWalker((IFixType) dataconvertertypes, datainspector);
    }

    public void registerVanillaWalker(IFixType dataconvertertype, IDataWalker datainspector) {
        this.getTypeList(this.walkerMap, dataconvertertype).add(datainspector);
    }

    public void registerFix(IFixType dataconvertertype, IFixableData idataconverter) {
        List list = this.getTypeList(this.fixMap, dataconvertertype);
        int i = idataconverter.getFixVersion();

        if (i > this.version) {
            DataFixer.LOGGER.warn("Ignored fix registered for version: {} as the DataVersion of the game is: {}", Integer.valueOf(i), Integer.valueOf(this.version));
        } else {
            if (!list.isEmpty() && ((IFixableData) Util.getLastElement(list)).getFixVersion() > i) {
                for (int j = 0; j < list.size(); ++j) {
                    if (((IFixableData) list.get(j)).getFixVersion() > i) {
                        list.add(j, idataconverter);
                        break;
                    }
                }
            } else {
                list.add(idataconverter);
            }

        }
    }

    private <V> List<V> getTypeList(Map<IFixType, List<V>> map, IFixType dataconvertertype) {
        Object object = (List) map.get(dataconvertertype);

        if (object == null) {
            object = Lists.newArrayList();
            map.put(dataconvertertype, object);
        }

        return (List) object;
    }
}
