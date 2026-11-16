package vms;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/** Finds a visitor who is still inside and records the check-out time. */
class VisitorCheckOut extends BaseFrame {

    private final JTextField visitorId = UI.field();
    private final JLabel vName = valueLabel();
    private final JLabel vPhone = valueLabel();
    private final JLabel vPurpose = valueLabel();
    private final JLabel vMeet = valueLabel();
    private final JLabel vCheckIn = valueLabel();
    private final JLabel vCheckOut = valueLabel();
    private final JButton checkOutButton = UI.button("Check-Out", UI.ORANGE);
    private int visitId = -1;

    VisitorCheckOut(String user) {
        super(user);
        visitorId.setName("visitorId");
        vName.setName("vName");
        vPhone.setName("vPhone");
        vPurpose.setName("vPurpose");
        vMeet.setName("vMeet");
        vCheckIn.setName("vCheckIn");
        vCheckOut.setName("vCheckOut");
        checkOutButton.setName("checkOut");

        JPanel root = UI.transparent(new BorderLayout(0, 12));
        root.setBorder(UI.pad(12, 60, 16, 60));
        root.add(titleRow("Visitor Check-Out"), BorderLayout.NORTH);

        JPanel card = UI.card();
        card.setLayout(new GridBagLayout());
        UI.row(card, 0, 0, "Visitor ID:", visitorId, 170);
        JButton searchButton = UI.button("Search", UI.BLUE);
        searchButton.setName("searchButton");
        searchButton.setPreferredSize(new Dimension(80, 28));
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 2;
        g.gridy = 0;
        g.anchor = GridBagConstraints.WEST;
        g.insets = new Insets(9, 0, 9, 10);
        card.add(searchButton, g);

        g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 1;
        g.gridwidth = 4;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(8, 10, 8, 10);
        card.add(new JSeparator(), g);

        UI.row(card, 2, 0, "Visitor Name:", vName, 0);
        UI.row(card, 2, 1, "Phone Number:", vPhone, 0);
        UI.row(card, 3, 0, "Purpose of Visit:", vPurpose, 0);
        UI.row(card, 3, 1, "Person to Meet:", vMeet, 0);
        UI.row(card, 4, 0, "Check-In Time:", vCheckIn, 0);
        UI.row(card, 4, 1, "Check-Out Time:", vCheckOut, 0);

        JButton clear = UI.button("Clear", UI.SLATE);
        clear.setName("clear");
        checkOutButton.setPreferredSize(new Dimension(130, 30));
        clear.setPreferredSize(new Dimension(100, 30));
        JPanel buttons = UI.transparent(new FlowLayout(FlowLayout.LEFT, 12, 0));
        buttons.add(checkOutButton);
        buttons.add(clear);
        g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 5;
        g.gridwidth = 4;
        g.anchor = GridBagConstraints.WEST;
        g.insets = new Insets(16, 0, 4, 10);
        card.add(buttons, g);

        JPanel holder = UI.transparent(new BorderLayout());
        holder.add(card, BorderLayout.NORTH);
        root.add(holder, BorderLayout.CENTER);
        body.add(root);

        checkOutButton.setEnabled(false);
        searchButton.addActionListener(e -> search());
        visitorId.addActionListener(e -> search());
        checkOutButton.addActionListener(e -> checkOut());
        clear.addActionListener(e -> clearAll());
    }

    private void clearDetails() {
        vName.setText("");
        vPhone.setText("");
        vPurpose.setText("");
        vMeet.setText("");
        vCheckIn.setText("");
        vCheckOut.setText("");
        checkOutButton.setEnabled(false);
        visitId = -1;
    }

    private void clearAll() {
        visitorId.setText("");
        clearDetails();
        visitorId.requestFocus();
    }

    private void search() {
        clearDetails();
        String text = visitorId.getText().trim().toUpperCase();
        if (text.startsWith("V-")) {
            text = text.substring(2);
        }
        if (!text.matches("\\d+")) {
            UI.error(this, "Please enter a valid visitor ID, for example V-1001.");
            return;
        }
        String sql = "SELECT s.visit_id, v.name, v.phone, s.purpose, s.person_to_meet, s.check_in, s.status "
                + "FROM visits s JOIN visitors v ON v.visitor_id = s.visitor_id "
                + "WHERE v.visitor_id = ? ORDER BY s.check_in DESC, s.visit_id DESC";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(text));
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    UI.error(this, "No visitor found with this ID.");
                    return;
                }
                vName.setText(rs.getString(2));
                vPhone.setText(rs.getString(3));
                vPurpose.setText(rs.getString(4));
                vMeet.setText(rs.getString(5));
                vCheckIn.setText(UI.fullFormat(rs.getTimestamp(6)));
                if ("Inside".equals(rs.getString(7))) {
                    visitId = rs.getInt(1);
                    vCheckOut.setText(UI.fullFormat(Timestamp.valueOf(LocalDateTime.now())));
                    checkOutButton.setEnabled(true);
                } else {
                    UI.info(this, "This visitor has already checked out.");
                }
            }
        } catch (SQLException ex) {
            UI.error(this, "Could not search the visitor.\n" + ex.getMessage());
        }
    }

    private void checkOut() {
        if (visitId < 0) {
            return;
        }
        String sql = "UPDATE visits SET check_out = NOW(), status = 'Checked Out' WHERE visit_id = ? AND status = 'Inside'";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, visitId);
            int changed = ps.executeUpdate();
            UI.info(this, changed > 0 ? "Visitor checked out successfully." : "This visit was already closed.");
            clearAll();
        } catch (SQLException ex) {
            UI.error(this, "Could not check out the visitor.\n" + ex.getMessage());
        }
    }
}
