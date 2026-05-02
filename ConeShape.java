import java.awt.*;

/**
 * ConeShape.java
 * ─────────────────────────────────────────────────────────────────
 * Represents a Cone (drawn as a 2D side-view with a 3D elliptical
 * base to give a sense of depth).
 *
 * Cone formulas used:
 *   Slant height (l) = √(r² + h²)
 *   Total Surface Area = π·r·(r + l)
 *   Volume             = (1/3)·π·r²·h
 * ─────────────────────────────────────────────────────────────────
 */
public class ConeShape extends Shape {

    private double radius; // Radius of the circular base
    private double height; // Height of the cone (apex to centre of base)

    public ConeShape(double radius, double height) {
        super("Cone");
        this.radius = radius;
        this.height = height;
    }

    public double getRadius() { return radius; }
    public double getConeHeight() { return height; }

    /**
     * draw() – Draws a 2D representation of a 3D cone:
     *   - An isosceles triangle for the body
     *   - An ellipse at the base for the 3D look
     *   - Dimension lines and labels
     */
    @Override
    public void draw(Graphics2D g2d, int px, int py, int pWidth, int pHeight) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        int padding = 50;
        // Reserve space: cone occupies ~65% width, ~70% height of panel
        int coneW = (int)((pWidth  - padding * 2) * 0.65);
        int coneH = (int)((pHeight - padding * 2) * 0.70);
        if (coneW < 30) coneW = 30;
        if (coneH < 30) coneH = 30;

        // Ellipse height for the 3D base effect
        int ellipseH = Math.max(20, coneH / 8);

        // Position: centre horizontally, leave room at bottom for labels
        int baseY  = py + padding + coneH;           // y of base centre
        int apexX  = px + pWidth / 2;                // x of apex (top point)
        int apexY  = py + padding;                   // y of apex
        int baseX1 = apexX - coneW / 2;              // left base corner
        int baseX2 = apexX + coneW / 2;              // right base corner

        // ── Cone body (triangle) ──────────────────────────────
        int[] xPts = { apexX,  baseX1, baseX2 };
        int[] yPts = { apexY,  baseY,  baseY  };

        // Shadow / gradient effect using GradientPaint
        GradientPaint gradient = new GradientPaint(
                baseX1, baseY, new Color(255, 140, 0, 200),
                baseX2, apexY, new Color(255, 200, 80, 160));
        g2d.setPaint(gradient);
        g2d.fillPolygon(xPts, yPts, 3);

        // Border
        g2d.setColor(new Color(180, 80, 0));
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawLine(apexX, apexY, baseX1, baseY);
        g2d.drawLine(apexX, apexY, baseX2, baseY);

        // ── Elliptical base ───────────────────────────────────
        // Drawn AFTER the triangle so it appears in front
        g2d.setColor(new Color(210, 110, 0, 220));
        g2d.fillOval(baseX1, baseY - ellipseH / 2, coneW, ellipseH);
        g2d.setColor(new Color(140, 60, 0));
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawOval(baseX1, baseY - ellipseH / 2, coneW, ellipseH);

        // ── Apex dot ──────────────────────────────────────────
        g2d.setColor(Color.RED);
        g2d.fillOval(apexX - 4, apexY - 4, 8, 8);

        // ── Height line (dashed) ──────────────────────────────
        g2d.setColor(new Color(150, 0, 0));
        g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10, new float[]{5, 4}, 0));
        g2d.drawLine(apexX, apexY, apexX, baseY);

        // ── Radius line ───────────────────────────────────────
        g2d.setColor(new Color(0, 100, 150));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawLine(apexX, baseY, baseX2, baseY);

        // ── Labels ───────────────────────────────────────────
        g2d.setFont(new Font("Arial", Font.BOLD, 13));

        // Height label
        g2d.setColor(new Color(150, 0, 0));
        g2d.drawString("h = " + height, apexX + 6, (apexY + baseY) / 2);

        // Radius label
        g2d.setColor(new Color(0, 80, 130));
        g2d.drawString("r = " + radius, apexX + (coneW / 4), baseY + ellipseH / 2 + 16);

        // ── Formula labels ────────────────────────────────────
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.setColor(Color.DARK_GRAY);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawString(String.format("Surface Area = π·r(r+l) = %.2f", getArea()),
                       px + 8, py + pHeight - 28);
        g2d.drawString(String.format("Volume = ⅓·π·r²·h  = %.2f",     getVolume()),
                       px + 8, py + pHeight - 10);
    }

    /**
     * Slant height: l = √(r² + h²)
     * Total surface area = π·r·(r + l)
     */
    @Override
    public double getArea() {
        double slant = Math.sqrt(radius * radius + height * height);
        return Math.PI * radius * (radius + slant);
    }

    /** Base circumference (perimeter of the circular base) */
    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    /** Volume = (1/3) × π × r² × h */
    public double getVolume() {
        return (1.0 / 3.0) * Math.PI * radius * radius * height;
    }

    @Override
    public String getDimensionsString() {
        return "radius=" + radius + ", height=" + height;
    }
}
