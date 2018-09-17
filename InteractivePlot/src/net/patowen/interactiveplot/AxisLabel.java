package net.patowen.interactiveplot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class AxisLabel extends JComponent {
	public enum Orientation {HORIZONTAL, VERTICAL};
	
	private BufferedImage textImage;
	
	public AxisLabel(String text, Orientation orientation, Font font) {
		if (font == null) {
			font = new JLabel().getFont();
		}
		setAlignmentX(0.5f);
		setAlignmentY(0.5f);
		
		FontMetrics fontMetrics = getFontMetrics(font);
		Rectangle2D bounds = fontMetrics.getStringBounds(text, getGraphics());
		int x = (int)bounds.getX();
		int y = (int)bounds.getY();
		int width = (int)bounds.getWidth();
		int height = (int)bounds.getHeight();

		BufferedImage tempImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = tempImage.getGraphics();
		g.setFont(font);
		g.setColor(Color.BLACK);
		g.drawString(text, -x, -y);
		
		switch (orientation) {
		case HORIZONTAL:
			textImage = tempImage;
			setPreferredSize(new Dimension(width, height));
			break;
		case VERTICAL:
			textImage = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);
			for (int i=0; i<width; i++) {
				for (int j=0; j<height; j++) {
					textImage.setRGB(j, width-i-1, tempImage.getRGB(i, j));
				}
			}
			setPreferredSize(new Dimension(height, width));
			break;
		default:
			throw new IllegalArgumentException("Orientation: " + orientation);
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int xPos = (getWidth() - textImage.getWidth()) / 2;
		int yPos = (getHeight() - textImage.getHeight()) / 2;
		g.drawImage(textImage, xPos, yPos, null);
		
		/*Font font = new Font("Sans", Font.PLAIN, 32);
		System.out.println(font.getName());
		
		g.setFont(font);
		g.drawString("hello", 50, 50);
		
		
		FontMetrics fontMetrics = g.getFontMetrics(font);
		System.out.println(fontMetrics.getHeight());*/
	}
}
