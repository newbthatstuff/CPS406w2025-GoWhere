import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MapViewer extends JFrame {
    private JTextField currentField;
    private JTextField destField;
    private MapPanel mapPanel;
    private NavigationSession navigationSession;
    private UserInputHandler inputHandler;

    public MapViewer(String imagePath) {
        setTitle("TMU Campus Map");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        mapPanel = new MapPanel(imagePath);

        Map<String, Location> locations = new HashMap<>();
        for (Map.Entry<String, Point> entry : mapPanel.getGraph().getCoordinates().entrySet()) {
            if (!entry.getKey().matches("I\\d+")) {
                locations.put(entry.getKey(), new Location(entry.getValue().x, entry.getValue().y, entry.getKey()));
            }
        }
        System.out.println("Populated locations: " + locations.keySet());

        inputHandler = new UserInputHandler(locations);
        RouteEngine routeEngine = new RouteEngine(mapPanel.getGraph());
        navigationSession = new NavigationSession(routeEngine, mapPanel);

        JPanel searchPanel = new JPanel(new GridLayout(2, 3));
        currentField = new JTextField(20);
        destField = new JTextField(20);
        JButton directionButton = new JButton("Get Directions");

        directionButton.addActionListener(_ -> {
            String currentText = currentField.getText().trim();
            String destText = destField.getText().trim();
            System.out.println("Current location input: '" + currentText + "'");
            System.out.println("Destination input: '" + destText + "'");
            Location current = inputHandler.getLocationFromText(currentText);
            Location dest = inputHandler.getLocationFromText(destText);

            if (current == null || dest == null) {
                JOptionPane.showMessageDialog(this, "Invalid location(s) entered.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            navigationSession.setCurrent(current);
            navigationSession.setDestination(dest);
            navigationSession.start();
        });

        searchPanel.add(new JLabel("Current Location:"));
        searchPanel.add(currentField);
        searchPanel.add(new JLabel(""));
        searchPanel.add(new JLabel("Destination:"));
        searchPanel.add(destField);
        searchPanel.add(directionButton);

        setLayout(new BorderLayout());
        add(searchPanel, BorderLayout.NORTH);
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