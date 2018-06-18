package net.minecraft.item;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ItemSkull extends Item {

    private static final String[] SKULL_TYPES = new String[] { "skeleton", "wither", "zombie", "char", "creeper", "dragon"};

    public ItemSkull() {
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public EnumActionResult onItemUse(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (enumdirection == EnumFacing.DOWN) {
            return EnumActionResult.FAIL;
        } else {
            IBlockState iblockdata = world.getBlockState(blockposition);
            Block block = iblockdata.getBlock();
            boolean flag = block.isReplaceable((IBlockAccess) world, blockposition);

            if (!flag) {
                if (!world.getBlockState(blockposition).getMaterial().isSolid()) {
                    return EnumActionResult.FAIL;
                }

                blockposition = blockposition.offset(enumdirection);
            }

            ItemStack itemstack = entityhuman.getHeldItem(enumhand);

            if (entityhuman.canPlayerEdit(blockposition, enumdirection, itemstack) && Blocks.SKULL.canPlaceBlockAt(world, blockposition)) {
                if (world.isRemote) {
                    return EnumActionResult.SUCCESS;
                } else {
                    // Spigot Start
                    if ( !Blocks.SKULL.canPlaceBlockAt( world, blockposition ) )
                    {
                        return EnumActionResult.FAIL;
                    }
                    // Spigot End
                    world.setBlockState(blockposition, Blocks.SKULL.getDefaultState().withProperty(BlockSkull.FACING, enumdirection), 11);
                    int i = 0;

                    if (enumdirection == EnumFacing.UP) {
                        i = MathHelper.floor((double) (entityhuman.rotationYaw * 16.0F / 360.0F) + 0.5D) & 15;
                    }

                    TileEntity tileentity = world.getTileEntity(blockposition);

                    if (tileentity instanceof TileEntitySkull) {
                        TileEntitySkull tileentityskull = (TileEntitySkull) tileentity;

                        if (itemstack.getMetadata() == 3) {
                            GameProfile gameprofile = null;

                            if (itemstack.hasTagCompound()) {
                                NBTTagCompound nbttagcompound = itemstack.getTagCompound();

                                if (nbttagcompound.hasKey("SkullOwner", 10)) {
                                    gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
                                } else if (nbttagcompound.hasKey("SkullOwner", 8) && !StringUtils.isBlank(nbttagcompound.getString("SkullOwner"))) {
                                    gameprofile = new GameProfile((UUID) null, nbttagcompound.getString("SkullOwner"));
                                }
                            }

                            tileentityskull.setPlayerProfile(gameprofile);
                        } else {
                            tileentityskull.setType(itemstack.getMetadata());
                        }

                        tileentityskull.setSkullRotation(i);
                        Blocks.SKULL.checkWitherSpawn(world, blockposition, tileentityskull);
                    }

                    if (entityhuman instanceof EntityPlayerMP) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) entityhuman, blockposition, itemstack);
                    }

                    itemstack.shrink(1);
                    return EnumActionResult.SUCCESS;
                }
            } else {
                return EnumActionResult.FAIL;
            }
        }
    }

    public void getSubItems(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.isInCreativeTab(creativemodetab)) {
            for (int i = 0; i < ItemSkull.SKULL_TYPES.length; ++i) {
                nonnulllist.add(new ItemStack(this, 1, i));
            }
        }

    }

    public int getMetadata(int i) {
        return i;
    }

    public String getUnlocalizedName(ItemStack itemstack) {
        int i = itemstack.getMetadata();

        if (i < 0 || i >= ItemSkull.SKULL_TYPES.length) {
            i = 0;
        }

        return super.getUnlocalizedName() + "." + ItemSkull.SKULL_TYPES[i];
    }

    public String getItemStackDisplayName(ItemStack itemstack) {
        if (itemstack.getMetadata() == 3 && itemstack.hasTagCompound()) {
            if (itemstack.getTagCompound().hasKey("SkullOwner", 8)) {
                return I18n.translateToLocalFormatted("item.skull.player.name", new Object[] { itemstack.getTagCompound().getString("SkullOwner")});
            }

            if (itemstack.getTagCompound().hasKey("SkullOwner", 10)) {
                NBTTagCompound nbttagcompound = itemstack.getTagCompound().getCompoundTag("SkullOwner");

                if (nbttagcompound.hasKey("Name", 8)) {
                    return I18n.translateToLocalFormatted("item.skull.player.name", new Object[] { nbttagcompound.getString("Name")});
                }
            }
        }

        return super.getItemStackDisplayName(itemstack);
    }

    public boolean updateItemStackNBT(final NBTTagCompound nbttagcompound) { // Spigot - make final
        super.updateItemStackNBT(nbttagcompound);
        if (nbttagcompound.hasKey("SkullOwner", 8) && !StringUtils.isBlank(nbttagcompound.getString("SkullOwner"))) {
            GameProfile gameprofile = new GameProfile((UUID) null, nbttagcompound.getString("SkullOwner"));

            // Spigot start
            TileEntitySkull.b(gameprofile, new com.google.common.base.Predicate<GameProfile>() {

                @Override
                public boolean apply(GameProfile gameprofile) {
                    nbttagcompound.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
                    return false;
                }
            }, false);
            // Spigot end
            return true;
        } else {
            // CraftBukkit start
            NBTTagList textures = nbttagcompound.getCompoundTag("SkullOwner").getCompoundTag("Properties").getTagList("textures", 10); // Safe due to method contracts
            for (int i = 0; i < textures.tagCount(); i++) {
                if (textures.getCompoundTagAt(i) instanceof NBTTagCompound && !((NBTTagCompound) textures.getCompoundTagAt(i)).hasKey("Signature", 8) && ((NBTTagCompound) textures.getCompoundTagAt(i)).getString("Value").trim().isEmpty()) {
                    nbttagcompound.removeTag("SkullOwner");
                    break;
                }
            }
            // CraftBukkit end
            return false;
        }
    }
}
