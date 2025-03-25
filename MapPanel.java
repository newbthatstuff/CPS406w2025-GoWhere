import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MapPanel extends JPanel implements MouseWheelListener, MouseListener, MouseMotionListener {
    private BufferedImage mapImage;
    private double zoomFactor = 1.0;
    private double minZoomFactor = 1.0;
    private boolean fitToScreen = true;

    private int offsetX = 0, offsetY = 0;
    private int lastMouseX, lastMouseY;
    private boolean dragging = false;

    public MapPanel(String imagePath) {
        try {
            mapImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
        }

        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateMinZoom();
                fitToScreen = true;
                offsetX = 0;
                offsetY = 0;
                repaint();
            }
        });

        if (mapImage != null) {
            setPreferredSize(new Dimension(1280, 853));
        }
    }

    private void updateMinZoom() {
        if (mapImage != null) {
            double scaleX = (double) getWidth() / mapImage.getWidth();
            double scaleY = (double) getHeight() / mapImage.getHeight();
            minZoomFactor = Math.min(scaleX, scaleY);
            zoomFactor = Math.max(zoomFactor, minZoomFactor);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mapImage == null) return;

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int imgWidth = mapImage.getWidth();
        int imgHeight = mapImage.getHeight();

        double scaleX = (double) panelWidth / imgWidth;
        double scaleY = (double) panelHeight / imgHeight;
        double scale = fitToScreen ? Math.min(scaleX, scaleY) : zoomFactor;

        int newWidth = (int) (imgWidth * scale);
        int newHeight = (int) (imgHeight * scale);

        int x = (panelWidth - newWidth) / 2 + offsetX;
        int y = (panelHeight - newHeight) / 2 + offsetY;

        g.drawImage(mapImage, x, y, newWidth, newHeight, this);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
    
        double prevZoom = zoomFactor;
        int notches = e.getWheelRotation();
        double scaleAmount = (notches < 0) ? 1.1 : 1 / 1.1;
    
        zoomFactor *= scaleAmount;
        zoomFactor = Math.max(minZoomFactor, Math.min(zoomFactor, 3.0));
    
        if (prevZoom == zoomFactor) return; // Prevent unnecessary recalculations
    
        // Adjust offsets to keep zoom centered at the cursor
        double scaleChange = zoomFactor / prevZoom;
        offsetX = (int) ((mouseX - getWidth() / 2) + (offsetX - (mouseX - getWidth() / 2)) * scaleChange);
        offsetY = (int) ((mouseY - getHeight() / 2) + (offsetY - (mouseY - getHeight() / 2)) * scaleChange);
    
        fitToScreen = false;
        repaint();
    }    

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && zoomFactor > minZoomFactor) {
            dragging = true;
            lastMouseX = e.getX();
            lastMouseY = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (dragging) {
            int dx = e.getX() - lastMouseX;
            int dy = e.getY() - lastMouseY;
    
            offsetX += dx;
            offsetY += dy;
    
            lastMouseX = e.getX();
            lastMouseY = e.getY();
    
            repaint();
        }
    }    

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}
}
