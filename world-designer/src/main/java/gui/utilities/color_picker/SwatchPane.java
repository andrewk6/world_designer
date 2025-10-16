package gui.utilities.color_picker;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class SwatchPane extends JPanel {
    private final List<Color> swatches = new ArrayList<>();
    private final List<JPanel> swatchPanels = new ArrayList<>();
    private final int swatchSize, maxColumns;
    private Consumer<Color> onColorSelected;
    private JPanel selectedPanel;

//    public SwatchPane(int swatchSize, int columns) {
//        this.swatchSize = swatchSize;
//        setLayout(new GridLayout(0, columns, 5, 5)); // dynamic rows, fixed columns
//        setBorder(BorderFactory.createTitledBorder("Swatches"));
//        setBackground(Color.DARK_GRAY);
//    }
//
//    /**
//     * Adds a swatch using a Color object.
//     */
//    public void addSwatch(Color color) {
//        if (color == null) return;
//        swatches.add(color);
//
//        JPanel panel = new JPanel();
//        panel.setPreferredSize(new Dimension(swatchSize, swatchSize));
//        panel.setBackground(color);
//        panel.setBorder(new LineBorder(Color.GRAY, 1));
//
//        panel.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                setSelected(panel);
//                if (onColorSelected != null) onColorSelected.accept(color);
//            }
//        });
//
//        swatchPanels.add(panel);
//        add(panel);
//        revalidate();
//        repaint();
//    }
    
    public SwatchPane(int swatchSize, int maxColumns) {
        this.swatchSize = swatchSize;
        this.maxColumns = maxColumns;

        // FlowLayout will wrap automatically
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        setBackground(Color.DARK_GRAY);
    }

    public void addSwatch(Color color) {
        if (color == null) return;

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(swatchSize, swatchSize));
        panel.setMaximumSize(new Dimension(swatchSize, swatchSize));
        panel.setMinimumSize(new Dimension(swatchSize, swatchSize));
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));


        panel.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
              setSelected(panel);
              if (onColorSelected != null) onColorSelected.accept(color);
          }
      });

        swatches.add(color);
        swatchPanels.add(panel);
        add(panel);
        revalidate();
        repaint();
    }
    
    public void addSwatch(List<Color> swatches) {
    	System.out.println(swatches.toString());
    	if(swatches != null) {
    		for(Color c : swatches)
    			addSwatch(c);
    	} 
    }

    /**
     * Adds a swatch using Hue, Saturation, and Brightness.
     * 
     * @param hue         0.0–1.0
     * @param saturation  0.0–1.0
     * @param brightness  0.0–1.0
     */
    public void addSwatch(float hue, float saturation, float brightness) {
        addSwatch(Color.getHSBColor(hue, saturation, brightness));
    }

    /**
     * Clears all swatches.
     */
    public void clearSwatches() {
        swatches.clear();
        swatchPanels.clear();
        removeAll();
        selectedPanel = null;
        revalidate();
        repaint();
    }

    /**
     * Highlights the selected swatch.
     */
    private void setSelected(JPanel panel) {
        if (selectedPanel != null)
            selectedPanel.setBorder(new LineBorder(Color.GRAY, 1));

        selectedPanel = panel;
        selectedPanel.setBorder(new LineBorder(Color.WHITE, 2));
    }

    /**
     * Sets a listener that runs when a swatch is clicked.
     */
    public void setOnColorSelected(Consumer<Color> listener) {
        this.onColorSelected = listener;
    }

    /**
     * Adds a few example colors.
     */
    public void addDefaultSwatches() {
        Color[] colors = {
                Color.WHITE, Color.BLACK, Color.GRAY, Color.RED, Color.ORANGE,
                Color.YELLOW, Color.GREEN, new Color(0, 128, 255),
                new Color(128, 0, 255), new Color(90, 45, 20) // dark brown
        };
        for (Color c : colors) addSwatch(c);
    }
    
    public List<Color> getSwatches() {
    	return Collections.unmodifiableList(swatches);
    }

    // --- Demo main ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Swatch Pane Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            SwatchPane swatchPane = new SwatchPane(30, 5);
            swatchPane.addDefaultSwatches();

            // Add HSB-based swatches (rainbow gradient)
            for (float h = 0f; h < 1f; h += 0.1f)
                swatchPane.addSwatch(h, 1f, 1f);

            JLabel label = new JLabel("Selected: none", SwingConstants.CENTER);
            label.setFont(label.getFont().deriveFont(Font.BOLD, 14f));
            label.setOpaque(true);
            label.setBackground(Color.LIGHT_GRAY);

            swatchPane.setOnColorSelected(color -> {
                label.setText("Selected: " + colorToHex(color));
                label.setBackground(color);
            });

            frame.setLayout(new BorderLayout(10, 10));
            frame.add(swatchPane, BorderLayout.CENTER);
            frame.add(label, BorderLayout.SOUTH);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static String colorToHex(Color c) {
        return String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
    }
}
