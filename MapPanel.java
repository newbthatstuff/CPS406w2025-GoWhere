import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class MapPanel extends JPanel implements MouseWheelListener, MouseListener, MouseMotionListener {
    private BufferedImage mapImage;
    private double zoomFactor = 1.0;
    private double minZoomFactor = 1.0;
    private boolean fitToScreen = true;
    private int offsetX = 0, offsetY = 0;
    private int lastMouseX, lastMouseY;
    private boolean dragging = false;
    private Graph graph;
    private String currentSearchResult = null;
    private Route route;

    public MapPanel(String imagePath) {
        try {
            mapImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
        }

        graph = new Graph();

        graph.addNode("MRS", 100, 300);
        graph.addNode("CPK", 830, 370);
        graph.addNode("CAR", 1290, 200);
        graph.addNode("MAC", 1430, 150);
        graph.addNode("YNG", 1020, 480);
        graph.addNode("CUI", 1400, 500);
        graph.addNode("JOR", 1120, 650);
        graph.addNode("KHN", 1400, 650);
        graph.addNode("MON", 1680, 630);
        graph.addNode("SHE", 1860, 630);
        graph.addNode("COP", 1980, 650);
        graph.addNode("POD", 1110, 780);
        graph.addNode("KHW", 1270, 780);
        graph.addNode("KHE", 1530, 780);
        graph.addNode("ARC", 1680, 730);
        graph.addNode("EPH", 1810, 690);
        graph.addNode("PIT", 1800, 770);
        graph.addNode("SLC", 980, 900);
        graph.addNode("LIB", 1110, 910);
        graph.addNode("KHS", 1400, 910);
        graph.addNode("RCC", 1670, 910);
        graph.addNode("ILC", 2030, 930);
        graph.addNode("BKS", 1080, 1010);
        graph.addNode("CED", 1250, 1090);
        graph.addNode("IMC", 1340, 1030);
        graph.addNode("OKF", 1460, 1000);
        graph.addNode("OAK", 1570, 1000);
        graph.addNode("ENG", 1670, 1080);
        graph.addNode("MER", 1810, 1090);
        graph.addNode("RAC", 1410, 835);
        graph.addNode("PKG", 1090, 1090);
        graph.addNode("IMA", 1360, 1070);
        graph.addNode("HEI", 1470, 1070);
        graph.addNode("SCC", 1500, 1030);
        graph.addNode("AOB", 780, 1200);
        graph.addNode("DSQ", 1080, 1200);
        graph.addNode("VIC", 1250, 1220);
        graph.addNode("BND", 1370, 1205);
        graph.addNode("PRO", 1370, 1240);
        graph.addNode("CIS", 1370, 1260);
        graph.addNode("BON", 1460, 1190);
        graph.addNode("SBB", 1460, 1230);
        graph.addNode("SID", 1550, 1115);
        graph.addNode("DCC", 1550, 1205);
        graph.addNode("DAL", 1810, 1130);
        graph.addNode("CIV", 1880, 1190);
        graph.addNode("TRS", 600, 1330);
        graph.addNode("YDI", 810, 1310);
        graph.addNode("HOEM", 2080, 1460);
        graph.addNode("BTS", 610, 1610);
        graph.addNode("TEC", 800, 1500);
        graph.addNode("SMH", 1260, 1530);

        graph.addNode("I1", 100, 300);   // Gerrard St West & Elizabeth St Ave
        graph.addNode("I2", 1120, 650);  // Gerrard St West & University Ave
        graph.addNode("I3", 1530, 780);  // Gerrard St East & Church St
        graph.addNode("I4", 1400, 910);  // Gould St & Church St
        graph.addNode("I5", 1250, 1220); // Dundas St East & Victoria St
        graph.addNode("I6", 810, 1310);  // Dundas St West & Yonge St
        graph.addNode("I7", 2080, 1460); // Dundas St East & Jarvis St
        graph.addNode("I8", 980, 900);   // Gould St & Yonge St
        graph.addNode("I9", 1020, 650);  // Gerrard St & Yonge St
        graph.addNode("I10", 1430, 150); // Carlton St & Church St
        graph.addNode("I11", 830, 370);  // Gerrard St West & Bay St
        graph.addNode("I12", 1020, 150); // Carlton St & Yonge St
        graph.addNode("I13", 1250, 650); // Gerrard St & Victoria St
        graph.addNode("I14", 1250, 150); // Carlton St & Victoria St
        graph.addNode("I15", 2030, 910); // Gould St & Jarvis St
        graph.addNode("I16", 2055, 1190); // Jarvis St, between Gould and Dundas
        graph.addNode("I17", 1250, 1115); // Victoria St & Mutual St
        graph.addNode("I18", 1465, 845); // Church St, between Gerrard and Gould
        graph.addNode("I19", 1250, 910); // Gould St & Victoria St


        graph.addEdge("I1", "I11");  // Gerrard St West (Elizabeth to Bay)
        graph.addEdge("I11", "I9");  // Gerrard St West (Bay to Yonge)
        graph.addEdge("I9", "I2");   // Gerrard St West (Yonge to University)
        graph.addEdge("I2", "I13");  // Gerrard St (University to Victoria)
        graph.addEdge("I13", "I3");  // Gerrard St (Victoria to Church)
        graph.addEdge("I3", "I18");  // Church St (Gerrard to intermediate point)
        graph.addEdge("I18", "I4");  // Church St (intermediate point to Gould)
        graph.addEdge("I4", "I5");   // Victoria St (Gould to Dundas)
        graph.addEdge("I5", "I7");   // Dundas St East (Victoria to Jarvis)
        graph.addEdge("I6", "I5");   // Dundas St (Yonge to Victoria)
        graph.addEdge("I8", "I19");  // Gould St (Yonge to Victoria)
        graph.addEdge("I19", "I4");  // Gould St (Victoria to Church)
        graph.addEdge("I4", "I15");  // Gould St (Church to Jarvis)
        graph.addEdge("I15", "I16"); // Jarvis St (Gould to intermediate point)
        graph.addEdge("I16", "I7");  // Jarvis St (intermediate point to Dundas)
        graph.addEdge("I19", "I17"); // Victoria St (Gould to Mutual)
        graph.addEdge("I17", "I5");  // Victoria St (Mutual to Dundas)
        graph.addEdge("I9", "I8");   // Yonge St (Gerrard to Gould)
        graph.addEdge("I8", "I6");   // Yonge St (Gould to Dundas)
        graph.addEdge("I10", "I3");  // Church St (Carlton to Gerrard)
        graph.addEdge("I12", "I9");  // Yonge St (Carlton to Gerrard)
        graph.addEdge("I12", "I10"); // Carlton St (Yonge to Church)
        graph.addEdge("I10", "I14"); // Carlton St (Church to Victoria)
        graph.addEdge("I14", "I13"); // Victoria St (Carlton to Gerrard)
        graph.addEdge("I13", "I5");  // Victoria St (Gerrard to Dundas)

        graph.addEdge("MRS", "I1");
        graph.addEdge("CPK", "I11");
        graph.addEdge("CAR", "I10");
        graph.addEdge("MAC", "I10");
        graph.addEdge("YNG", "I9");
        graph.addEdge("CUI", "I14");
        graph.addEdge("JOR", "I2");
        graph.addEdge("KHN", "I3");
        graph.addEdge("MON", "I3");
        graph.addEdge("SHE", "I3");
        graph.addEdge("COP", "I3");
        graph.addEdge("POD", "I2");
        graph.addEdge("KHW", "I13");
        graph.addEdge("KHE", "I3");
        graph.addEdge("ARC", "I15");
        graph.addEdge("EPH", "I15");
        graph.addEdge("PIT", "I15");
        graph.addEdge("SLC", "I8");
        graph.addEdge("LIB", "I8");
        graph.addEdge("KHS", "I19");
        graph.addEdge("RCC", "I15");
        graph.addEdge("ILC", "I15");
        graph.addEdge("BKS", "I8");
        graph.addEdge("CED", "I5");
        graph.addEdge("IMC", "I19");
        graph.addEdge("OKF", "I19");
        graph.addEdge("OAK", "I19");
        graph.addEdge("ENG", "I15");
        graph.addEdge("MER", "I15");
        graph.addEdge("RAC", "I19");
        graph.addEdge("PKG", "I8");
        graph.addEdge("IMA", "I17");
        graph.addEdge("HEI", "I17");
        graph.addEdge("SCC", "I19");
        graph.addEdge("AOB", "I6");
        graph.addEdge("DSQ", "I6");
        graph.addEdge("VIC", "I5");
        graph.addEdge("BND", "I5");
        graph.addEdge("PRO", "I5");
        graph.addEdge("CIS", "I5");
        graph.addEdge("BON", "I5");
        graph.addEdge("SBB", "I5");
        graph.addEdge("SID", "I17");
        graph.addEdge("DCC", "I17");
        graph.addEdge("DAL", "I15");
        graph.addEdge("CIV", "I16");
        graph.addEdge("TRS", "I6");
        graph.addEdge("YDI", "I6");
        graph.addEdge("HOEM", "I7");
        graph.addEdge("BTS", "I6");
        graph.addEdge("TEC", "I6");
        graph.addEdge("SMH", "I5");

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

    public Graph getGraph() {
        return graph;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public void searchLocation(String query) {
        String lowercaseQuery = query.trim().toLowerCase();
        for (String node : graph.getCoordinates().keySet()) {
            if (!node.matches("I\\d+") && node.toLowerCase().equals(lowercaseQuery)) {
                currentSearchResult = node;
                repaint();
                return;
            }
        }
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

        if (route != null && route.getPath() != null && route.getPath().size() > 1) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(3));

            List<String> path = route.getPath();
            for (int i = 0; i < path.size() - 1; i++) {
                String node1 = path.get(i);
                String node2 = path.get(i + 1);
                Point p1 = graph.getCoordinates(node1);
                Point p2 = graph.getCoordinates(node2);


                int x1 = x + (int) (p1.x * scale);
                int y1 = y + (int) (p1.y * scale);
                int x2 = x + (int) (p2.x * scale);
                int y2 = y + (int) (p2.y * scale);

                List<Point> segmentPoints = new ArrayList<>();
                segmentPoints.add(new Point(x1, y1));

                if (node1.matches("I\\d+") && node2.matches("I\\d+")) {
                    String edge = node1.compareTo(node2) < 0 ? node1 + "-" + node2 : node2 + "-" + node1;
                    if (edge.equals("I8-I19") || edge.equals("I19-I4") || edge.equals("I4-I15")) {
                        segmentPoints.add(new Point(x2, y1));
                    } else if (edge.equals("I15-I16") || edge.equals("I16-I7")) {
                        segmentPoints.add(new Point(x1, y2));
                    } else if (edge.equals("I4-I5") || edge.equals("I13-I5") || edge.equals("I14-I13") || edge.equals("I19-I17") || edge.equals("I17-I5")) {
                        segmentPoints.add(new Point(x1, y2));
                    } else if (edge.equals("I5-I7")) {
                        segmentPoints.add(new Point(x2, y1));
                    } else if (edge.equals("I3-I18") || edge.equals("I18-I4") || edge.equals("I10-I3")) {
                        segmentPoints.add(new Point(x1, y2));
                    } else if (edge.equals("I9-I8") || edge.equals("I8-I6") || edge.equals("I12-I9")) {
                        segmentPoints.add(new Point(x1, y2));
                    } else if (edge.equals("I1-I11") || edge.equals("I11-I9") || edge.equals("I9-I2") || edge.equals("I2-I13") || edge.equals("I13-I3")) {
                        segmentPoints.add(new Point(x2, y1));
                    } else if (edge.equals("I12-I10") || edge.equals("I10-I14")) {
                        segmentPoints.add(new Point(x2, y1));
                    } else if (edge.equals("I6-I5")) {
                        segmentPoints.add(new Point(x2, y1));
                    } else {
                        segmentPoints.add(new Point(x2, y2));
                    }
                } else {
                    segmentPoints.add(new Point(x2, y2));
                }

                segmentPoints.add(new Point(x2, y2));

                for (int j = 0; j < segmentPoints.size() - 1; j++) {
                    Point sp1 = segmentPoints.get(j);
                    Point sp2 = segmentPoints.get(j + 1);
                    g2d.drawLine(sp1.x, sp1.y, sp2.x, sp2.y);
                }
            }

            Point start = graph.getCoordinates(path.get(0));
            Point end = graph.getCoordinates(path.get(path.size() - 1));
            g2d.setColor(Color.GREEN);
            int markerX = x + (int) (start.x * scale);
            int markerY = y + (int) (start.y * scale);
            int arrowSize = 30;
            int[] xPoints = { markerX, markerX - arrowSize, markerX - arrowSize };
            int[] yPoints = { markerY, markerY - arrowSize, markerY + arrowSize };
            g2d.fillPolygon(xPoints, yPoints, 3);

            g2d.setColor(Color.RED);
            markerX = x + (int) (end.x * scale);
            markerY = y + (int) (end.y * scale);
            xPoints = new int[]{ markerX, markerX - arrowSize, markerX - arrowSize };
            yPoints = new int[]{ markerY, markerY - arrowSize, markerY + arrowSize };
            g2d.fillPolygon(xPoints, yPoints, 3);
        }

        if (currentSearchResult != null) {
            Point location = graph.getCoordinates(currentSearchResult);
            if (location != null) {
                int markerX = x + (int) (location.x * scale);
                int markerY = y + (int) (location.y * scale);
                g.setColor(Color.RED);
                int arrowSize = 30;
                int[] xPoints = { markerX, markerX - arrowSize, markerX - arrowSize };
                int[] yPoints = { markerY, markerY - arrowSize, markerY + arrowSize };
                g.fillPolygon(xPoints, yPoints, 3);
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

        if (prevZoom == zoomFactor) return;

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