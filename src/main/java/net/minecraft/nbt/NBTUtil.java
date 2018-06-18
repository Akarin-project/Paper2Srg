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

    private static final Logger LOGGER = LogManager.getLogger();

    @Nullable
    public static GameProfile readGameProfileFromNBT(NBTTagCompound nbttagcompound) {
        String s = null;
        String s1 = null;

        if (nbttagcompound.hasKey("Name", 8)) {
            s = nbttagcompound.getString("Name");
        }

        if (nbttagcompound.hasKey("Id", 8)) {
            s1 = nbttagcompound.getString("Id");
        }

        try {
            UUID uuid;

            try {
                uuid = UUID.fromString(s1);
            } catch (Throwable throwable) {
                uuid = null;
            }

            GameProfile gameprofile = new GameProfile(uuid, s);

            if (nbttagcompound.hasKey("Properties", 10)) {
                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Properties");
                Iterator iterator = nbttagcompound1.getKeySet().iterator();

                while (iterator.hasNext()) {
                    String s2 = (String) iterator.next();
                    NBTTagList nbttaglist = nbttagcompound1.getTagList(s2, 10);

                    for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                        NBTTagCompound nbttagcompound2 = nbttaglist.getCompoundTagAt(i);
                        String s3 = nbttagcompound2.getString("Value");

                        if (nbttagcompound2.hasKey("Signature", 8)) {
                            gameprofile.getProperties().put(s2, new Property(s2, s3, nbttagcompound2.getString("Signature")));
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

    public static NBTTagCompound writeGameProfile(NBTTagCompound nbttagcompound, GameProfile gameprofile) {
        if (!StringUtils.isNullOrEmpty(gameprofile.getName())) {
            nbttagcompound.setString("Name", gameprofile.getName());
        }

        if (gameprofile.getId() != null) {
            nbttagcompound.setString("Id", gameprofile.getId().toString());
        }

        if (!gameprofile.getProperties().isEmpty()) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            Iterator iterator = gameprofile.getProperties().keySet().iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();
                NBTTagList nbttaglist = new NBTTagList();

                NBTTagCompound nbttagcompound2;

                for (Iterator iterator1 = gameprofile.getProperties().get(s).iterator(); iterator1.hasNext(); nbttaglist.appendTag(nbttagcompound2)) {
                    Property property = (Property) iterator1.next();

                    nbttagcompound2 = new NBTTagCompound();
                    nbttagcompound2.setString("Value", property.getValue());
                    if (property.hasSignature()) {
                        nbttagcompound2.setString("Signature", property.getSignature());
                    }
                }

                nbttagcompound1.setTag(s, nbttaglist);
            }

            nbttagcompound.setTag("Properties", nbttagcompound1);
        }

        return nbttagcompound;
    }

    @VisibleForTesting
    public static boolean areNBTEquals(NBTBase nbtbase, NBTBase nbtbase1, boolean flag) {
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
            Iterator iterator = nbttagcompound.getKeySet().iterator();

            String s;
            NBTBase nbtbase2;

            do {
                if (!iterator.hasNext()) {
                    return true;
                }

                s = (String) iterator.next();
                nbtbase2 = nbttagcompound.getTag(s);
            } while (areNBTEquals(nbtbase2, nbttagcompound1.getTag(s), flag));

            return false;
        } else if (nbtbase instanceof NBTTagList && flag) {
            NBTTagList nbttaglist = (NBTTagList) nbtbase;
            NBTTagList nbttaglist1 = (NBTTagList) nbtbase1;

            if (nbttaglist.hasNoTags()) {
                return nbttaglist1.hasNoTags();
            } else {
                int i = 0;

                while (i < nbttaglist.tagCount()) {
                    NBTBase nbtbase3 = nbttaglist.get(i);
                    boolean flag1 = false;
                    int j = 0;

                    while (true) {
                        if (j < nbttaglist1.tagCount()) {
                            if (!areNBTEquals(nbtbase3, nbttaglist1.get(j), flag)) {
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

    public static NBTTagCompound createUUIDTag(UUID uuid) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.setLong("M", uuid.getMostSignificantBits());
        nbttagcompound.setLong("L", uuid.getLeastSignificantBits());
        return nbttagcompound;
    }

    public static UUID getUUIDFromTag(NBTTagCompound nbttagcompound) {
        return new UUID(nbttagcompound.getLong("M"), nbttagcompound.getLong("L"));
    }

    public static BlockPos getPosFromTag(NBTTagCompound nbttagcompound) {
        return new BlockPos(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Y"), nbttagcompound.getInteger("Z"));
    }

    public static NBTTagCompound createPosTag(BlockPos blockposition) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.setInteger("X", blockposition.getX());
        nbttagcompound.setInteger("Y", blockposition.getY());
        nbttagcompound.setInteger("Z", blockposition.getZ());
        return nbttagcompound;
    }

    public static IBlockState readBlockState(NBTTagCompound nbttagcompound) {
        if (!nbttagcompound.hasKey("Name", 8)) {
            return Blocks.AIR.getDefaultState();
        } else {
            Block block = (Block) Block.REGISTRY.getObject(new ResourceLocation(nbttagcompound.getString("Name")));
            IBlockState iblockdata = block.getDefaultState();

            if (nbttagcompound.hasKey("Properties", 10)) {
                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Properties");
                BlockStateContainer blockstatelist = block.getBlockState();
                Iterator iterator = nbttagcompound1.getKeySet().iterator();

                while (iterator.hasNext()) {
                    String s = (String) iterator.next();
                    IProperty iblockstate = blockstatelist.getProperty(s);

                    if (iblockstate != null) {
                        iblockdata = setValueHelper(iblockdata, iblockstate, s, nbttagcompound1, nbttagcompound);
                    }
                }
            }

            return iblockdata;
        }
    }

    private static <T extends Comparable<T>> IBlockState setValueHelper(IBlockState iblockdata, IProperty<T> iblockstate, String s, NBTTagCompound nbttagcompound, NBTTagCompound nbttagcompound1) {
        Optional optional = iblockstate.parseValue(nbttagcompound.getString(s));

        if (optional.isPresent()) {
            return iblockdata.withProperty(iblockstate, (Comparable) optional.get());
        } else {
            NBTUtil.LOGGER.warn("Unable to read property: {} with value: {} for blockstate: {}", s, nbttagcompound.getString(s), nbttagcompound1.toString());
            return iblockdata;
        }
    }

    public static NBTTagCompound writeBlockState(NBTTagCompound nbttagcompound, IBlockState iblockdata) {
        nbttagcompound.setString("Name", ((ResourceLocation) Block.REGISTRY.getNameForObject(iblockdata.getBlock())).toString());
        if (!iblockdata.getProperties().isEmpty()) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            UnmodifiableIterator unmodifiableiterator = iblockdata.getProperties().entrySet().iterator();

            while (unmodifiableiterator.hasNext()) {
                Entry entry = (Entry) unmodifiableiterator.next();
                IProperty iblockstate = (IProperty) entry.getKey();

                nbttagcompound1.setString(iblockstate.getName(), getName(iblockstate, (Comparable) entry.getValue()));
            }

            nbttagcompound.setTag("Properties", nbttagcompound1);
        }

        return nbttagcompound;
    }

    private static <T extends Comparable<T>> String getName(IProperty<T> iblockstate, Comparable<?> comparable) {
        return iblockstate.getName(comparable);
    }
}
