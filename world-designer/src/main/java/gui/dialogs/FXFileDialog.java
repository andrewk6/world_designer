package gui.dialogs;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.concurrent.CompletableFuture;

/**
 * Provides native-style file dialogs (Open/Save) for Swing apps using JavaFX FileChooser.
 * This class wraps the FX FileChooser in a blocking modal JDialog so it behaves like JFileChooser.
 */
public class FXFileDialog {

    static {
        // Initialize JavaFX toolkit once (required for using FX components)
        new JFXPanel();
    }

    /**
     * Opens a native Open dialog filtered for .world files.
     * Blocks until user picks a file or cancels.
     */
    public static File openWorldFile(Component parent) {
        return showFileChooser(parent, FileDialogType.OPEN);
    }

    /**
     * Opens a native Save dialog filtered for .world files.
     * Blocks until user picks a file or cancels.
     */
    public static File saveWorldFile(Component parent) {
        return showFileChooser(parent, FileDialogType.SAVE);
    }

    private enum FileDialogType { OPEN, SAVE }

    private static File showFileChooser(Component parent, FileDialogType type) {
        CompletableFuture<File> result = new CompletableFuture<>();

        // Create a zero-size modal dialog to block Swing thread
        JDialog blocker = new JDialog(
                SwingUtilities.getWindowAncestor(parent),
                (type == FileDialogType.OPEN ? "Open World" : "Save World"),
                Dialog.ModalityType.APPLICATION_MODAL
        );
        blocker.setSize(0, 0);
        blocker.setUndecorated(true);
        blocker.setLocationRelativeTo(parent);

        // Run the FileChooser on the JavaFX thread
        Platform.runLater(() -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle(type == FileDialogType.OPEN ? "Open World File" : "Save World File");

            //  Set the extension filter for .world files
            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("World Files (*.world)", "*.world"),
                    new FileChooser.ExtensionFilter("All Files", "*.*")
            );

            File file = (type == FileDialogType.OPEN)
                    ? chooser.showOpenDialog(new Stage())
                    : chooser.showSaveDialog(new Stage());

            // Ensure file has .world extension when saving
            if (file != null && type == FileDialogType.SAVE && !file.getName().toLowerCase().endsWith(".world")) {
                file = new File(file.getParentFile(), file.getName() + ".world");
            }

            result.complete(file);
            SwingUtilities.invokeLater(blocker::dispose);
        });

        // Block Swing thread until dialog completes
        blocker.setVisible(true);

        try {
            return result.get();
        } catch (Exception e) {
            return null;
        }
    }
}
