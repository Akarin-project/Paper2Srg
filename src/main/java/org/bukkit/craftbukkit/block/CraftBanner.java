package org.bukkit.craftbukkit.block;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityBanner;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

public class CraftBanner extends CraftBlockEntityState<TileEntityBanner> implements Banner {

    private DyeColor base;
    private List<Pattern> patterns;

    public CraftBanner(final Block block) {
        super(block, TileEntityBanner.class);
    }

    public CraftBanner(final Material material, final TileEntityBanner te) {
        super(material, te);
    }

    @Override
    public void load(TileEntityBanner banner) {
        super.load(banner);

        base = DyeColor.getByDyeData((byte) banner.field_175120_a.func_176767_b());
        patterns = new ArrayList<Pattern>();

        if (banner.field_175118_f != null) {
            for (int i = 0; i < banner.field_175118_f.func_74745_c(); i++) {
                NBTTagCompound p = (NBTTagCompound) banner.field_175118_f.func_150305_b(i);
                patterns.add(new Pattern(DyeColor.getByDyeData((byte) p.func_74762_e("Color")), PatternType.getByIdentifier(p.func_74779_i("Pattern"))));
            }
        }
    }

    @Override
    public DyeColor getBaseColor() {
        return this.base;
    }

    @Override
    public void setBaseColor(DyeColor color) {
        this.base = color;
    }

    @Override
    public List<Pattern> getPatterns() {
        return new ArrayList<Pattern>(patterns);
    }

    @Override
    public void setPatterns(List<Pattern> patterns) {
        this.patterns = new ArrayList<Pattern>(patterns);
    }

    @Override
    public void addPattern(Pattern pattern) {
        this.patterns.add(pattern);
    }

    @Override
    public Pattern getPattern(int i) {
        return this.patterns.get(i);
    }

    @Override
    public Pattern removePattern(int i) {
        return this.patterns.remove(i);
    }

    @Override
    public void setPattern(int i, Pattern pattern) {
        this.patterns.set(i, pattern);
    }

    @Override
    public int numberOfPatterns() {
        return patterns.size();
    }

    @Override
    public void applyTo(TileEntityBanner banner) {
        super.applyTo(banner);

        banner.field_175120_a = EnumDyeColor.func_176766_a(base.getDyeData());

        NBTTagList newPatterns = new NBTTagList();

        for (Pattern p : patterns) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.func_74768_a("Color", p.getColor().getDyeData());
            compound.func_74778_a("Pattern", p.getPattern().getIdentifier());
            newPatterns.func_74742_a(compound);
        }
        banner.field_175118_f = newPatterns;
    }
}
