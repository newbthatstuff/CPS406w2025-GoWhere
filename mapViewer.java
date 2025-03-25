import javax.swing.*;

public class MapViewer extends JFrame {
    public MapViewer(String imagePath) {
        setTitle("TMU Campus Map");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        MapPanel mapPanel = new MapPanel(imagePath);
        add(mapPanel);
        pack();

        setSize(1280, 853);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MapViewer("map.png"));
    }
}
