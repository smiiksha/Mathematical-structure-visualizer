import javax.swing.*;

/**
 * Main.java
 * ─────────────────────────────────────────────────────────────────
 * Entry point of the Mathematical Structures Visualizer.
 *
 * This is the FIRST class Java runs when you type:
 *     java -cp "lib/*;." Main          (Windows)
 *     java -cp "lib/*:." Main          (Mac / Linux)
 *
 * What happens here:
 *   1. A DatabaseManager is created (opens shapes.db, creates table).
 *   2. The GUI is launched on the Event Dispatch Thread (EDT).
 *      → SwingUtilities.invokeLater() is the standard safe way to
 *        start a Swing GUI; it ensures the UI is built on the
 *        correct thread.
 * ─────────────────────────────────────────────────────────────────
 */
public class Main {

    public static void main(String[] args) {

        // ── Step 1: Set a modern Look-and-Feel ───────────────
        // "Nimbus" gives a cleaner appearance than the default Java UI.
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus isn't available, use the system default — no problem.
            System.out.println("[UI] Nimbus L&F not found, using system default.");
        }

        // ── Step 2: Connect to the database ──────────────────
        // This opens (or creates) shapes.db in the current folder.
        DatabaseManager db = new DatabaseManager();

        // ── Step 3: Launch the GUI on the Event Dispatch Thread ──
        // SwingUtilities.invokeLater() schedules the Runnable to run
        // after Java finishes its startup tasks.  This is best practice
        // for all Swing applications.
        SwingUtilities.invokeLater(() -> {
            new MainGUI(db);
            System.out.println("[App] Mathematical Structures Visualizer started.");
        });
    }
}
