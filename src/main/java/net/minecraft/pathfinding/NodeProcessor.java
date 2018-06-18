package net.minecraft.pathfinding;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;


public abstract class NodeProcessor {

    protected IBlockAccess blockaccess;
    protected EntityLiving entity;
    protected final IntHashMap<PathPoint> pointMap = new IntHashMap();
    protected int entitySizeX;
    protected int entitySizeY;
    protected int entitySizeZ;
    protected boolean canEnterDoors;
    protected boolean canOpenDoors;
    protected boolean canSwim;

    public NodeProcessor() {}

    public void init(IBlockAccess iblockaccess, EntityLiving entityinsentient) {
        this.blockaccess = iblockaccess;
        this.entity = entityinsentient;
        this.pointMap.clearMap();
        this.entitySizeX = MathHelper.floor(entityinsentient.width + 1.0F);
        this.entitySizeY = MathHelper.floor(entityinsentient.height + 1.0F);
        this.entitySizeZ = MathHelper.floor(entityinsentient.width + 1.0F);
    }

    public void postProcess() {
        this.blockaccess = null;
        this.entity = null;
    }

    protected PathPoint openPoint(int i, int j, int k) {
        int l = PathPoint.makeHash(i, j, k);
        PathPoint pathpoint = (PathPoint) this.pointMap.lookup(l);

        if (pathpoint == null) {
            pathpoint = new PathPoint(i, j, k);
            this.pointMap.addKey(l, pathpoint);
        }

        return pathpoint;
    }

    public abstract PathPoint getStart();

    public abstract PathPoint getPathPointToCoords(double d0, double d1, double d2);

    public abstract int findPathOptions(PathPoint[] apathpoint, PathPoint pathpoint, PathPoint pathpoint1, float f);

    public abstract PathNodeType getPathNodeType(IBlockAccess iblockaccess, int i, int j, int k, EntityLiving entityinsentient, int l, int i1, int j1, boolean flag, boolean flag1);

    public abstract PathNodeType getPathNodeType(IBlockAccess iblockaccess, int i, int j, int k);

    public void setCanEnterDoors(boolean flag) {
        this.canEnterDoors = flag;
    }

    public void setCanOpenDoors(boolean flag) {
        this.canOpenDoors = flag;
    }

    public void setCanSwim(boolean flag) {
        this.canSwim = flag;
    }

    public boolean getCanEnterDoors() {
        return this.canEnterDoors;
    }

    public boolean getCanOpenDoors() {
        return this.canOpenDoors;
    }

    public boolean getCanSwim() {
        return this.canSwim;
    }
}
