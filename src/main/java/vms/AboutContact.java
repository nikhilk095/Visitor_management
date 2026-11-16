package vms;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.JLabel;

/** Information about the project and its developer. */
class AboutContact extends BaseFrame {

    AboutContact(String user) {
        super(user);
        JPanel root = UI.transparent(new BorderLayout(0, 12));
        root.setBorder(UI.pad(12, 60, 16, 60));
        root.add(titleRow("About / Contact"), BorderLayout.NORTH);

        JPanel card = UI.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.add(line("Visitor Management System", Font.BOLD, 18, UI.NAVY));
        card.add(space(8));
        card.add(line("A desktop application to register visitors, track their check-in and check-out,", Font.PLAIN, 13, Color.DARK_GRAY));
        card.add(line("and keep visit records in a MySQL database.", Font.PLAIN, 13, Color.DARK_GRAY));
        card.add(space(14));
        card.add(line("Developed by:  NIKHIL KUMAR", Font.BOLD, 13, Color.BLACK));
        card.add(line("Department of Computer Engineering", Font.PLAIN, 13, Color.BLACK));
        card.add(line("Poornima Institute of Engineering & Technology, Jaipur", Font.PLAIN, 13, Color.BLACK));
        card.add(line("Mini Project Lab (Non-Syllabus Project), Academic Year 2026-27", Font.PLAIN, 13, Color.BLACK));
        card.add(space(14));
        card.add(line("Technology:  Java, Swing & AWT, JDBC, MySQL", Font.PLAIN, 13, Color.BLACK));
        card.add(line("Version 1.0", Font.PLAIN, 13, UI.GRAY));

        JPanel center = UI.transparent(new GridBagLayout());
        center.add(card);
        root.add(center, BorderLayout.CENTER);
        body.add(root);
    }

    private static JLabel line(String text, int style, int size, Color color) {
        JLabel l = UI.label(text, style, size, color);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private static Component space(int h) {
        return javax.swing.Box.createVerticalStrut(h);
    }
}
