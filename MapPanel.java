import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapPanel extends JPanel implements MouseWheelListener, MouseListener, MouseMotionListener {
    private BufferedImage mapImage;
    private double zoomFactor = 1.0;
    private double minZoomFactor = 1.0;
    private boolean fitToScreen = true;

    private int offsetX = 0, offsetY = 0;
    private int lastMouseX, lastMouseY;
    private boolean dragging = false;

    // Store building names and their coordinates (x, y)
    private Map<String, Point> locations = new HashMap<>();
    private String currentSearchResult = null;

    public MapPanel(String imagePath) {
        try {
            mapImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
        }

        // Locations on map
        locations.put("MRS", new Point(100, 300));
        locations.put("CPK", new Point(830, 370));
        locations.put("CAR", new Point(1290, 200));
        locations.put("MAC", new Point(1430, 150));
        locations.put("YNG", new Point(1020, 480));
        locations.put("CUI", new Point(1400, 500));
        locations.put("JOR", new Point(1120, 650));
        locations.put("KHN", new Point(1400, 650));
        locations.put("MON", new Point(1680, 630));
        locations.put("SHE", new Point(1860, 630));
        locations.put("COP", new Point(1980, 650));
        locations.put("POD", new Point(1110, 780));
        locations.put("KHW", new Point(1270, 780));
        locations.put("KHE", new Point(1530, 780));
        locations.put("ARC", new Point(1680, 730));
        locations.put("EPH", new Point(1810, 690));
        locations.put("PIT", new Point(1800, 770));
        locations.put("SLC", new Point(980, 900));
        locations.put("LIB", new Point(1110, 910));
        locations.put("KHS", new Point(1400, 910));
        locations.put("RCC", new Point(1670, 910));
        locations.put("ILC", new Point(2030, 930));
        locations.put("BKS", new Point(1080, 1010));
        locations.put("CED", new Point(1250, 1090));
        locations.put("IMC", new Point(1340, 1030));
        locations.put("KF", new Point(1460, 1000));
        locations.put("OAK", new Point(1570, 1000));
        locations.put("ENG", new Point(1670, 1080));
        locations.put("MER", new Point(1810, 1090));
        locations.put("RAC", new Point(1410, 835));
        locations.put("PKG", new Point(1090, 1090));
        locations.put("IMA", new Point(1360, 1070));
        locations.put("HEI", new Point(1470, 1070));
        locations.put("SCC", new Point(1500, 1030));
        locations.put("AOB", new Point(780, 1200));
        locations.put("DSQ", new Point(1080, 1200));
        locations.put("VIC", new Point(1250, 1220));
        locations.put("BND", new Point(1370, 1205));
        locations.put("PRO", new Point(1370, 1240));
        locations.put("CIS", new Point(1370, 1260));
        locations.put("BON", new Point(1460, 1190));
        locations.put("SBB", new Point(1460, 1230));
        locations.put("SID", new Point(1550, 1115));
        locations.put("DCC", new Point(1550, 1205));
        locations.put("DAL", new Point(1810, 1130));
        locations.put("CIV", new Point(1880, 1190));
        locations.put("TRS", new Point(600, 1330));
        locations.put("YDI", new Point(810, 1310));
        locations.put("HOEM", new Point(2080, 1460));
        locations.put("BTS", new Point(610, 1610));
        locations.put("TEC", new Point(800, 1500));
        locations.put("SMH", new Point(1260, 1530));

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

    // Search for a location and highlight it
    public void searchLocation(String query) {
        // Convert both the query and location keys to lowercase for comparison
        String lowercaseQuery = query.trim().toLowerCase();

        for (String locationName : locations.keySet()) {
            if (locationName.toLowerCase().equals(lowercaseQuery)) {
                currentSearchResult = locationName;
                repaint();
                return;
            }
        }

        // If no match found, show an error
        JOptionPane.showMessageDialog(this, "Location not found!", "Error", JOptionPane.ERROR_MESSAGE);
        currentSearchResult = null;
        repaint();
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
        if (mapImage == null)
            return;

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

        // Highlight the searched location (if any)
        if (currentSearchResult != null) {
            Point location = locations.get(currentSearchResult);
            if (location != null) {
                int markerX = x + (int) (location.x * scale);
                int markerY = y + (int) (location.y * scale);

                // Draw a big red arrow pointing to the location
                g.setColor(Color.RED);
                int arrowSize = 30; // Size of the arrow
                int[] xPoints = { markerX, markerX - arrowSize, markerX - arrowSize };
                int[] yPoints = { markerY, markerY - arrowSize, markerY + arrowSize };
                g.fillPolygon(xPoints, yPoints, 3); // Draw the arrowhead
            }
        }
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

        if (prevZoom == zoomFactor)
            return; // Prevent unnecessary recalculations

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
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
