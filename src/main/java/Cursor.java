public class Cursor {
    private int x = 0;
    private int y = 0;
    private int z = -1;

    public Cursor() {
        reset();
    }

    public void move(int dx, int dy, int gridWidth, int gridHeight) {
        x = Math.max(0, Math.min(x + dx, gridWidth - 1));
        y = Math.max(0, Math.min(y + dy, gridHeight - 1));
    }

    public void updateZ(Tile[][] tiles) {
        z = tiles[x][y].getHeight() - 1;
    }

    public void reset() {
        x = 0;
        y = 0;
        z = -1;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
