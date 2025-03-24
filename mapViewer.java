import javax.swing.*;
import java.awt.*;

public class mapViewer extends JFrame {
    private Image mapImage;

    public mapViewer(String imagePath) {
        setTitle("TMU Campus Map");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load the image
        mapImage = new ImageIcon(imagePath).getImage();

        // Custom JPanel to draw the map
        JPanel mapPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(mapImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        add(mapPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new mapViewer("map.png"));
    }
}
