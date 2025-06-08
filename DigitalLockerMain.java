import javax.swing.*;
import java.awt.*;

public class DigitalLockerMain extends JFrame {
    public DigitalLockerMain(String username) {
        setTitle("Digital Locker - Welcome " + username);
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel welcome = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 20));
        add(welcome, BorderLayout.CENTER);
    }
}
