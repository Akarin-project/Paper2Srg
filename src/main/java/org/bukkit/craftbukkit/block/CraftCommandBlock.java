package org.bukkit.craftbukkit.block;

import net.minecraft.tileentity.TileEntityCommandBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;

public class CraftCommandBlock extends CraftBlockEntityState<TileEntityCommandBlock> implements CommandBlock {

    private String command;
    private String name;

    public CraftCommandBlock(Block block) {
        super(block, TileEntityCommandBlock.class);
    }

    public CraftCommandBlock(final Material material, final TileEntityCommandBlock te) {
        super(material, te);
    }

    @Override
    public void load(TileEntityCommandBlock commandBlock) {
        super.load(commandBlock);

        command = commandBlock.func_145993_a().func_145753_i();
        name = commandBlock.func_145993_a().func_70005_c_();
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public void setCommand(String command) {
        this.command = command != null ? command : "";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name != null ? name : "@";
    }

    @Override
    public void applyTo(TileEntityCommandBlock commandBlock) {
        super.applyTo(commandBlock);

        commandBlock.func_145993_a().func_145752_a(command);
        commandBlock.func_145993_a().func_145754_b(name);
    }
}
