package vms;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/** Lets the logged-in user change the login password. */
class ChangePassword extends BaseFrame {

    private final JTextField username = UI.field();
    private final JPasswordField oldPassword = UI.passwordField();
    private final JPasswordField newPassword = UI.passwordField();
    private final JPasswordField confirmPassword = UI.passwordField();

    ChangePassword(String user) {
        super(user);
        username.setName("username");
        oldPassword.setName("oldPassword");
        newPassword.setName("newPassword");
        confirmPassword.setName("confirmPassword");
        username.setText(user);

        JPanel root = UI.transparent(new BorderLayout(0, 12));
        root.setBorder(UI.pad(12, 60, 16, 60));
        root.add(titleRow("Change Password"), BorderLayout.NORTH);

        JPanel card = UI.card();
        card.setLayout(new GridBagLayout());
        UI.row(card, 0, 0, "Username:", username, 185);
        UI.row(card, 1, 0, "Old Password:", oldPassword, 185);
        UI.row(card, 2, 0, "New Password:", newPassword, 185);
        UI.row(card, 3, 0, "Confirm Password:", confirmPassword, 185);

        JButton update = UI.button("Update Password", UI.BLUE);
        update.setName("update");
        update.setPreferredSize(new Dimension(300, 32));
        update.addActionListener(e -> update());
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 4;
        g.gridwidth = 2;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(16, 10, 4, 10);
        card.add(update, g);

        JPanel center = UI.transparent(new GridBagLayout());
        center.add(card);
        root.add(center, BorderLayout.CENTER);
        body.add(root);
    }

    private void update() {
        String name = username.getText().trim();
        String oldPass = new String(oldPassword.getPassword());
        String newPass = new String(newPassword.getPassword());
        String confirm = new String(confirmPassword.getPassword());

        if (name.isEmpty() || oldPass.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) {
            UI.error(this, "Please fill in all the fields.");
            return;
        }
        if (newPass.length() < 4) {
            UI.error(this, "The new password must be at least 4 characters long.");
            return;
        }
        if (!newPass.equals(confirm)) {
            UI.error(this, "New password and confirm password do not match.");
            return;
        }
        String sql = "UPDATE login SET password = ? WHERE username = ? AND password = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, newPass);
            ps.setString(2, name);
            ps.setString(3, oldPass);
            if (ps.executeUpdate() > 0) {
                UI.info(this, "Password changed successfully.");
                oldPassword.setText("");
                newPassword.setText("");
                confirmPassword.setText("");
            } else {
                UI.error(this, "Username or old password is incorrect.");
            }
        } catch (SQLException ex) {
            UI.error(this, "Could not change the password.\n" + ex.getMessage());
        }
    }
}
