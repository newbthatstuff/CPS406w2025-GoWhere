import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
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

        JButton voiceButtonCur = new JButton("Voice Input");
        voiceButtonCur.addActionListener(e -> startVoiceRecognition(currentField));
        JButton voiceButtonDest = new JButton("Voice Input");
        voiceButtonDest.addActionListener(e -> startVoiceRecognition(destField));

        JPanel searchPanel = new JPanel(new GridLayout(2, 3));
        currentField = new JTextField(20);
        destField = new JTextField(20);
        JButton directionButton = new JButton("Get Directions");

        directionButton.addActionListener(z -> {
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
        searchPanel.add(voiceButtonCur, BorderLayout.WEST);
        searchPanel.add(new JLabel(""));
        searchPanel.add(new JLabel("Destination:"));
        searchPanel.add(destField);
        searchPanel.add(voiceButtonDest, BorderLayout.WEST);
        searchPanel.add(directionButton);

        setLayout(new BorderLayout());
        add(searchPanel, BorderLayout.NORTH);
        add(mapPanel);
        pack();

        setSize(1280, 853);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void startVoiceRecognition(JTextField field) {
        try {
            ProcessBuilder pb = new ProcessBuilder("python", "voice_search.py");
            pb.redirectErrorStream(true);
            Process p = pb.start();

            // Read Python's output
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String output = reader.readLine();

            // Handle Python errors
            if (output == null) {
                JOptionPane.showMessageDialog(this, "Python script returned no output.");
            } else if (output.startsWith("ERROR")) {
                JOptionPane.showMessageDialog(this, "Voice error: " + output);
            } else {
                field.setText(output);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Java error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MapViewer("map.png"));
    }
}