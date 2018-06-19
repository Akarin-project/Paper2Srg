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

    private static final Logger field_188259_a = LogManager.getLogger();
    private final Map<IFixType, List<IDataWalker>> field_188260_b = Maps.newHashMap();
    private final Map<IFixType, List<IFixableData>> field_188261_c = Maps.newHashMap();
    private final int field_188262_d;

    public DataFixer(int i) {
        this.field_188262_d = i;
    }

    public NBTTagCompound func_188257_a(IFixType dataconvertertype, NBTTagCompound nbttagcompound) {
        int i = nbttagcompound.func_150297_b("DataVersion", 99) ? nbttagcompound.func_74762_e("DataVersion") : -1;

        return i >= 1343 ? nbttagcompound : this.func_188251_a(dataconvertertype, nbttagcompound, i);
    }

    public NBTTagCompound func_188251_a(IFixType dataconvertertype, NBTTagCompound nbttagcompound, int i) {
        if (i < this.field_188262_d) {
            nbttagcompound = this.func_188252_b(dataconvertertype, nbttagcompound, i);
            nbttagcompound = this.func_188253_c(dataconvertertype, nbttagcompound, i);
        }

        return nbttagcompound;
    }

    private NBTTagCompound func_188252_b(IFixType dataconvertertype, NBTTagCompound nbttagcompound, int i) {
        List list = (List) this.field_188261_c.get(dataconvertertype);

        if (list != null) {
            for (int j = 0; j < list.size(); ++j) {
                IFixableData idataconverter = (IFixableData) list.get(j);

                if (idataconverter.func_188216_a() > i) {
                    nbttagcompound = idataconverter.func_188217_a(nbttagcompound);
                }
            }
        }

        return nbttagcompound;
    }

    private NBTTagCompound func_188253_c(IFixType dataconvertertype, NBTTagCompound nbttagcompound, int i) {
        List list = (List) this.field_188260_b.get(dataconvertertype);

        if (list != null) {
            for (int j = 0; j < list.size(); ++j) {
                nbttagcompound = ((IDataWalker) list.get(j)).func_188266_a(this, nbttagcompound, i);
            }
        }

        return nbttagcompound;
    }

    public void func_188258_a(FixTypes dataconvertertypes, IDataWalker datainspector) {
        this.func_188255_a((IFixType) dataconvertertypes, datainspector);
    }

    public void func_188255_a(IFixType dataconvertertype, IDataWalker datainspector) {
        this.func_188254_a(this.field_188260_b, dataconvertertype).add(datainspector);
    }

    public void func_188256_a(IFixType dataconvertertype, IFixableData idataconverter) {
        List list = this.func_188254_a(this.field_188261_c, dataconvertertype);
        int i = idataconverter.func_188216_a();

        if (i > this.field_188262_d) {
            DataFixer.field_188259_a.warn("Ignored fix registered for version: {} as the DataVersion of the game is: {}", Integer.valueOf(i), Integer.valueOf(this.field_188262_d));
        } else {
            if (!list.isEmpty() && ((IFixableData) Util.func_184878_a(list)).func_188216_a() > i) {
                for (int j = 0; j < list.size(); ++j) {
                    if (((IFixableData) list.get(j)).func_188216_a() > i) {
                        list.add(j, idataconverter);
                        break;
                    }
                }
            } else {
                list.add(idataconverter);
            }

        }
    }

    private <V> List<V> func_188254_a(Map<IFixType, List<V>> map, IFixType dataconvertertype) {
        Object object = (List) map.get(dataconvertertype);

        if (object == null) {
            object = Lists.newArrayList();
            map.put(dataconvertertype, object);
        }

        return (List) object;
    }
}
