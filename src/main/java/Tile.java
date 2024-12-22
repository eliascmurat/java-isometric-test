public class Tile {
    private int height = 0;

    public int getHeight() {
        return height;
    }

    public void increaseHeight() {
        height++;
    }

    public void decreaseHeight() {
        if (height > 0) height--;
    }
}
