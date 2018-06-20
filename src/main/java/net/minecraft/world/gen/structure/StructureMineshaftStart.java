package net.minecraft.world.gen.structure;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.world.World;

public class StructureMineshaftStart extends StructureStart {

    private MapGenMineshaft.Type field_189908_c;

    public StructureMineshaftStart() {}

    public StructureMineshaftStart(World world, Random random, int i, int j, MapGenMineshaft.Type worldgenmineshaft_type) {
        super(i, j);
        this.field_189908_c = worldgenmineshaft_type;
        StructureMineshaftPieces.Room worldgenmineshaftpieces_worldgenmineshaftroom = new StructureMineshaftPieces.Room(0, random, (i << 4) + 2, (j << 4) + 2, this.field_189908_c);

        this.field_75075_a.add(worldgenmineshaftpieces_worldgenmineshaftroom);
        worldgenmineshaftpieces_worldgenmineshaftroom.func_74861_a(worldgenmineshaftpieces_worldgenmineshaftroom, this.field_75075_a, random);
        this.func_75072_c();
        if (worldgenmineshaft_type == MapGenMineshaft.Type.MESA) {
            boolean flag = true;
            int k = world.func_181545_F() - this.field_75074_b.field_78894_e + this.field_75074_b.func_78882_c() / 2 - -5;

            this.field_75074_b.func_78886_a(0, k, 0);
            Iterator iterator = this.field_75075_a.iterator();

            while (iterator.hasNext()) {
                StructureComponent structurepiece = (StructureComponent) iterator.next();

                structurepiece.func_181138_a(0, k, 0);
            }
        } else {
            this.func_75067_a(world, random, 10);
        }

    }
}
