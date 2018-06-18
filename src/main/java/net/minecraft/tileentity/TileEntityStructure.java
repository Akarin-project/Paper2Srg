package net.minecraft.tileentity;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStructure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class TileEntityStructure extends TileEntity {

    private String name = ""; // PAIL: rename name
    public String author = ""; // PAIL: private -> public
    public String metadata = ""; // PAIL: private -> public
    public BlockPos position = new BlockPos(0, 1, 0); // PAIL: private -> public
    public BlockPos size; // PAIL: private -> public
    public Mirror mirror; // PAIL: private -> public
    public Rotation rotation; // PAIL: private -> public
    private TileEntityStructure.Mode mode; // PAIL: rename
    public boolean ignoreEntities; // PAIL: private -> public
    private boolean powered;
    public boolean showAir; // PAIL: private -> public
    public boolean showBoundingBox; // PAIL: private -> public
    public float integrity; // PAIL: private -> public
    public long seed; // PAIL: private -> public

    public TileEntityStructure() {
        this.size = BlockPos.ORIGIN;
        this.mirror = Mirror.NONE;
        this.rotation = Rotation.NONE;
        this.mode = TileEntityStructure.Mode.DATA;
        this.ignoreEntities = true;
        this.showBoundingBox = true;
        this.integrity = 1.0F;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setString("name", this.name);
        nbttagcompound.setString("author", this.author);
        nbttagcompound.setString("metadata", this.metadata);
        nbttagcompound.setInteger("posX", this.position.getX());
        nbttagcompound.setInteger("posY", this.position.getY());
        nbttagcompound.setInteger("posZ", this.position.getZ());
        nbttagcompound.setInteger("sizeX", this.size.getX());
        nbttagcompound.setInteger("sizeY", this.size.getY());
        nbttagcompound.setInteger("sizeZ", this.size.getZ());
        nbttagcompound.setString("rotation", this.rotation.toString());
        nbttagcompound.setString("mirror", this.mirror.toString());
        nbttagcompound.setString("mode", this.mode.toString());
        nbttagcompound.setBoolean("ignoreEntities", this.ignoreEntities);
        nbttagcompound.setBoolean("powered", this.powered);
        nbttagcompound.setBoolean("showair", this.showAir);
        nbttagcompound.setBoolean("showboundingbox", this.showBoundingBox);
        nbttagcompound.setFloat("integrity", this.integrity);
        nbttagcompound.setLong("seed", this.seed);
        return nbttagcompound;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.setName(nbttagcompound.getString("name"));
        this.author = nbttagcompound.getString("author");
        this.metadata = nbttagcompound.getString("metadata");
        int i = MathHelper.clamp(nbttagcompound.getInteger("posX"), -32, 32);
        int j = MathHelper.clamp(nbttagcompound.getInteger("posY"), -32, 32);
        int k = MathHelper.clamp(nbttagcompound.getInteger("posZ"), -32, 32);

        this.position = new BlockPos(i, j, k);
        int l = MathHelper.clamp(nbttagcompound.getInteger("sizeX"), 0, 32);
        int i1 = MathHelper.clamp(nbttagcompound.getInteger("sizeY"), 0, 32);
        int j1 = MathHelper.clamp(nbttagcompound.getInteger("sizeZ"), 0, 32);

        this.size = new BlockPos(l, i1, j1);

        try {
            this.rotation = Rotation.valueOf(nbttagcompound.getString("rotation"));
        } catch (IllegalArgumentException illegalargumentexception) {
            this.rotation = Rotation.NONE;
        }

        try {
            this.mirror = Mirror.valueOf(nbttagcompound.getString("mirror"));
        } catch (IllegalArgumentException illegalargumentexception1) {
            this.mirror = Mirror.NONE;
        }

        try {
            this.mode = TileEntityStructure.Mode.valueOf(nbttagcompound.getString("mode"));
        } catch (IllegalArgumentException illegalargumentexception2) {
            this.mode = TileEntityStructure.Mode.DATA;
        }

        this.ignoreEntities = nbttagcompound.getBoolean("ignoreEntities");
        this.powered = nbttagcompound.getBoolean("powered");
        this.showAir = nbttagcompound.getBoolean("showair");
        this.showBoundingBox = nbttagcompound.getBoolean("showboundingbox");
        if (nbttagcompound.hasKey("integrity")) {
            this.integrity = nbttagcompound.getFloat("integrity");
        } else {
            this.integrity = 1.0F;
        }

        this.seed = nbttagcompound.getLong("seed");
        this.updateBlockState();
    }

    private void updateBlockState() {
        if (this.world != null) {
            BlockPos blockposition = this.getPos();
            IBlockState iblockdata = this.world.getBlockState(blockposition);

            if (iblockdata.getBlock() == Blocks.STRUCTURE_BLOCK) {
                this.world.setBlockState(blockposition, iblockdata.withProperty(BlockStructure.MODE, this.mode), 2);
            }

        }
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 7, this.getUpdateTag());
    }

    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    public boolean usedBy(EntityPlayer entityhuman) {
        if (!entityhuman.canUseCommandBlock()) {
            return false;
        } else {
            if (entityhuman.getEntityWorld().isRemote) {
                entityhuman.openEditStructure(this);
            }

            return true;
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String s) {
        String s1 = s;
        char[] achar = ChatAllowedCharacters.ILLEGAL_STRUCTURE_CHARACTERS;
        int i = achar.length;

        for (int j = 0; j < i; ++j) {
            char c0 = achar[j];

            s1 = s1.replace(c0, '_');
        }

        this.name = s1;
    }

    public void createdBy(EntityLivingBase entityliving) {
        if (!StringUtils.isNullOrEmpty(entityliving.getName())) {
            this.author = entityliving.getName();
        }

    }

    public void setPosition(BlockPos blockposition) {
        this.position = blockposition;
    }

    public void setSize(BlockPos blockposition) {
        this.size = blockposition;
    }

    public void setMirror(Mirror enumblockmirror) {
        this.mirror = enumblockmirror;
    }

    public void setRotation(Rotation enumblockrotation) {
        this.rotation = enumblockrotation;
    }

    public void setMetadata(String s) {
        this.metadata = s;
    }

    public TileEntityStructure.Mode getMode() {
        return this.mode;
    }

    public void setMode(TileEntityStructure.Mode tileentitystructure_usagemode) {
        this.mode = tileentitystructure_usagemode;
        IBlockState iblockdata = this.world.getBlockState(this.getPos());

        if (iblockdata.getBlock() == Blocks.STRUCTURE_BLOCK) {
            this.world.setBlockState(this.getPos(), iblockdata.withProperty(BlockStructure.MODE, tileentitystructure_usagemode), 2);
        }

    }

    public void setIgnoresEntities(boolean flag) {
        this.ignoreEntities = flag;
    }

    public void setIntegrity(float f) {
        this.integrity = f;
    }

    public void setSeed(long i) {
        this.seed = i;
    }

    public boolean detectSize() {
        if (this.mode != TileEntityStructure.Mode.SAVE) {
            return false;
        } else {
            BlockPos blockposition = this.getPos();
            boolean flag = true;
            BlockPos blockposition1 = new BlockPos(blockposition.getX() - 80, 0, blockposition.getZ() - 80);
            BlockPos blockposition2 = new BlockPos(blockposition.getX() + 80, 255, blockposition.getZ() + 80);
            List list = this.getNearbyCornerBlocks(blockposition1, blockposition2);
            List list1 = this.filterRelatedCornerBlocks(list);

            if (list1.size() < 1) {
                return false;
            } else {
                StructureBoundingBox structureboundingbox = this.calculateEnclosingBoundingBox(blockposition, list1);

                if (structureboundingbox.maxX - structureboundingbox.minX > 1 && structureboundingbox.maxY - structureboundingbox.minY > 1 && structureboundingbox.maxZ - structureboundingbox.minZ > 1) {
                    this.position = new BlockPos(structureboundingbox.minX - blockposition.getX() + 1, structureboundingbox.minY - blockposition.getY() + 1, structureboundingbox.minZ - blockposition.getZ() + 1);
                    this.size = new BlockPos(structureboundingbox.maxX - structureboundingbox.minX - 1, structureboundingbox.maxY - structureboundingbox.minY - 1, structureboundingbox.maxZ - structureboundingbox.minZ - 1);
                    this.markDirty();
                    IBlockState iblockdata = this.world.getBlockState(blockposition);

                    this.world.notifyBlockUpdate(blockposition, iblockdata, iblockdata, 3);
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    private List<TileEntityStructure> filterRelatedCornerBlocks(List<TileEntityStructure> list) {
        Iterable iterable = Iterables.filter(list, new Predicate() {
            public boolean a(@Nullable TileEntityStructure tileentitystructure) {
                return tileentitystructure.mode == TileEntityStructure.Mode.CORNER && TileEntityStructure.this.name.equals(tileentitystructure.name);
            }

            public boolean apply(@Nullable Object object) {
                return this.a((TileEntityStructure) object);
            }
        });

        return Lists.newArrayList(iterable);
    }

    private List<TileEntityStructure> getNearbyCornerBlocks(BlockPos blockposition, BlockPos blockposition1) {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = BlockPos.getAllInBoxMutable(blockposition, blockposition1).iterator();

        while (iterator.hasNext()) {
            BlockPos.MutableBlockPos blockposition_mutableblockposition = (BlockPos.MutableBlockPos) iterator.next();
            IBlockState iblockdata = this.world.getBlockState(blockposition_mutableblockposition);

            if (iblockdata.getBlock() == Blocks.STRUCTURE_BLOCK) {
                TileEntity tileentity = this.world.getTileEntity(blockposition_mutableblockposition);

                if (tileentity != null && tileentity instanceof TileEntityStructure) {
                    arraylist.add((TileEntityStructure) tileentity);
                }
            }
        }

        return arraylist;
    }

    private StructureBoundingBox calculateEnclosingBoundingBox(BlockPos blockposition, List<TileEntityStructure> list) {
        StructureBoundingBox structureboundingbox;

        if (list.size() > 1) {
            BlockPos blockposition1 = ((TileEntityStructure) list.get(0)).getPos();

            structureboundingbox = new StructureBoundingBox(blockposition1, blockposition1);
        } else {
            structureboundingbox = new StructureBoundingBox(blockposition, blockposition);
        }

        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            TileEntityStructure tileentitystructure = (TileEntityStructure) iterator.next();
            BlockPos blockposition2 = tileentitystructure.getPos();

            if (blockposition2.getX() < structureboundingbox.minX) {
                structureboundingbox.minX = blockposition2.getX();
            } else if (blockposition2.getX() > structureboundingbox.maxX) {
                structureboundingbox.maxX = blockposition2.getX();
            }

            if (blockposition2.getY() < structureboundingbox.minY) {
                structureboundingbox.minY = blockposition2.getY();
            } else if (blockposition2.getY() > structureboundingbox.maxY) {
                structureboundingbox.maxY = blockposition2.getY();
            }

            if (blockposition2.getZ() < structureboundingbox.minZ) {
                structureboundingbox.minZ = blockposition2.getZ();
            } else if (blockposition2.getZ() > structureboundingbox.maxZ) {
                structureboundingbox.maxZ = blockposition2.getZ();
            }
        }

        return structureboundingbox;
    }

    public boolean save() {
        return this.save(true);
    }

    public boolean save(boolean flag) {
        if (this.mode == TileEntityStructure.Mode.SAVE && !this.world.isRemote && !StringUtils.isNullOrEmpty(this.name)) {
            BlockPos blockposition = this.getPos().add((Vec3i) this.position);
            WorldServer worldserver = (WorldServer) this.world;
            MinecraftServer minecraftserver = this.world.getMinecraftServer();
            TemplateManager definedstructuremanager = worldserver.getStructureTemplateManager();
            Template definedstructure = definedstructuremanager.getTemplate(minecraftserver, new ResourceLocation(this.name));

            definedstructure.takeBlocksFromWorld(this.world, blockposition, this.size, !this.ignoreEntities, Blocks.STRUCTURE_VOID);
            definedstructure.setAuthor(this.author);
            return !flag || definedstructuremanager.writeTemplate(minecraftserver, new ResourceLocation(this.name));
        } else {
            return false;
        }
    }

    public boolean load() {
        return this.load(true);
    }

    public boolean load(boolean flag) {
        if (this.mode == TileEntityStructure.Mode.LOAD && !this.world.isRemote && !StringUtils.isNullOrEmpty(this.name)) {
            BlockPos blockposition = this.getPos();
            BlockPos blockposition1 = blockposition.add((Vec3i) this.position);
            WorldServer worldserver = (WorldServer) this.world;
            MinecraftServer minecraftserver = this.world.getMinecraftServer();
            TemplateManager definedstructuremanager = worldserver.getStructureTemplateManager();
            Template definedstructure = definedstructuremanager.get(minecraftserver, new ResourceLocation(this.name));

            if (definedstructure == null) {
                return false;
            } else {
                if (!StringUtils.isNullOrEmpty(definedstructure.getAuthor())) {
                    this.author = definedstructure.getAuthor();
                }

                BlockPos blockposition2 = definedstructure.getSize();
                boolean flag1 = this.size.equals(blockposition2);

                if (!flag1) {
                    this.size = blockposition2;
                    this.markDirty();
                    IBlockState iblockdata = this.world.getBlockState(blockposition);

                    this.world.notifyBlockUpdate(blockposition, iblockdata, iblockdata, 3);
                }

                if (flag && !flag1) {
                    return false;
                } else {
                    PlacementSettings definedstructureinfo = (new PlacementSettings()).setMirror(this.mirror).setRotation(this.rotation).setIgnoreEntities(this.ignoreEntities).setChunk((ChunkPos) null).setReplacedBlock((Block) null).setIgnoreStructureBlock(false);

                    if (this.integrity < 1.0F) {
                        definedstructureinfo.setIntegrity(MathHelper.clamp(this.integrity, 0.0F, 1.0F)).setSeed(Long.valueOf(this.seed));
                    }

                    definedstructure.addBlocksToWorldChunk(this.world, blockposition1, definedstructureinfo);
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    public void unloadStructure() {
        WorldServer worldserver = (WorldServer) this.world;
        TemplateManager definedstructuremanager = worldserver.getStructureTemplateManager();

        definedstructuremanager.remove(new ResourceLocation(this.name));
    }

    public boolean isStructureLoadable() {
        if (this.mode == TileEntityStructure.Mode.LOAD && !this.world.isRemote) {
            WorldServer worldserver = (WorldServer) this.world;
            MinecraftServer minecraftserver = this.world.getMinecraftServer();
            TemplateManager definedstructuremanager = worldserver.getStructureTemplateManager();

            return definedstructuremanager.get(minecraftserver, new ResourceLocation(this.name)) != null;
        } else {
            return false;
        }
    }

    public boolean isPowered() {
        return this.powered;
    }

    public void setPowered(boolean flag) {
        this.powered = flag;
    }

    public void setShowAir(boolean flag) {
        this.showAir = flag;
    }

    public void setShowBoundingBox(boolean flag) {
        this.showBoundingBox = flag;
    }

    @Nullable
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation("structure_block.hover." + this.mode.modeName, new Object[] { this.mode == TileEntityStructure.Mode.DATA ? this.metadata : this.name});
    }

    public static enum Mode implements IStringSerializable {

        SAVE("save", 0), LOAD("load", 1), CORNER("corner", 2), DATA("data", 3);

        private static final TileEntityStructure.Mode[] MODES = new TileEntityStructure.Mode[values().length];
        private final String modeName;
        private final int modeId;

        private Mode(String s, int i) {
            this.modeName = s;
            this.modeId = i;
        }

        public String getName() {
            return this.modeName;
        }

        public int getModeId() {
            return this.modeId;
        }

        public static TileEntityStructure.Mode getById(int i) {
            return i >= 0 && i < TileEntityStructure.Mode.MODES.length ? TileEntityStructure.Mode.MODES[i] : TileEntityStructure.Mode.MODES[0];
        }

        static {
            TileEntityStructure.Mode[] atileentitystructure_usagemode = values();
            int i = atileentitystructure_usagemode.length;

            for (int j = 0; j < i; ++j) {
                TileEntityStructure.Mode tileentitystructure_usagemode = atileentitystructure_usagemode[j];

                TileEntityStructure.Mode.MODES[tileentitystructure_usagemode.getModeId()] = tileentitystructure_usagemode;
            }

        }
    }
}
