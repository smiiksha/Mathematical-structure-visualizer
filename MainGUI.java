import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * MainGUI.java
 * ─────────────────────────────────────────────────────────────────
 * The main application window.  Contains:
 *   - A top title bar
 *   - Tab 1 "Draw Shape"  : shape selector, input fields, canvas
 *   - Tab 2 "View Records": the saved-shapes table (RecordsPanel)
 * ─────────────────────────────────────────────────────────────────
 */
public class MainGUI extends JFrame {

    // ── Core components ───────────────────────────────────────────
    private DatabaseManager db;        // Database connection
    private DrawPanel       drawPanel; // Custom drawing canvas
    private RecordsPanel    recordsPanel; // Records table

    // ── Input controls ────────────────────────────────────────────
    private JComboBox<String> shapeSelector; // Dropdown list
    private JPanel            inputPanel;    // Holds dimension fields
    private JTextField        field1, field2; // Dimension text boxes
    private JLabel            label1, label2; // Labels next to fields

    // ── Info labels ───────────────────────────────────────────────
    private JLabel areaLabel;      // Shows computed area
    private JLabel perimeterLabel; // Shows computed perimeter
    private JLabel statusLabel;    // Bottom status bar

    // ── Shape names shown in dropdown ─────────────────────────────
    private static final String[] SHAPES =
            { "-- Select Shape --", "Circle", "Square", "Rectangle", "Cone" };

    // ── Colours ───────────────────────────────────────────────────
    private static final Color PRIMARY   = new Color(52, 86, 180);
    private static final Color SECONDARY = new Color(245, 247, 255);
    private static final Color ACCENT    = new Color(70, 170, 100);
    private static final Color WARN      = new Color(200, 70, 70);

    public MainGUI(DatabaseManager db) {
        this.db = db;
        initWindow();
        buildUI();
        setVisible(true);
    }

    /** Sets window properties (title, size, close behaviour, etc.) */
    private void initWindow() {
        setTitle("📐  Mathematical Structures Visualizer");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(950, 620));
        setPreferredSize(new Dimension(1020, 680));

        // Custom close handler — closes DB connection before exiting
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                db.close();
                System.exit(0);
            }
        });

        // Centre the window on screen
        setLocationRelativeTo(null);
        pack();
    }

    /** Assembles the full UI: header + tabbed pane */
    private void buildUI() {
        setLayout(new BorderLayout());

        // ── App header ────────────────────────────────────────
        add(buildHeader(), BorderLayout.NORTH);

        // ── Tabbed pane ───────────────────────────────────────
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 13));
        tabs.setBackground(SECONDARY);

        // Tab 1 – Draw shape
        tabs.addTab("📐  Draw Shape", buildDrawTab());

        // Tab 2 – View records (populate when tab is selected)
        recordsPanel = new RecordsPanel(db);
        tabs.addTab("📋  View Records", recordsPanel);

        // Refresh records every time the user switches to tab 2
        tabs.addChangeListener(e -> {
            if (tabs.getSelectedIndex() == 1) {
                recordsPanel.loadRecords();
            }
        });

        add(tabs, BorderLayout.CENTER);

        // ── Status bar ────────────────────────────────────────
        statusLabel = new JLabel("  Ready — select a shape to begin.");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(80, 90, 120));
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 210, 230)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        add(statusLabel, BorderLayout.SOUTH);
    }

    /** Creates the gradient title header */
    private JPanel buildHeader() {
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(40, 70, 160),
                        getWidth(), 0, new Color(80, 140, 220));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(0, 64));
        header.setLayout(new BorderLayout());

        JLabel title = new JLabel("  📐  Mathematical Structures Visualizer",
                                  SwingConstants.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.CENTER);

        JLabel sub = new JLabel("Draw · Calculate · Save  ", SwingConstants.RIGHT);
        sub.setFont(new Font("Arial", Font.ITALIC, 13));
        sub.setForeground(new Color(200, 220, 255));
        header.add(sub, BorderLayout.EAST);

        return header;
    }

    // ─────────────────────────────────────────────────────────────
    //  TAB 1 – Draw Shape
    // ─────────────────────────────────────────────────────────────
    private JPanel buildDrawTab() {
        JPanel tab = new JPanel(new BorderLayout(10, 10));
        tab.setBackground(SECONDARY);
        tab.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        tab.add(buildControlPanel(), BorderLayout.WEST);  // Controls on left
        tab.add(buildCanvasArea(),   BorderLayout.CENTER); // Canvas in center

        return tab;
    }

    /** Left-side control panel: dropdown + inputs + buttons + stats */
    private JPanel buildControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 230), 1, true),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        panel.setPreferredSize(new Dimension(260, 0));

        // ── Section: Select Shape ─────────────────────────────
        panel.add(sectionLabel("1  ·  Select Shape"));
        panel.add(Box.createVerticalStrut(6));

        shapeSelector = new JComboBox<>(SHAPES);
        shapeSelector.setFont(new Font("Arial", Font.PLAIN, 14));
        shapeSelector.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        shapeSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
        // When shape changes → update input fields
        shapeSelector.addActionListener(e -> updateInputFields());
        panel.add(shapeSelector);

        panel.add(Box.createVerticalStrut(18));

        // ── Section: Enter Dimensions ─────────────────────────
        panel.add(sectionLabel("2  ·  Enter Dimensions"));
        panel.add(Box.createVerticalStrut(6));

        inputPanel = new JPanel(new GridLayout(0, 2, 8, 8));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        label1 = new JLabel("—");
        field1 = createField("0");
        label2 = new JLabel("—");
        field2 = createField("0");

        inputPanel.add(label1); inputPanel.add(field1);
        inputPanel.add(label2); inputPanel.add(field2);
        panel.add(inputPanel);

        panel.add(Box.createVerticalStrut(18));

        // ── Section: Actions ──────────────────────────────────
        panel.add(sectionLabel("3  ·  Actions"));
        panel.add(Box.createVerticalStrut(8));

        JButton drawBtn = actionButton("▶  Draw Shape", ACCENT);
        JButton saveBtn = actionButton("💾  Save to Database", PRIMARY);
        JButton clearBtn= actionButton("✖  Clear Canvas", WARN);

        panel.add(drawBtn);
        panel.add(Box.createVerticalStrut(8));
        panel.add(saveBtn);
        panel.add(Box.createVerticalStrut(8));
        panel.add(clearBtn);

        panel.add(Box.createVerticalStrut(18));

        // ── Section: Computed Values ──────────────────────────
        panel.add(sectionLabel("4  ·  Computed Values"));
        panel.add(Box.createVerticalStrut(8));

        areaLabel      = infoLabel("Area:       —");
        perimeterLabel = infoLabel("Perimeter:  —");
        panel.add(areaLabel);
        panel.add(Box.createVerticalStrut(4));
        panel.add(perimeterLabel);

        panel.add(Box.createVerticalGlue());

        // ── Button actions ────────────────────────────────────
        drawBtn.addActionListener(e -> handleDraw(false));
        saveBtn.addActionListener(e -> handleDraw(true));
        clearBtn.addActionListener(e -> {
            drawPanel.setShape(null);
            areaLabel.setText("Area:       —");
            perimeterLabel.setText("Perimeter:  —");
            setStatus("Canvas cleared.");
        });

        return panel;
    }

    /** Right-side canvas area */
    private JPanel buildCanvasArea() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 6));
        wrapper.setBackground(SECONDARY);

        JLabel hint = new JLabel(
            "  Shape will be drawn below after you click  ▶  Draw Shape",
            SwingConstants.LEFT);
        hint.setFont(new Font("Arial", Font.ITALIC, 12));
        hint.setForeground(new Color(130, 140, 170));
        wrapper.add(hint, BorderLayout.NORTH);

        drawPanel = new DrawPanel();
        wrapper.add(drawPanel, BorderLayout.CENTER);
        return wrapper;
    }

    // ─────────────────────────────────────────────────────────────
    //  CORE LOGIC
    // ─────────────────────────────────────────────────────────────

    /**
     * handleDraw() – Validates input, builds a Shape object,
     * draws it, and optionally saves it to the database.
     *
     * @param save  true  = draw AND save to DB
     *              false = draw only
     */
    private void handleDraw(boolean save) {
        String selected = (String) shapeSelector.getSelectedItem();
        if (selected == null || selected.startsWith("--")) {
            showError("Please select a shape from the dropdown first.");
            return;
        }

        // ── Parse numeric inputs ──────────────────────────────
        double val1 = 0, val2 = 0;
        try {
            val1 = Double.parseDouble(field1.getText().trim());
            if (val1 <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            showError(label1.getText() + " must be a positive number.");
            field1.requestFocus();
            return;
        }

        // Second field is only needed for Rectangle and Cone
        if (selected.equals("Rectangle") || selected.equals("Cone")) {
            try {
                val2 = Double.parseDouble(field2.getText().trim());
                if (val2 <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                showError(label2.getText() + " must be a positive number.");
                field2.requestFocus();
                return;
            }
        }

        // ── Build the appropriate Shape object ────────────────
        Shape shape;
        switch (selected) {
            case "Circle":    shape = new CircleShape(val1);               break;
            case "Square":    shape = new SquareShape(val1);               break;
            case "Rectangle": shape = new RectangleShape(val1, val2);      break;
            case "Cone":      shape = new ConeShape(val1, val2);           break;
            default: return;
        }

        // ── Update canvas ─────────────────────────────────────
        drawPanel.setShape(shape);

        // ── Update computed-values labels ─────────────────────
        areaLabel.setText(String.format("Area:       %.4f", shape.getArea()));
        perimeterLabel.setText(String.format("Perimeter:  %.4f", shape.getPerimeter()));

        // ── Optionally save to DB ─────────────────────────────
        if (save) {
            boolean ok = db.saveShape(
                    shape.getName(),
                    shape.getDimensionsString(),
                    shape.getArea(),
                    shape.getPerimeter());
            if (ok) {
                setStatus("✅  " + selected + " drawn and saved to database!");
                JOptionPane.showMessageDialog(this,
                    selected + " saved to database.\n"
                    + "Switch to 'View Records' tab to see all saved shapes.",
                    "Saved", JOptionPane.INFORMATION_MESSAGE);
            } else {
                setStatus("⚠  Shape drawn but database save failed.");
            }
        } else {
            setStatus("✅  " + selected + " drawn. Click 'Save to Database' to store it.");
        }
    }

    /**
     * updateInputFields() – Called when user changes the dropdown.
     * Shows the correct labels and hides unused fields.
     */
    private void updateInputFields() {
        String selected = (String) shapeSelector.getSelectedItem();
        if (selected == null) return;

        switch (selected) {
            case "Circle":
                showFields("Radius:", true, "—", false);
                break;
            case "Square":
                showFields("Side length:", true, "—", false);
                break;
            case "Rectangle":
                showFields("Length:", true, "Width:", true);
                break;
            case "Cone":
                showFields("Radius:", true, "Height:", true);
                break;
            default:
                showFields("—", false, "—", false);
                break;
        }

        // Reset canvas and stats when shape type changes
        drawPanel.setShape(null);
        areaLabel.setText("Area:       —");
        perimeterLabel.setText("Perimeter:  —");
    }

    /** Shows/hides the two input field rows */
    private void showFields(String lbl1, boolean show1,
                             String lbl2, boolean show2) {
        label1.setText(lbl1); label1.setVisible(show1); field1.setVisible(show1);
        label2.setText(lbl2); label2.setVisible(show2); field2.setVisible(show2);
        field1.setText(""); field2.setText("");
        inputPanel.revalidate();
        inputPanel.repaint();
    }

    // ─────────────────────────────────────────────────────────────
    //  UI HELPER METHODS
    // ─────────────────────────────────────────────────────────────

    /** Creates a styled section-header label */
    private JLabel sectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setForeground(PRIMARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    /** Creates an info label (area / perimeter display) */
    private JLabel infoLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Monospaced", Font.PLAIN, 13));
        lbl.setForeground(new Color(50, 60, 110));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    /** Creates a styled text-input field */
    private JTextField createField(String defaultText) {
        JTextField tf = new JTextField(defaultText);
        tf.setFont(new Font("Arial", Font.PLAIN, 14));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 190, 220)),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
        return tf;
    }

    /** Creates a full-width action button with hover */
    private JButton actionButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(color.darker()); }
            @Override public void mouseExited (MouseEvent e) { btn.setBackground(color); }
        });
        return btn;
    }

    private void setStatus(String msg) {
        statusLabel.setText("  " + msg);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Input Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
