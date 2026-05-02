import javax.swing.*;
import java.awt.*;

/**
 * DrawPanel.java
 * ─────────────────────────────────────────────────────────────────
 * A custom JPanel that draws whichever Shape object is currently set.
 *
 * How it works:
 *   1. MainGUI calls setShape(someShape) when the user clicks "Draw".
 *   2. setShape() calls repaint(), which triggers paintComponent().
 *   3. paintComponent() calls shape.draw() to render the shape.
 *
 * We extend JPanel and override paintComponent() — this is the
 * standard way to do custom drawing in Java Swing.
 * ─────────────────────────────────────────────────────────────────
 */
public class DrawPanel extends JPanel {

    // The shape currently shown (null = nothing drawn yet)
    private Shape currentShape = null;

    // Background gradient colours
    private static final Color BG_TOP    = new Color(245, 248, 255);
    private static final Color BG_BOTTOM = new Color(220, 230, 250);

    public DrawPanel() {
        setPreferredSize(new Dimension(480, 420));
        setBackground(BG_TOP);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 190, 220), 2, true),
            BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));
    }

    /**
     * setShape() – Called by MainGUI to display a new shape.
     * repaint() schedules a redraw of this panel.
     */
    public void setShape(Shape shape) {
        this.currentShape = shape;
        repaint(); // Triggers paintComponent() below
    }

    /**
     * paintComponent() – Java calls this automatically whenever
     * the panel needs to be redrawn (window resize, tab switch, etc.).
     *
     * IMPORTANT: Always call super.paintComponent(g) first to
     * erase the previous drawing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Cast to Graphics2D for richer drawing capabilities
        Graphics2D g2d = (Graphics2D) g;

        // ── Draw gradient background ───────────────────────────
        GradientPaint bg = new GradientPaint(
                0, 0, BG_TOP, 0, getHeight(), BG_BOTTOM);
        g2d.setPaint(bg);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // ── Draw subtle grid ───────────────────────────────────
        drawGrid(g2d);

        if (currentShape == null) {
            // ── Placeholder text when no shape is selected ─────
            g2d.setFont(new Font("Arial", Font.ITALIC, 16));
            g2d.setColor(new Color(160, 170, 200));
            String msg = "Select a shape and click  ▶  Draw Shape";
            FontMetrics fm = g2d.getFontMetrics();
            int msgX = (getWidth()  - fm.stringWidth(msg)) / 2;
            int msgY = getHeight() / 2;
            g2d.drawString(msg, msgX, msgY);
        } else {
            // ── Let the shape draw itself ───────────────────────
            currentShape.draw(g2d, 0, 0, getWidth(), getHeight());
        }
    }

    /** Draws a light dotted grid in the background for a graph-paper look */
    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(new Color(200, 210, 230, 120));
        g2d.setStroke(new BasicStroke(0.5f));
        int step = 30;
        for (int xg = 0; xg < getWidth(); xg += step) {
            g2d.drawLine(xg, 0, xg, getHeight());
        }
        for (int yg = 0; yg < getHeight(); yg += step) {
            g2d.drawLine(0, yg, getWidth(), yg);
        }
    }
}
