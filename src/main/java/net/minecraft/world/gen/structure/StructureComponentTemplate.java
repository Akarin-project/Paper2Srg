package net.minecraft.world.gen.structure;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public abstract class StructureComponentTemplate extends StructureComponent {

    private static final PlacementSettings field_186179_d = new PlacementSettings();
    protected Template field_186176_a;
    protected PlacementSettings field_186177_b;
    protected BlockPos field_186178_c;

    public StructureComponentTemplate() {
        this.field_186177_b = StructureComponentTemplate.field_186179_d.func_186222_a(true).func_186225_a(Blocks.field_150350_a);
    }

    public StructureComponentTemplate(int i) {
        super(i);
        this.field_186177_b = StructureComponentTemplate.field_186179_d.func_186222_a(true).func_186225_a(Blocks.field_150350_a);
    }

    protected void func_186173_a(Template definedstructure, BlockPos blockposition, PlacementSettings definedstructureinfo) {
        this.field_186176_a = definedstructure;
        this.func_186164_a(EnumFacing.NORTH);
        this.field_186178_c = blockposition;
        this.field_186177_b = definedstructureinfo;
        this.func_186174_h();
    }

    protected void func_143012_a(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74768_a("TPX", this.field_186178_c.func_177958_n());
        nbttagcompound.func_74768_a("TPY", this.field_186178_c.func_177956_o());
        nbttagcompound.func_74768_a("TPZ", this.field_186178_c.func_177952_p());
    }

    protected void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
        this.field_186178_c = new BlockPos(nbttagcompound.func_74762_e("TPX"), nbttagcompound.func_74762_e("TPY"), nbttagcompound.func_74762_e("TPZ"));
    }

    public boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox) {
        this.field_186177_b.func_186223_a(structureboundingbox);
        this.field_186176_a.func_189962_a(world, this.field_186178_c, this.field_186177_b, 18);
        Map map = this.field_186176_a.func_186258_a(this.field_186178_c, this.field_186177_b);
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            String s = (String) entry.getValue();

            this.func_186175_a(s, (BlockPos) entry.getKey(), world, random, structureboundingbox);
        }

        return true;
    }

    protected abstract void func_186175_a(String s, BlockPos blockposition, World world, Random random, StructureBoundingBox structureboundingbox);

    private void func_186174_h() {
        Rotation enumblockrotation = this.field_186177_b.func_186215_c();
        BlockPos blockposition = this.field_186176_a.func_186257_a(enumblockrotation);
        Mirror enumblockmirror = this.field_186177_b.func_186212_b();

        this.field_74887_e = new StructureBoundingBox(0, 0, 0, blockposition.func_177958_n(), blockposition.func_177956_o() - 1, blockposition.func_177952_p());
        switch (enumblockrotation) {
        case NONE:
        default:
            break;

        case CLOCKWISE_90:
            this.field_74887_e.func_78886_a(-blockposition.func_177958_n(), 0, 0);
            break;

        case COUNTERCLOCKWISE_90:
            this.field_74887_e.func_78886_a(0, 0, -blockposition.func_177952_p());
            break;

        case CLOCKWISE_180:
            this.field_74887_e.func_78886_a(-blockposition.func_177958_n(), 0, -blockposition.func_177952_p());
        }

        BlockPos blockposition1;

        switch (enumblockmirror) {
        case NONE:
        default:
            break;

        case FRONT_BACK:
            blockposition1 = BlockPos.field_177992_a;
            if (enumblockrotation != Rotation.CLOCKWISE_90 && enumblockrotation != Rotation.COUNTERCLOCKWISE_90) {
                if (enumblockrotation == Rotation.CLOCKWISE_180) {
                    blockposition1 = blockposition1.func_177967_a(EnumFacing.EAST, blockposition.func_177958_n());
                } else {
                    blockposition1 = blockposition1.func_177967_a(EnumFacing.WEST, blockposition.func_177958_n());
                }
            } else {
                blockposition1 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.WEST), blockposition.func_177952_p());
            }

            this.field_74887_e.func_78886_a(blockposition1.func_177958_n(), 0, blockposition1.func_177952_p());
            break;

        case LEFT_RIGHT:
            blockposition1 = BlockPos.field_177992_a;
            if (enumblockrotation != Rotation.CLOCKWISE_90 && enumblockrotation != Rotation.COUNTERCLOCKWISE_90) {
                if (enumblockrotation == Rotation.CLOCKWISE_180) {
                    blockposition1 = blockposition1.func_177967_a(EnumFacing.SOUTH, blockposition.func_177952_p());
                } else {
                    blockposition1 = blockposition1.func_177967_a(EnumFacing.NORTH, blockposition.func_177952_p());
                }
            } else {
                blockposition1 = blockposition1.func_177967_a(enumblockrotation.func_185831_a(EnumFacing.NORTH), blockposition.func_177958_n());
            }

            this.field_74887_e.func_78886_a(blockposition1.func_177958_n(), 0, blockposition1.func_177952_p());
        }

        this.field_74887_e.func_78886_a(this.field_186178_c.func_177958_n(), this.field_186178_c.func_177956_o(), this.field_186178_c.func_177952_p());
    }

    public void func_181138_a(int i, int j, int k) {
        super.func_181138_a(i, j, k);
        this.field_186178_c = this.field_186178_c.func_177982_a(i, j, k);
    }
}
