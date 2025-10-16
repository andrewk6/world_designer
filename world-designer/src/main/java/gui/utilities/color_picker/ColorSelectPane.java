package gui.utilities.color_picker;
////
////import java.awt.BorderLayout;
////import java.awt.Color;
////import java.awt.Dimension;
////
////import javax.swing.JPanel;
////
////import gui.utilities.CompFactory;
////
////public class MiniColorPicker extends JPanel {
////	public static void main(String[] args) {
////		CompFactory.buildTestWindow(new MiniColorPicker());
////	}
////    private final SVBox svBox = new SVBox();
////    private final HueBar hueBar = new HueBar();
////    private final JPanel preview = new JPanel();
////
////    MiniColorPicker() {
////        setLayout(new BorderLayout(5, 5));
////        add(svBox, BorderLayout.CENTER);
////        add(hueBar, BorderLayout.EAST);
////        add(preview, BorderLayout.SOUTH);
////        preview.setPreferredSize(new Dimension(100, 30));
////
////        hueBar.setOnHueChange(h -> {
////            svBox.setHue(h);
////            preview.setBackground(svBox.getColor());
////        });
////
////        svBox.setOnChange(c -> preview.setBackground(c));
////    }
////
////    public Color getColor() { return svBox.getColor(); }
////}
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.awt.image.BufferedImage;
//
//public class MiniColorPicker extends JPanel {
//
//    private float hue = 0f;
//    private float saturation = 1f;
//    private float brightness = 1f;
//    private final JPanel previewPanel;
//
//    public MiniColorPicker() {
//        setLayout(new BorderLayout(10, 0));
//
//        // --- Main SV (saturation/value) square ---
//        SVBox svBox = new SVBox();
//        add(svBox, BorderLayout.CENTER);
//
//        // --- Vertical hue slider ---
//        HueSlider hueSlider = new HueSlider();
//        add(hueSlider, BorderLayout.EAST);
//
//        // --- Preview box at bottom (optional) ---
//        previewPanel = new JPanel();
//        previewPanel.setPreferredSize(new Dimension(50, 50));
//        add(previewPanel, BorderLayout.SOUTH);
//
//        updatePreview();
//    }
//
//    private void updatePreview() {
//        Color c = Color.getHSBColor(hue, saturation, brightness);
//        previewPanel.setBackground(c);
//    }
//
//    // --- Saturation/Value selection area ---
//    private class SVBox extends JPanel {
//        private BufferedImage image;
//
//        SVBox() {
//            setPreferredSize(new Dimension(200, 200));
//
//            addMouseMotionListener(new MouseMotionAdapter() {
//                @Override
//                public void mouseDragged(MouseEvent e) {
//                    handle(e);
//                }
//            });
//            addMouseListener(new MouseAdapter() {
//                @Override
//                public void mousePressed(MouseEvent e) {
//                    handle(e);
//                }
//            });
//        }
//
//        private void handle(MouseEvent e) {
//            int x = Math.max(0, Math.min(getWidth(), e.getX()));
//            int y = Math.max(0, Math.min(getHeight(), e.getY()));
//            saturation = x / (float) getWidth();
//            brightness = 1f - (y / (float) getHeight());
//            repaint();
//            updatePreview();
//        }
//
//        @Override
//        protected void paintComponent(Graphics g) {
//            super.paintComponent(g);
//            if (image == null || image.getWidth() != getWidth() || image.getHeight() != getHeight()) {
//                image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
//            }
//
//            for (int x = 0; x < getWidth(); x++) {
//                for (int y = 0; y < getHeight(); y++) {
//                    float s = x / (float) getWidth();
//                    float b = 1f - (y / (float) getHeight());
//                    int rgb = Color.HSBtoRGB(hue, s, b);
//                    image.setRGB(x, y, rgb);
//                }
//            }
//            g.drawImage(image, 0, 0, null);
//
//            // Draw marker
//            int markerX = (int) (saturation * getWidth());
//            int markerY = (int) ((1 - brightness) * getHeight());
//            g.setColor(Color.WHITE);
//            g.drawOval(markerX - 4, markerY - 4, 8, 8);
//            g.setColor(Color.BLACK);
//            g.drawOval(markerX - 3, markerY - 3, 6, 6);
//        }
//    }
//
//    // --- Vertical hue slider ---
//    private class HueSlider extends JPanel {
//        private BufferedImage hueImage;
//
//        HueSlider() {
//            setPreferredSize(new Dimension(20, 200));
//
//            addMouseMotionListener(new MouseMotionAdapter() {
//                @Override
//                public void mouseDragged(MouseEvent e) {
//                    handle(e);
//                }
//            });
//            addMouseListener(new MouseAdapter() {
//                @Override
//                public void mousePressed(MouseEvent e) {
//                    handle(e);
//                }
//            });
//        }
//
//        private void handle(MouseEvent e) {
//            int y = Math.max(0, Math.min(getHeight(), e.getY()));
//            hue = 1f - (y / (float) getHeight());
//            repaint();
//            updatePreview();
//            MiniColorPicker.this.repaint(); // refresh SV box
//        }
//
//        @Override
//        protected void paintComponent(Graphics g) {
//            super.paintComponent(g);
//
//            if (hueImage == null || hueImage.getHeight() != getHeight()) {
//                hueImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
//                for (int y = 0; y < getHeight(); y++) {
//                    float h = 1f - (y / (float) getHeight());
//                    int rgb = Color.HSBtoRGB(h, 1f, 1f);
//                    for (int x = 0; x < getWidth(); x++) {
//                        hueImage.setRGB(x, y, rgb);
//                    }
//                }
//            }
//
//            g.drawImage(hueImage, 0, 0, null);
//
//            // Draw slider indicator
//            int sliderY = (int) ((1 - hue) * getHeight());
//            g.setColor(Color.BLACK);
//            g.drawLine(0, sliderY, getWidth(), sliderY);
//        }
//    }
//
//    // --- Demo main method ---
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("Mini Color Picker");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.add(new MiniColorPicker());
//            frame.pack();
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });
//    }
//}

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class ColorSelectPane extends JPanel {

    private float hue = 0f;
    private float saturation = 1f;
    private float brightness = 1f;
    private final JPanel previewPanel;
    private final SVBox svBox;
    private final HueSlider hueSlider;

    public ColorSelectPane() {
        setLayout(new BorderLayout(10, 0)); // spacing between SV and hue slider

        // --- Main SV (saturation/value) square ---
        svBox = new SVBox();
        svBox.setBorder(new LineBorder(Color.GRAY, 1, true));
        add(svBox, BorderLayout.CENTER);

        // --- Vertical hue slider ---
        hueSlider = new HueSlider();
        hueSlider.setBorder(new LineBorder(Color.GRAY, 1, true));
        add(hueSlider, BorderLayout.EAST);

        // --- Preview box at bottom ---
        previewPanel = new JPanel();
        previewPanel.setPreferredSize(new Dimension(50, 50));
        previewPanel.setBorder(new MatteBorder(2, 0, 0, 0, Color.DARK_GRAY)); // subtle top line
        add(previewPanel, BorderLayout.SOUTH);

        updatePreview();
    }

    private void updatePreview() {
        Color c = Color.getHSBColor(hue, saturation, brightness);
        previewPanel.setBackground(c);
    }
    
    public Color getSelectedColor() {
    	return Color.getHSBColor(hue, saturation, brightness);
    }

    // --- Saturation/Value selection area ---
    private class SVBox extends JPanel {
        private BufferedImage image;

        SVBox() {
            setPreferredSize(new Dimension(200, 200));
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    handle(e);
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    handle(e);
                }
            });
        }

        private void handle(MouseEvent e) {
            int x = Math.max(0, Math.min(getWidth(), e.getX()));
            int y = Math.max(0, Math.min(getHeight(), e.getY()));
            saturation = x / (float) getWidth();
            brightness = 1f - (y / (float) getHeight());
            repaint();
            updatePreview();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (image == null || image.getWidth() != getWidth() || image.getHeight() != getHeight()) {
                image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            }

            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    float s = x / (float) getWidth();
                    float b = 1f - (y / (float) getHeight());
                    int rgb = Color.HSBtoRGB(hue, s, b);
                    image.setRGB(x, y, rgb);
                }
            }
            g.drawImage(image, 0, 0, null);

            // Draw marker
            int markerX = (int) (saturation * getWidth());
            int markerY = (int) ((1 - brightness) * getHeight());
            g.setColor(Color.WHITE);
            g.drawOval(markerX - 4, markerY - 4, 8, 8);
            g.setColor(Color.BLACK);
            g.drawOval(markerX - 3, markerY - 3, 6, 6);
        }
    }

    // --- Vertical hue slider ---
    private class HueSlider extends JPanel {
        private BufferedImage hueImage;

        HueSlider() {
            setPreferredSize(new Dimension(30, 200)); // thicker slider
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    handle(e);
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    handle(e);
                }
            });
        }

        private void handle(MouseEvent e) {
            int y = Math.max(0, Math.min(getHeight(), e.getY()));
            hue = 1f - (y / (float) getHeight());
            repaint();
            updatePreview();
            svBox.repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (hueImage == null || hueImage.getHeight() != getHeight()) {
                hueImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
                for (int y = 0; y < getHeight(); y++) {
                    float h = 1f - (y / (float) getHeight());
                    int rgb = Color.HSBtoRGB(h, 1f, 1f);
                    for (int x = 0; x < getWidth(); x++) {
                        hueImage.setRGB(x, y, rgb);
                    }
                }
            }

            g.drawImage(hueImage, 0, 0, null);

            // Draw slider indicator line
            int sliderY = (int) ((1 - hue) * getHeight());
            g.setColor(Color.BLACK);
            g.drawLine(0, sliderY, getWidth(), sliderY);
            g.setColor(Color.WHITE);
            g.drawLine(0, sliderY + 1, getWidth(), sliderY + 1);
        }
    }

    // --- Demo main method ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Mini Color Picker");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new ColorSelectPane());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

