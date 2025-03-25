import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MapViewer extends JFrame {
    private JTextField searchField;

    public MapViewer(String imagePath) {
        setTitle("TMU Campus Map");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        MapPanel mapPanel = new MapPanel(imagePath);

        // Create search components
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        // Add action listener for the search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = searchField.getText().trim();
                mapPanel.searchLocation(query);
            }
        });

        // Add components to the search panel
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Add components to the frame
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
