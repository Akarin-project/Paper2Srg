package net.minecraft.nbt;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.Iterator;
import java.util.UUID;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;

public final class NBTUtil {

    private static final Logger field_193591_a = LogManager.getLogger();

    @Nullable
    public static GameProfile func_152459_a(NBTTagCompound nbttagcompound) {
        String s = null;
        String s1 = null;

        if (nbttagcompound.func_150297_b("Name", 8)) {
            s = nbttagcompound.func_74779_i("Name");
        }

        if (nbttagcompound.func_150297_b("Id", 8)) {
            s1 = nbttagcompound.func_74779_i("Id");
        }

        try {
            UUID uuid;

            try {
                uuid = UUID.fromString(s1);
            } catch (Throwable throwable) {
                uuid = null;
            }

            GameProfile gameprofile = new GameProfile(uuid, s);

            if (nbttagcompound.func_150297_b("Properties", 10)) {
                NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("Properties");
                Iterator iterator = nbttagcompound1.func_150296_c().iterator();

                while (iterator.hasNext()) {
                    String s2 = (String) iterator.next();
                    NBTTagList nbttaglist = nbttagcompound1.func_150295_c(s2, 10);

                    for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
                        NBTTagCompound nbttagcompound2 = nbttaglist.func_150305_b(i);
                        String s3 = nbttagcompound2.func_74779_i("Value");

                        if (nbttagcompound2.func_150297_b("Signature", 8)) {
                            gameprofile.getProperties().put(s2, new Property(s2, s3, nbttagcompound2.func_74779_i("Signature")));
                        } else {
                            gameprofile.getProperties().put(s2, new Property(s2, s3));
                        }
                    }
                }
            }

            return gameprofile;
        } catch (Throwable throwable1) {
            return null;
        }
    }

    public static NBTTagCompound func_180708_a(NBTTagCompound nbttagcompound, GameProfile gameprofile) {
        if (!StringUtils.func_151246_b(gameprofile.getName())) {
            nbttagcompound.func_74778_a("Name", gameprofile.getName());
        }

        if (gameprofile.getId() != null) {
            nbttagcompound.func_74778_a("Id", gameprofile.getId().toString());
        }

        if (!gameprofile.getProperties().isEmpty()) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            Iterator iterator = gameprofile.getProperties().keySet().iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();
                NBTTagList nbttaglist = new NBTTagList();

                NBTTagCompound nbttagcompound2;

                for (Iterator iterator1 = gameprofile.getProperties().get(s).iterator(); iterator1.hasNext(); nbttaglist.func_74742_a(nbttagcompound2)) {
                    Property property = (Property) iterator1.next();

                    nbttagcompound2 = new NBTTagCompound();
                    nbttagcompound2.func_74778_a("Value", property.getValue());
                    if (property.hasSignature()) {
                        nbttagcompound2.func_74778_a("Signature", property.getSignature());
                    }
                }

                nbttagcompound1.func_74782_a(s, nbttaglist);
            }

            nbttagcompound.func_74782_a("Properties", nbttagcompound1);
        }

        return nbttagcompound;
    }

    @VisibleForTesting
    public static boolean func_181123_a(NBTBase nbtbase, NBTBase nbtbase1, boolean flag) {
        if (nbtbase == nbtbase1) {
            return true;
        } else if (nbtbase == null) {
            return true;
        } else if (nbtbase1 == null) {
            return false;
        } else if (!nbtbase.getClass().equals(nbtbase1.getClass())) {
            return false;
        } else if (nbtbase instanceof NBTTagCompound) {
            NBTTagCompound nbttagcompound = (NBTTagCompound) nbtbase;
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbtbase1;
            Iterator iterator = nbttagcompound.func_150296_c().iterator();

            String s;
            NBTBase nbtbase2;

            do {
                if (!iterator.hasNext()) {
                    return true;
                }

                s = (String) iterator.next();
                nbtbase2 = nbttagcompound.func_74781_a(s);
            } while (func_181123_a(nbtbase2, nbttagcompound1.func_74781_a(s), flag));

            return false;
        } else if (nbtbase instanceof NBTTagList && flag) {
            NBTTagList nbttaglist = (NBTTagList) nbtbase;
            NBTTagList nbttaglist1 = (NBTTagList) nbtbase1;

            if (nbttaglist.func_82582_d()) {
                return nbttaglist1.func_82582_d();
            } else {
                int i = 0;

                while (i < nbttaglist.func_74745_c()) {
                    NBTBase nbtbase3 = nbttaglist.func_179238_g(i);
                    boolean flag1 = false;
                    int j = 0;

                    while (true) {
                        if (j < nbttaglist1.func_74745_c()) {
                            if (!func_181123_a(nbtbase3, nbttaglist1.func_179238_g(j), flag)) {
                                ++j;
                                continue;
                            }

                            flag1 = true;
                        }

                        if (!flag1) {
                            return false;
                        }

                        ++i;
                        break;
                    }
                }

                return true;
            }
        } else {
            return nbtbase.equals(nbtbase1);
        }
    }

    public static NBTTagCompound func_186862_a(UUID uuid) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.func_74772_a("M", uuid.getMostSignificantBits());
        nbttagcompound.func_74772_a("L", uuid.getLeastSignificantBits());
        return nbttagcompound;
    }

    public static UUID func_186860_b(NBTTagCompound nbttagcompound) {
        return new UUID(nbttagcompound.func_74763_f("M"), nbttagcompound.func_74763_f("L"));
    }

    public static BlockPos func_186861_c(NBTTagCompound nbttagcompound) {
        return new BlockPos(nbttagcompound.func_74762_e("X"), nbttagcompound.func_74762_e("Y"), nbttagcompound.func_74762_e("Z"));
    }

    public static NBTTagCompound func_186859_a(BlockPos blockposition) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.func_74768_a("X", blockposition.func_177958_n());
        nbttagcompound.func_74768_a("Y", blockposition.func_177956_o());
        nbttagcompound.func_74768_a("Z", blockposition.func_177952_p());
        return nbttagcompound;
    }

    public static IBlockState func_190008_d(NBTTagCompound nbttagcompound) {
        if (!nbttagcompound.func_150297_b("Name", 8)) {
            return Blocks.field_150350_a.func_176223_P();
        } else {
            Block block = Block.field_149771_c.func_82594_a(new ResourceLocation(nbttagcompound.func_74779_i("Name")));
            IBlockState iblockdata = block.func_176223_P();

            if (nbttagcompound.func_150297_b("Properties", 10)) {
                NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("Properties");
                BlockStateContainer blockstatelist = block.func_176194_O();
                Iterator iterator = nbttagcompound1.func_150296_c().iterator();

                while (iterator.hasNext()) {
                    String s = (String) iterator.next();
                    IProperty iblockstate = blockstatelist.func_185920_a(s);

                    if (iblockstate != null) {
                        iblockdata = func_193590_a(iblockdata, iblockstate, s, nbttagcompound1, nbttagcompound);
                    }
                }
            }

            return iblockdata;
        }
    }

    private static <T extends Comparable<T>> IBlockState func_193590_a(IBlockState iblockdata, IProperty<T> iblockstate, String s, NBTTagCompound nbttagcompound, NBTTagCompound nbttagcompound1) {
        Optional<T> optional = iblockstate.func_185929_b(nbttagcompound.func_74779_i(s));

        if (optional.isPresent()) {
            return iblockdata.func_177226_a(iblockstate, optional.get());
        } else {
            NBTUtil.field_193591_a.warn("Unable to read property: {} with value: {} for blockstate: {}", s, nbttagcompound.func_74779_i(s), nbttagcompound1.toString());
            return iblockdata;
        }
    }

    public static NBTTagCompound func_190009_a(NBTTagCompound nbttagcompound, IBlockState iblockdata) {
        nbttagcompound.func_74778_a("Name", Block.field_149771_c.func_177774_c(iblockdata.func_177230_c()).toString());
        if (!iblockdata.func_177228_b().isEmpty()) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            UnmodifiableIterator unmodifiableiterator = iblockdata.func_177228_b().entrySet().iterator();

            while (unmodifiableiterator.hasNext()) {
                Entry entry = (Entry) unmodifiableiterator.next();
                IProperty iblockstate = (IProperty) entry.getKey();

                nbttagcompound1.func_74778_a(iblockstate.func_177701_a(), func_190010_a(iblockstate, (Comparable) entry.getValue()));
            }

            nbttagcompound.func_74782_a("Properties", nbttagcompound1);
        }

        return nbttagcompound;
    }

    private static <T extends Comparable<T>> String func_190010_a(IProperty<T> iblockstate, Comparable<?> comparable) {
        return iblockstate.func_177702_a((T) comparable);
    }
}
