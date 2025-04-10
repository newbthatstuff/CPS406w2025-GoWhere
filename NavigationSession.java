import javax.swing.JOptionPane;

public class NavigationSession {
    private Location current;
    private Location destination;
    private RouteEngine routeEngine;
    private MapPanel mapPanel;

    public NavigationSession(RouteEngine routeEngine, MapPanel mapPanel) {
        this.routeEngine = routeEngine;
        this.mapPanel = mapPanel;
    }

    public void setCurrent(Location current) {
        this.current = current;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public void start() {
        if (current == null || destination == null) {
            JOptionPane.showMessageDialog(null, "Please set both current location and destination.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Route route = routeEngine.calculateRoute(current, destination);
        mapPanel.setRoute(route);
        mapPanel.repaint();
    }
}