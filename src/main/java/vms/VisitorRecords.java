package vms;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Shows every visit in a table with a search box (name, phone or visitor ID). */
class VisitorRecords extends BaseFrame {

    private static final String[] COLUMNS = {"ID", "Visitor Name", "Phone", "Purpose", "Person to Meet", "Check-In", "Check-Out", "Status"};
    private static final int[] WIDTHS = {70, 150, 115, 150, 150, 100, 100, 105};

    private final JTextField search = UI.field();
    private final DefaultTableModel model = new DefaultTableModel(COLUMNS, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    VisitorRecords(String user) {
        super(user);
        search.setName("search");
        table.setName("table");

        JPanel root = UI.transparent(new BorderLayout(0, 10));
        root.setBorder(UI.pad(12, 40, 14, 40));

        JPanel top = UI.transparent(new BorderLayout(0, 8));
        top.add(titleRow("Visitor Records"), BorderLayout.NORTH);
        JPanel bar = UI.transparent(new FlowLayout(FlowLayout.LEFT, 8, 0));
        bar.add(UI.label("Search:", Font.BOLD, 13, new Color(40, 40, 40)));
        search.setPreferredSize(new Dimension(235, 26));
        search.setToolTipText("Type a name, phone number or visitor ID");
        bar.add(search);
        JButton searchButton = UI.button("Search", UI.BLUE);
        JButton refreshButton = UI.button("Refresh", UI.SLATE);
        searchButton.setName("searchButton");
        refreshButton.setName("refreshButton");
        searchButton.setPreferredSize(new Dimension(90, 26));
        refreshButton.setPreferredSize(new Dimension(90, 26));
        bar.add(searchButton);
        bar.add(refreshButton);
        top.add(bar, BorderLayout.CENTER);
        root.add(top, BorderLayout.NORTH);

        styleTable();
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(new Color(170, 175, 180)));
        scroll.getViewport().setBackground(Color.WHITE);
        root.add(scroll, BorderLayout.CENTER);
        body.add(root);

        searchButton.addActionListener(e -> load(search.getText().trim()));
        search.addActionListener(e -> load(search.getText().trim()));
        refreshButton.addActionListener(e -> {
            search.setText("");
            load("");
        });
        load("");
    }

    private void styleTable() {
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setPreferredSize(new Dimension(100, 28));
        for (int i = 0; i < WIDTHS.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(WIDTHS[i]);
        }
        table.getTableHeader().setDefaultRenderer((t, value, selected, focus, row, col) -> {
            JLabel l = new JLabel(String.valueOf(value));
            l.setOpaque(true);
            l.setBackground(new Color(52, 73, 94));
            l.setForeground(Color.WHITE);
            l.setFont(UI.font(Font.BOLD, 12));
            l.setBorder(new EmptyBorder(0, 8, 0, 8));
            return l;
        });
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean selected,
                                                           boolean focus, int row, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, value, selected, focus, row, col);
                l.setBorder(new EmptyBorder(0, 8, 0, 8));
                l.setFont(UI.font(Font.PLAIN, 12));
                l.setForeground(Color.BLACK);
                l.setBackground(selected ? new Color(184, 207, 229)
                        : row % 2 == 0 ? Color.WHITE : new Color(236, 240, 243));
                if (col == 7) {
                    l.setFont(UI.font(Font.BOLD, 12));
                    l.setForeground("Inside".equals(value) ? UI.GREEN : UI.GRAY);
                }
                return l;
            }
        });
    }

    private void load(String query) {
        model.setRowCount(0);
        String like = "%" + query + "%";
        String sql = "SELECT CONCAT('V-', v.visitor_id), v.name, v.phone, s.purpose, s.person_to_meet, "
                + "s.check_in, s.check_out, s.status "
                + "FROM visits s JOIN visitors v ON v.visitor_id = s.visitor_id "
                + "WHERE v.name LIKE ? OR v.phone LIKE ? OR CONCAT('V-', v.visitor_id) LIKE ? "
                + "ORDER BY s.check_in DESC, s.visit_id DESC";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                            rs.getString(5), UI.format(rs.getTimestamp(6)), UI.format(rs.getTimestamp(7)), rs.getString(8)});
                }
            }
        } catch (SQLException ex) {
            UI.error(this, "Could not load visitor records.\n" + ex.getMessage());
        }
    }
}
