import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import com.google.gson.*;

public class IsometricMapEditor extends JPanel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final int GRID_WIDTH = 10;
    private static final int GRID_HEIGHT = 10;
    private final transient Tile[][] tiles = new Tile[GRID_WIDTH][GRID_HEIGHT];
    private final transient Cursor editorCursor = new Cursor();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Isometric Map Editor");
        IsometricMapEditor editor = new IsometricMapEditor();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setJMenuBar(createMenuBar(editor, frame));
        frame.add(editor);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JMenuBar createMenuBar(IsometricMapEditor editor, JFrame frame) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createMenuOptionFile(editor, frame));
        return menuBar;
    }

    private static JMenu createMenuOptionFile(IsometricMapEditor editor, JFrame frame) {
        JMenu fileMenu = new JMenu("File");

        JMenuItem newItem = new JMenuItem("New");
        newItem.addActionListener(e -> editor.newMap());
        fileMenu.add(newItem);

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> editor.saveMap(frame));
        fileMenu.add(saveItem);

        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.addActionListener(e -> editor.loadMap(frame));
        fileMenu.add(loadItem);

        return fileMenu;
    }

    private void newMap() {
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                tiles[x][y] = new Tile();
            }
        }
        editorCursor.reset();
        repaint();
    }

    private void saveMap(JFrame frame) {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (Writer writer = new FileWriter(file)) {
                Gson gson = new Gson();
                gson.toJson(tiles, writer);
                JOptionPane.showMessageDialog(frame, "Map saved successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Failed to save map: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void loadMap(JFrame frame) {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (Reader reader = new FileReader(file)) {
                Gson gson = new Gson();
                Tile[][] loadedTiles = gson.fromJson(reader, Tile[][].class);
                if (loadedTiles.length == GRID_WIDTH && loadedTiles[0].length == GRID_HEIGHT) {
                    for (int x = 0; x < GRID_WIDTH; x++) {
                        System.arraycopy(loadedTiles[x], 0, tiles[x], 0, GRID_HEIGHT);
                    }
                    repaint();
                    JOptionPane.showMessageDialog(frame, "Map loaded successfully!");
                } else {
                    throw new IllegalArgumentException("Map dimensions do not match!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Failed to load map: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    public IsometricMapEditor() {
        setFocusable(true);

        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                tiles[x][y] = new Tile();
            }
        }

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_UP -> moveCursor(0, -1);
                    case KeyEvent.VK_DOWN -> moveCursor(0, 1);
                    case KeyEvent.VK_LEFT -> moveCursor(-1, 0);
                    case KeyEvent.VK_RIGHT -> moveCursor(1, 0);
                    case KeyEvent.VK_Z -> addTile();
                    case KeyEvent.VK_X -> removeTile();
                }
                repaint();
            }
        });
    }

    private void moveCursor(int dx, int dy) {
        editorCursor.move(dx, dy, GRID_WIDTH, GRID_HEIGHT);
        editorCursor.updateZ(tiles);

        System.out.println(
                "CURSOR POSITION { " +
                        "x: " + editorCursor.getX() + ", " +
                        "y: " + editorCursor.getY() + ", " +
                        "z: " + editorCursor.getZ() + " " +
                        "}"
        );

        repaint();
    }

    private void addTile() {
        tiles[editorCursor.getX()][editorCursor.getY()].increaseHeight();
        editorCursor.updateZ(tiles);
    }

    private void removeTile() {
        tiles[editorCursor.getX()][editorCursor.getY()].decreaseHeight();
        editorCursor.updateZ(tiles);
    }

    private Point getIsometricPosition(int x, int y) {
        int isoX = (x - y) * 64 / 2;
        int isoY = (x + y) * 32 / 2;
        return new Point(isoX + getWidth() / 2, isoY + getHeight() / 4);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                Point p = getIsometricPosition(x, y);
                int baseX = p.x;
                int baseY = p.y;
                for (int z = 0; z < tiles[x][y].getHeight(); z++) {
                    TileRenderer.drawTile(g2d, baseX, baseY, z);
                }
            }
        }

        Point cursorPos = getIsometricPosition(editorCursor.getX(), editorCursor.getY());
        TileRenderer.drawCursor(g2d, cursorPos.x, cursorPos.y, editorCursor.getZ());
    }
}
