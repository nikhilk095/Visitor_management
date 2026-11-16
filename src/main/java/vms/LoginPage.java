
package vms;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Login screen using static authentication.
 * No database connection required.
 */
class LoginPage extends BaseFrame {

    // Static admin credentials
    private static final String ADMIN_LOGIN_ID = "VMS101";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    private final JTextField loginId = UI.field();
    private final JTextField username = UI.field();
    private final JPasswordField password = UI.passwordField();
    private final JButton loginButton = UI.button("Login", UI.GREEN);

    LoginPage() {
        super("");

        loginId.setName("loginId");
        username.setName("username");
        password.setName("password");
        loginButton.setName("login");

        JPanel card = UI.card();
        card.setLayout(new GridBagLayout());

        GridBagConstraints g = new GridBagConstraints();

        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 2;
        g.anchor = GridBagConstraints.WEST;
        g.insets = new Insets(0, 10, 8, 10);

        card.add(
                UI.label("Login", Font.BOLD, 22, UI.NAVY),
                g
        );

        UI.row(card, 1, 0, "Login ID:", loginId, 172);
        UI.row(card, 2, 0, "Username:", username, 172);
        UI.row(card, 3, 0, "Password:", password, 172);

        loginButton.setPreferredSize(new Dimension(264, 30));

        g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 4;
        g.gridwidth = 2;
        g.insets = new Insets(14, 10, 10, 10);
        g.fill = GridBagConstraints.HORIZONTAL;

        card.add(loginButton, g);

        g = new GridBagConstraints();
        g.gridx = 0;
        g.gridwidth = 2;
        g.insets = new Insets(1, 10, 1, 10);
        g.gridy = 5;

        card.add(
                UI.label(
                        "Enter your admin login credentials.",
                        Font.PLAIN, 11, UI.GRAY
                ),
                g
        );

        body.setLayout(new GridBagLayout());
        body.add(card);

        loginButton.addActionListener(e -> doLogin());

        getRootPane().setDefaultButton(loginButton);
    }

    private void doLogin() {

        String id = loginId.getText().trim();
        String name = username.getText().trim();
        String pass = new String(password.getPassword());

        // Validate empty fields
        if (id.isEmpty() || name.isEmpty() || pass.isEmpty()) {
            UI.error(
                    this,
                    "Please enter login ID, username and password."
            );
            return;
        }

        // Static authentication
        if (
                ADMIN_LOGIN_ID.equals(id) &&
                        ADMIN_USERNAME.equals(name) &&
                        ADMIN_PASSWORD.equals(pass)
        ) {

            // Authentication successful
            new Dashboard(name).setVisible(true);

            // Close login page
            dispose();

        } else {

            // Authentication failed
            UI.error(this, "Invalid user");

            password.setText("");
        }
    }
}
