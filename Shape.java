import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Shape.java
 * ─────────────────────────────────────────────────────────────────
 * This is an ABSTRACT BASE CLASS — a blueprint that all specific
 * shape classes (Circle, Square, etc.) must follow.
 *
 * "abstract" means you cannot create a Shape object directly.
 * Every shape must provide its own version of the abstract methods.
 * ─────────────────────────────────────────────────────────────────
 */
public abstract class Shape {

    // The name of the shape (e.g., "Circle", "Square")
    protected String name;

    /**
     * Constructor – sets the shape name.
     * Called by each subclass using super("ShapeName").
     */
    public Shape(String name) {
        this.name = name;
    }

    // Getter for the shape name
    public String getName() {
        return name;
    }

    /**
     * draw() – Each shape knows how to draw itself.
     * 
     * @param g2d    The Graphics2D object (Java's drawing tool)
     * @param x      Top-left X coordinate of the drawing area
     * @param y      Top-left Y coordinate of the drawing area
     * @param width  Width of the drawing panel
     * @param height Height of the drawing panel
     *
     * Every subclass MUST override this method.
     */
    public abstract void draw(Graphics2D g2d, int x, int y, int width, int height);

    /**
     * getArea() – Returns the calculated area of the shape.
     * Every subclass MUST implement its own formula.
     */
    public abstract double getArea();

    /**
     * getPerimeter() – Returns perimeter / circumference.
     * Every subclass MUST implement its own formula.
     */
    public abstract double getPerimeter();

    /**
     * getDimensionsString() – Returns a human-readable summary
     * of dimensions, e.g. "radius=5.0".
     * Used when saving data to the database.
     */
    public abstract String getDimensionsString();
}
