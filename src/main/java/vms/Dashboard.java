package vms;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** Main screen shown after a successful login: six tiles that open the other modules. */
class Dashboard extends BaseFrame {

    Dashboard(String user) {
        super(user);
        body.setLayout(new GridBagLayout());

        JLabel welcome = UI.label("Welcome to Dashboard", Font.BOLD, 24, UI.DARK);
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.insets = new Insets(0, 0, 20, 0);
        body.add(welcome, g);

        JPanel grid = new JPanel(new GridLayout(2, 3, 30, 25));
        grid.setOpaque(false);
        grid.setPreferredSize(new Dimension(810, 235));
        grid.add(new Tile("VISITOR ENTRY", "Register New Visitor", () -> open(new VisitorEntry(user))));
        grid.add(new Tile("VISITOR RECORDS", "View & Search Visitors", () -> open(new VisitorRecords(user))));
        grid.add(new Tile("CHECK-OUT", "Mark Visitor Exit", () -> open(new VisitorCheckOut(user))));
        grid.add(new Tile("CHANGE PASSWORD", "Update Your Password", () -> open(new ChangePassword(user))));
        grid.add(new Tile("ABOUT / CONTACT", "Project Information & Contact", () -> open(new AboutContact(user))));
        grid.add(new Tile("LOGOUT", "Return to Login Screen", this::logout));
        g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 1;
        body.add(grid, g);
    }

    private void open(BaseFrame next) {
        next.setVisible(true);
        dispose();
    }

    private void logout() {
        int answer = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout",
                JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            new LoginPage().setVisible(true);
            dispose();
        }
    }

    /** A clickable white card with a title and a one-line description. */
    private static final class Tile extends JPanel {
        private static final Color HOVER = new Color(234, 244, 252);

        Tile(String title, String subtitle, Runnable action) {
            super(new GridBagLayout());
            setName(title);
            setBackground(Color.WHITE);
            setBorder(new LineBorder(UI.LINE, 1, true));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JPanel text = UI.transparent(new GridLayout(2, 1, 0, 6));
            JLabel a = UI.label(title, Font.BOLD, 15, UI.DARK);
            JLabel b = UI.label(subtitle, Font.PLAIN, 11, UI.GRAY);
            a.setHorizontalAlignment(JLabel.CENTER);
            b.setHorizontalAlignment(JLabel.CENTER);
            text.add(a);
            text.add(b);
            add(text);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    action.run();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(HOVER);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(Color.WHITE);
                }
            });
        }
    }
}
