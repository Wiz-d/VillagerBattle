public class Block {

    public boolean isUnit = false;
    public String blockType = Utility.TERRAIN_UNIT;

    public Block(boolean isUnit, String blockType) {
        this.isUnit = isUnit;
        this.blockType = blockType;
    }
}
