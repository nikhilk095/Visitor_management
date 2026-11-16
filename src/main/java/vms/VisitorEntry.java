package vms;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

/** Registers a new visitor and starts a visit (status "Inside"). */
class VisitorEntry extends BaseFrame {

    private final JTextField visitorId = UI.field();
    private final JTextField name = UI.field();
    private final JTextField phone = UI.field();
    private final JComboBox<String> idProof = UI.combo("Aadhaar Card", "Driving Licence", "Voter ID", "PAN Card", "Passport", "College ID");
    private final JTextField purpose = UI.field();
    private final JTextField personToMeet = UI.field();
    private final JComboBox<String> department = UI.combo("Computer Engineering", "Electronics & Communication",
            "Mechanical Engineering", "Civil Engineering", "Administration", "Admission Cell", "HR Department", "Accounts");
    private final JTextField checkIn = UI.field();
    private final Timer clock;

    VisitorEntry(String user) {
        super(user);
        visitorId.setName("visitorId");
        name.setName("name");
        phone.setName("phone");
        idProof.setName("idProof");
        purpose.setName("purpose");
        personToMeet.setName("personToMeet");
        department.setName("department");
        checkIn.setName("checkIn");
        visitorId.setEditable(false);
        checkIn.setEditable(false);

        JPanel root = UI.transparent(new BorderLayout(0, 12));
        root.setBorder(UI.pad(12, 60, 16, 60));
        root.add(titleRow("Visitor Entry"), BorderLayout.NORTH);

        JPanel card = UI.card();
        card.setLayout(new GridBagLayout());
        UI.row(card, 0, 0, "Visitor ID:", visitorId, 230);
        UI.row(card, 1, 0, "Visitor Name:", name, 230);
        UI.row(card, 2, 0, "Phone Number:", phone, 230);
        UI.row(card, 3, 0, "ID Proof:", idProof, 230);
        UI.row(card, 0, 1, "Purpose of Visit:", purpose, 280);
        UI.row(card, 1, 1, "Person to Meet:", personToMeet, 280);
        UI.row(card, 2, 1, "Department:", department, 280);
        UI.row(card, 3, 1, "Check-In Time:", checkIn, 280);

        JButton save = UI.button("Save", UI.GREEN);
        JButton clear = UI.button("Clear", UI.SLATE);
        save.setName("save");
        clear.setName("clear");
        save.addActionListener(e -> save());
        clear.addActionListener(e -> clearForm());
        JPanel buttons = UI.transparent(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttons.add(save);
        buttons.add(javax.swing.Box.createHorizontalStrut(12));
        buttons.add(clear);
        save.setPreferredSize(new java.awt.Dimension(110, 30));
        clear.setPreferredSize(new java.awt.Dimension(110, 30));
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 1;
        g.gridy = 4;
        g.gridwidth = 2;
        g.anchor = GridBagConstraints.WEST;
        g.insets = new Insets(18, 10, 4, 10);
        card.add(buttons, g);

        JPanel holder = UI.transparent(new BorderLayout());
        holder.add(card, BorderLayout.NORTH);
        root.add(holder, BorderLayout.CENTER);
        body.add(root);

        refreshId();
        updateClock();
        clock = new Timer(1000, e -> updateClock());
        clock.start();
    }

    @Override
    public void dispose() {
        clock.stop();
        super.dispose();
    }

    private void updateClock() {
        checkIn.setText(LocalDateTime.now().format(UI.FULL));
    }

    private void refreshId() {
        String sql = "SELECT COALESCE(MAX(visitor_id), 1000) + 1 FROM visitors";
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                visitorId.setText("V-" + rs.getInt(1));
            }
        } catch (SQLException ex) {
            visitorId.setText("V-");
        }
    }

    private void clearForm() {
        name.setText("");
        phone.setText("");
        purpose.setText("");
        personToMeet.setText("");
        idProof.setSelectedIndex(0);
        department.setSelectedIndex(0);
        refreshId();
        name.requestFocus();
    }

    private void save() {
        String n = name.getText().trim();
        String ph = phone.getText().trim();
        String pur = purpose.getText().trim();
        String meet = personToMeet.getText().trim();
        if (n.isEmpty() || pur.isEmpty() || meet.isEmpty()) {
            UI.error(this, "Please fill in the visitor name, purpose of visit and person to meet.");
            return;
        }
        if (!ph.matches("\\d{10}")) {
            UI.error(this, "Phone number must contain exactly 10 digits.");
            return;
        }

        Connection c = null;
        try {
            c = DBConnection.getConnection();
            c.setAutoCommit(false);
            int newId = 0;
            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO visitors (name, phone, id_proof, department) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, n);
                ps.setString(2, ph);
                ps.setString(3, (String) idProof.getSelectedItem());
                ps.setString(4, (String) department.getSelectedItem());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        newId = keys.getInt(1);
                    }
                }
            }
            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO visits (visitor_id, purpose, person_to_meet, check_in, status) VALUES (?, ?, ?, NOW(), 'Inside')")) {
                ps.setInt(1, newId);
                ps.setString(2, pur);
                ps.setString(3, meet);
                ps.executeUpdate();
            }
            c.commit();
            UI.info(this, "Visitor registered successfully.\nVisitor ID: V-" + newId);
            clearForm();
        } catch (SQLException ex) {
            if (c != null) {
                try {
                    c.rollback();
                } catch (SQLException ignored) {
                    // nothing more to do
                }
            }
            UI.error(this, "Could not save the visitor.\n" + ex.getMessage());
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException ignored) {
                    // nothing more to do
                }
            }
        }
    }
}
