package net.minecraft.world.gen;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;


public class FlatLayerInfo {

    private final int version;
    private IBlockState layerMaterial;
    private int layerCount;
    private int layerMinimumY;

    public FlatLayerInfo(int i, Block block) {
        this(3, i, block);
    }

    public FlatLayerInfo(int i, int j, Block block) {
        this.layerCount = 1;
        this.version = i;
        this.layerCount = j;
        this.layerMaterial = block.getDefaultState();
    }

    public FlatLayerInfo(int i, int j, Block block, int k) {
        this(i, j, block);
        this.layerMaterial = block.getStateFromMeta(k);
    }

    public int getLayerCount() {
        return this.layerCount;
    }

    public IBlockState getLayerMaterial() {
        return this.layerMaterial;
    }

    private Block getLayerMaterialBlock() {
        return this.layerMaterial.getBlock();
    }

    private int getFillBlockMeta() {
        return this.layerMaterial.getBlock().getMetaFromState(this.layerMaterial);
    }

    public int getMinY() {
        return this.layerMinimumY;
    }

    public void setMinY(int i) {
        this.layerMinimumY = i;
    }

    public String toString() {
        String s;

        if (this.version >= 3) {
            ResourceLocation minecraftkey = (ResourceLocation) Block.REGISTRY.getNameForObject(this.getLayerMaterialBlock());

            s = minecraftkey == null ? "null" : minecraftkey.toString();
            if (this.layerCount > 1) {
                s = this.layerCount + "*" + s;
            }
        } else {
            s = Integer.toString(Block.getIdFromBlock(this.getLayerMaterialBlock()));
            if (this.layerCount > 1) {
                s = this.layerCount + "x" + s;
            }
        }

        int i = this.getFillBlockMeta();

        if (i > 0) {
            s = s + ":" + i;
        }

        return s;
    }
}
