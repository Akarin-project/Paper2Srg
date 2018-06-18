package net.minecraft.block.state;

import com.google.common.collect.ImmutableMap;
import java.util.Collection;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;

public interface IBlockState extends IBlockBehaviors, IBlockProperties {

    Collection<IProperty<?>> getPropertyKeys();

    <T extends Comparable<T>> T getValue(IProperty<T> iblockstate);

    <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> iblockstate, V v0);

    <T extends Comparable<T>> IBlockState cycleProperty(IProperty<T> iblockstate);

    ImmutableMap<IProperty<?>, Comparable<?>> getProperties();

    Block getBlock();
}
