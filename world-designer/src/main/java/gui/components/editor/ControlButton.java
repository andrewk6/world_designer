package gui.components.editor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ControlButton extends JButton
{
	private boolean active = false;
	
	public ControlButton(String txt, ActionListener act) {
		super(txt);
		this.addActionListener(act);
		
		this.setFocusable(false);
	}
	
	public ControlButton(String txt, Runnable act) {
		this(txt, e_-> act.run());
	}
	
	protected void paintComponent(Graphics g) {
        // Paint normal background
        super.paintComponent(g);

        // Paint inner highlight box if selected
        if (active) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.BLACK); // solid blue box
            g2.setStroke(new BasicStroke(2)); // thickness of box
            int inset = 5; // pixels from edge
            int x = inset;
            int y = inset;
            int w = getWidth() - 2 * inset - 1; // -1 so stroke fits
            int h = getHeight() - 2 * inset - 1;

            g2.drawRect(x, y, w, h);
            g2.dispose();
        }
    }
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
		this.repaint();
	}
	
	public void changeState() {
		this.active = !this.active;
		this.repaint();
	}
}