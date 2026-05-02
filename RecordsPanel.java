import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * RecordsPanel.java
 * ─────────────────────────────────────────────────────────────────
 * Displays all saved shape records in a JTable.
 * Users can refresh the list or delete selected rows.
 * ─────────────────────────────────────────────────────────────────
 */
public class RecordsPanel extends JPanel {

    private DatabaseManager db;           // Reference to the DB manager
    private DefaultTableModel tableModel; // The data model behind the JTable
    private JTable table;                 // The visible table widget

    // Column headers shown in the table
    private static final String[] COLUMNS = {
        "ID", "Shape", "Dimensions", "Area", "Perimeter", "Saved At"
    };

    public RecordsPanel(DatabaseManager db) {
        this.db = db;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 255));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        buildUI();
        loadRecords(); // Populate the table immediately
    }

    /** Builds the panel: title, table, and action buttons */
    private void buildUI() {
        // ── Title ─────────────────────────────────────────────
        JLabel title = new JLabel("📋  Saved Shape Records", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(50, 60, 120));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // ── Table model (non-editable cells) ──────────────────
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // Prevent users from editing cells directly
            }
        };

        table = new JTable(tableModel);
        styleTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 190, 220), 1));
        add(scrollPane, BorderLayout.CENTER);

        // ── Button panel ──────────────────────────────────────
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnPanel.setBackground(new Color(245, 247, 255));

        JButton refreshBtn = createButton("🔄  Refresh", new Color(70, 130, 200));
        JButton deleteBtn  = createButton("🗑  Delete Selected", new Color(200, 70, 70));
        JButton exportBtn  = createButton("📄  Show Count", new Color(100, 160, 100));

        btnPanel.add(refreshBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(exportBtn);
        add(btnPanel, BorderLayout.SOUTH);

        // ── Button actions ────────────────────────────────────
        refreshBtn.addActionListener(e -> loadRecords());

        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                    "Please select a row to delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // ID is in column 0
            int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete record ID=" + id + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (db.deleteShape(id)) {
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this,
                        "Record deleted successfully.", "Deleted",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        exportBtn.addActionListener(e -> {
            int count = tableModel.getRowCount();
            JOptionPane.showMessageDialog(this,
                "Total saved shapes: " + count,
                "Record Count", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    /**
     * loadRecords() – Clears the table and re-fetches all rows
     * from the database.  Called on tab switch and Refresh click.
     */
    public void loadRecords() {
        tableModel.setRowCount(0); // Clear existing rows
        List<String[]> rows = db.getAllShapes();
        for (String[] row : rows) {
            tableModel.addRow(row);
        }
        // Show a subtle row-count message
        System.out.println("[Records] Loaded " + rows.size() + " shape(s).");
    }

    /** Applies visual styling to the JTable */
    private void styleTable() {
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setGridColor(new Color(200, 210, 230));
        table.setShowGrid(true);
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setSelectionForeground(Color.DARK_GRAY);
        table.setFillsViewportHeight(true);

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setBackground(new Color(70, 100, 180));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        // Column widths
        int[] widths = { 40, 90, 200, 90, 90, 150 };
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // Alternating row colours via custom renderer
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(
                        t, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0
                            ? Color.WHITE
                            : new Color(235, 240, 255));
                }
                setHorizontalAlignment(col >= 3 && col <= 4
                        ? SwingConstants.CENTER : SwingConstants.LEFT);
                return c;
            }
        });
    }

    /** Helper – creates a styled button */
    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 36));

        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            Color original = color;
            @Override public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.darker());
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(original);
            }
        });
        return btn;
    }
}
