package net.minecraft.world.gen.structure;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.world.World;

public class StructureMineshaftStart extends StructureStart {

    private MapGenMineshaft.Type mineShaftType;

    public StructureMineshaftStart() {}

    public StructureMineshaftStart(World world, Random random, int i, int j, MapGenMineshaft.Type worldgenmineshaft_type) {
        super(i, j);
        this.mineShaftType = worldgenmineshaft_type;
        StructureMineshaftPieces.Room worldgenmineshaftpieces_worldgenmineshaftroom = new StructureMineshaftPieces.Room(0, random, (i << 4) + 2, (j << 4) + 2, this.mineShaftType);

        this.components.add(worldgenmineshaftpieces_worldgenmineshaftroom);
        worldgenmineshaftpieces_worldgenmineshaftroom.buildComponent((StructureComponent) worldgenmineshaftpieces_worldgenmineshaftroom, this.components, random);
        this.updateBoundingBox();
        if (worldgenmineshaft_type == MapGenMineshaft.Type.MESA) {
            boolean flag = true;
            int k = world.getSeaLevel() - this.boundingBox.maxY + this.boundingBox.getYSize() / 2 - -5;

            this.boundingBox.offset(0, k, 0);
            Iterator iterator = this.components.iterator();

            while (iterator.hasNext()) {
                StructureComponent structurepiece = (StructureComponent) iterator.next();

                structurepiece.offset(0, k, 0);
            }
        } else {
            this.markAvailableHeight(world, random, 10);
        }

    }
}
