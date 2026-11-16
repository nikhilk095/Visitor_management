package vms;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** Shared colours, fonts and small component factories so that every screen looks the same. */
final class UI {

    static final Color BLUE = new Color(41, 128, 185);
    static final Color DARK = new Color(44, 62, 80);
    static final Color NAVY = new Color(31, 67, 103);
    static final Color GREEN = new Color(39, 174, 96);
    static final Color ORANGE = new Color(211, 84, 0);
    static final Color SLATE = new Color(127, 140, 141);
    static final Color GRAY = new Color(110, 110, 110);
    static final Color BG = new Color(244, 246, 247);
    static final Color LINE = new Color(205, 210, 215);

    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
    private static final DateTimeFormatter DAY_TIME = DateTimeFormatter.ofPattern("dd MMM, hh:mm a", Locale.ENGLISH);
    static final DateTimeFormatter FULL = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", Locale.ENGLISH);

    private UI() {
    }

    static Font font(int style, int size) {
        return new Font("Segoe UI", style, size);
    }

    /** Shows only the time for today's visits and date + time for older ones. */
    static String format(Timestamp t) {
        if (t == null) {
            return "-";
        }
        return t.toLocalDateTime().toLocalDate().equals(LocalDate.now())
                ? t.toLocalDateTime().format(TIME)
                : t.toLocalDateTime().format(DAY_TIME);
    }

    static String fullFormat(Timestamp t) {
        return t == null ? "-" : t.toLocalDateTime().format(FULL);
    }

    static ImageIcon logo(int w, int h) {
        URL url = UI.class.getResource("/logo.png");
        if (url == null) {
            return null;
        }
        Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    static JLabel label(String text, int style, int size, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font(style, size));
        l.setForeground(color);
        return l;
    }

    private static void styleField(JComponent f) {
        f.setFont(font(Font.PLAIN, 13));
        f.setBorder(new CompoundBorder(new LineBorder(new Color(160, 165, 170)), new EmptyBorder(3, 7, 3, 7)));
    }

    static JTextField field() {
        JTextField f = new JTextField();
        styleField(f);
        return f;
    }

    static JPasswordField passwordField() {
        JPasswordField f = new JPasswordField();
        styleField(f);
        return f;
    }

    static JComboBox<String> combo(String... items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setFont(font(Font.PLAIN, 13));
        c.setBackground(Color.WHITE);
        return c;
    }

    static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(new CompoundBorder(new LineBorder(LINE, 1, true), new EmptyBorder(18, 28, 18, 28)));
        return p;
    }

    /** Adds "label + component" to a GridBagLayout panel. col is 0 (left pair) or 1 (right pair). */
    static void row(JPanel p, int row, int col, String text, JComponent comp, int width) {
        GridBagConstraints g = new GridBagConstraints();
        g.gridy = row;
        g.insets = new Insets(9, 10, 9, 10);
        g.anchor = GridBagConstraints.WEST;
        g.gridx = col * 2;
        p.add(label(text, Font.BOLD, 13, new Color(40, 40, 40)), g);
        g.gridx = col * 2 + 1;
        if (width > 0) {
            comp.setPreferredSize(new Dimension(width, 28));
        }
        p.add(comp, g);
    }

    static JButton button(String text, Color color) {
        return new RoundedButton(text, color);
    }

    static void info(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Visitor Management System", JOptionPane.INFORMATION_MESSAGE);
    }

    static void error(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Visitor Management System", JOptionPane.ERROR_MESSAGE);
    }

    /** White band with the institute logo, followed by the blue project-title band. */
    static JPanel header() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 8));
        top.setBackground(Color.WHITE);
        ImageIcon icon = logo(78, 76);
        if (icon != null) {
            top.add(new JLabel(icon));
        }
        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        JLabel a = new JLabel("POORNIMA");
        a.setFont(new Font("Serif", Font.BOLD, 34));
        a.setForeground(new Color(150, 25, 35));
        JLabel b = new JLabel("INSTITUTE OF ENGINEERING & TECHNOLOGY");
        b.setFont(new Font("Serif", Font.PLAIN, 13));
        b.setForeground(NAVY);
        JLabel c = label("An Autonomous Institution approved by RTU, AICTE & UGC", Font.PLAIN, 10, GRAY);
        text.add(a);
        text.add(b);
        text.add(c);
        top.add(text);

        JPanel band = new JPanel();
        band.setLayout(new BoxLayout(band, BoxLayout.Y_AXIS));
        band.setBackground(BLUE);
        band.setBorder(new EmptyBorder(8, 0, 9, 0));
        JLabel title = label("VISITOR MANAGEMENT SYSTEM", Font.BOLD, 24, Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel sub = label("Poornima Institute of Engineering & Technology", Font.PLAIN, 12, new Color(222, 236, 248));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        band.add(title);
        band.add(sub);

        JPanel p = new JPanel(new BorderLayout());
        p.add(top, BorderLayout.NORTH);
        p.add(band, BorderLayout.CENTER);
        return p;
    }

    static JPanel footer() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 7));
        p.setBackground(DARK);
        p.add(label("Made by Nikhil Kumar", Font.BOLD, 11, Color.WHITE));
        return p;
    }

    /** A flat, rounded button that looks the same on every operating system. */
    private static final class RoundedButton extends JButton {
        private final Color base;
        private boolean hover;

        RoundedButton(String text, Color base) {
            super(text);
            this.base = base;
            setForeground(Color.WHITE);
            setFont(font(Font.BOLD, 13));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setMargin(new Insets(4, 6, 4, 6));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color c = !isEnabled() ? new Color(190, 195, 200)
                    : getModel().isPressed() ? base.darker().darker()
                    : hover ? base.darker() : base;
            g2.setColor(c);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static JPanel transparent(java.awt.LayoutManager lm) {
        JPanel p = new JPanel(lm);
        p.setOpaque(false);
        return p;
    }

    static javax.swing.border.Border pad(int top, int left, int bottom, int right) {
        return BorderFactory.createEmptyBorder(top, left, bottom, right);
    }
}
