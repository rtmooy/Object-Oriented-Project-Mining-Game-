public interface EntityVisitor<R>{
	R visit(Ore ore);
	R visit(Blacksmith smith);
	R visit(Obstacle obstacle);
	R visit(Quake quake);
	R visit(MinerNotFull minernotfull);
	R visit(MinerFull minerfull);
	R visit(Vein vein);
	R visit(OreBlob blob);
}