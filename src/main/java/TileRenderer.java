import java.awt.*;

public class TileRenderer {

    private TileRenderer() {
        throw new UnsupportedOperationException("TileRenderer is a utility class and should not be instantiated.");
    }

    public static void drawTile(Graphics2D g2d, int x, int y, int height) {
        y -= height * 16;

        int[] xPointsTop = {x, x + 64 / 2, x, x - 64 / 2};
        int[] yPointsTop = {y, y + 32 / 2, y + 32, y + 32 / 2};
        g2d.setColor(new Color(200, 200, 200));
        g2d.fillPolygon(xPointsTop, yPointsTop, 4);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(xPointsTop, yPointsTop, 4);

        int[] xPointsRight = {x, x + 64 / 2, x + 64 / 2, x};
        int[] yPointsRight = {y + 32, y + 32 / 2, y + 32 / 2 + 16, y + 32 + 16};
        g2d.setColor(new Color(180, 180, 180));
        g2d.fillPolygon(xPointsRight, yPointsRight, 4);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(xPointsRight, yPointsRight, 4);

        int[] xPointsLeft = {x, x - 64 / 2, x - 64 / 2, x};
        int[] yPointsLeft = {y + 32, y + 32 / 2, y + 32 / 2 + 16, y + 32 + 16};
        g2d.setColor(new Color(160, 160, 160));
        g2d.fillPolygon(xPointsLeft, yPointsLeft, 4);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(xPointsLeft, yPointsLeft, 4);
    }

    public static void drawCursor(Graphics2D g2d, int x, int y, int height) {
        y -= height * 16;

        int[] xPoints = {x, x + 64 / 2, x, x - 64 / 2};
        int[] yPoints = {y, y + 32 / 2, y + 32, y + 32 / 2};
        g2d.setColor(new Color(255, 0, 0, 128));
        g2d.fillPolygon(xPoints, yPoints, 4);
    }
}
