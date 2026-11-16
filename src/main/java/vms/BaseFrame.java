package vms;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

/** Common window used by every screen: logo header on top, content in the middle, footer at the bottom. */
abstract class BaseFrame extends JFrame {

    /** Username of the logged-in user (empty on the login screen). */
    protected final String user;
    /** Each screen puts its own content here. */
    protected final JPanel body = new JPanel(new BorderLayout());

    BaseFrame(String user) {
        super("Visitor Management System");
        this.user = user;
        ImageIcon icon = UI.logo(32, 32);
        if (icon != null) {
            setIconImage(icon.getImage());
        }
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1020, 600);
        setMinimumSize(new Dimension(900, 560));
        setLocationRelativeTo(null);
        body.setBackground(UI.BG);
        add(UI.header(), BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);
        add(UI.footer(), BorderLayout.SOUTH);
    }

    /** Screen title on the left and a "Back to Dashboard" button on the right. */
    protected JPanel titleRow(String text) {
        JPanel row = UI.transparent(new BorderLayout());
        JButton back = UI.button("< Back to Dashboard", UI.SLATE);
        back.setName("back");
        back.setPreferredSize(new Dimension(190, 28));
        back.addActionListener(e -> {
            new Dashboard(user).setVisible(true);
            dispose();
        });
        JPanel right = UI.transparent(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.add(back);
        row.add(UI.label(text, Font.BOLD, 22, UI.NAVY), BorderLayout.WEST);
        row.add(right, BorderLayout.EAST);
        return row;
    }

    protected static JLabel valueLabel() {
        return UI.label("", Font.PLAIN, 13, java.awt.Color.BLACK);
    }
}
