package vms;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Entry point of the Visitor Management System. Run this class to start the application. */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
            // the default look and feel is fine too
        }
        SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }
}
