import javax.swing.JOptionPane;

public class Assistant {
    public void speak(String message) {
        JOptionPane.showMessageDialog(null, message, "Navigation Instruction", JOptionPane.INFORMATION_MESSAGE);
    }

    public String listen() {
        return JOptionPane.showInputDialog("Enter location:");
    }
}