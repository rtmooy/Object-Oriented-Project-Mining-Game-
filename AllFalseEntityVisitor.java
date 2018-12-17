public class AllFalseEntityVisitor implements EntityVisitor<Boolean> {
    public Boolean visit(Ore ore) {
        return false;
    }

    public Boolean visit(OreBlob blob) {
        return false;
    }

    public Boolean visit(Quake quake) {
        return false;
    }

    public Boolean visit(MinerNotFull minernotfull) {
        return false;
    }

    public Boolean visit(MinerFull minerfull) {
        return false;
    }

    public Boolean visit(Obstacle obstacle) {
        return false;
    }

    public Boolean visit(Vein vein) {
        return false;
    }

    public Boolean visit(Blacksmith smith) {
        return false;
    }
}
