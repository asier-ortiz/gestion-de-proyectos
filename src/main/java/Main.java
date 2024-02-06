import view.Principal;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Menu.font", new Font("monospaced", Font.BOLD, 14));
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        var principal = new Principal();
        principal.setContentPane(principal.getPrincipalWindow());
        principal.setVisible(true);
    }
}