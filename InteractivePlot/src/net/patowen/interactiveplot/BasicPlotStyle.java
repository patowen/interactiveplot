package net.patowen.interactiveplot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * {@code BasicPlotStyle} is the default style used for plots. The plot appears in a rectangular
 * window with a border and grid, and axes appear on the sides of this window.
 * @author Patrick Owen
 */
public class BasicPlotStyle extends PlotStyle {
	private Color mainColor;
	private Color gridColor;
	
	private int majorAxisLength;
	
	/**
	 * Creates a {@code BasicPlotStyle} with the default settings.
	 */
	public BasicPlotStyle() {
		majorAxisLength = 8;
		
		mainColor = Color.BLACK;
		gridColor = Color.LIGHT_GRAY;
	}
	
	protected void paintBackground(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillRect(plotScale.getPixelXLeft(), plotScale.getPixelYTop(), plotScale.getWidth(), plotScale.getHeight());
		g.setColor(gridColor);
		drawGrid(g);
	}
	
	protected void paintForeground(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.drawRect(plotScale.getPixelXLeft(), plotScale.getPixelYTop(), plotScale.getWidth()-1, plotScale.getHeight()-1);
		drawAxes(g);
	}
	
	protected int getLeftMargin() {
		return 64; // TODO Stop hardcoding.
	}
	
	protected int getTopMargin() {
		return 16;
	}
	
	protected int getRightMargin() {
		return 24;
	}
	
	protected int getBottomMargin() {
		return 24;
	}
	
	protected int getMajorAxisSpacingX() {
		return 48;
	}
	
	protected int getMajorAxisSpacingY() {
		return 16;
	}
	
	private void drawGrid(Graphics2D g) {
		for (AxisLabel label : xLabels.getLabels()) {
			int pixelX = plotScale.getPixelX(label.getLocation());
			g.drawLine(pixelX, plotScale.getPixelYTop(), pixelX, plotScale.getPixelYBottom());
		}
		
		for (AxisLabel label : yLabels.getLabels()) {
			int pixelY = plotScale.getPixelY(label.getLocation());
			g.drawLine(plotScale.getPixelXLeft(), pixelY, plotScale.getPixelXRight(), pixelY);
		}
	}
	
	private void drawAxes(Graphics2D g) {
		for (AxisLabel label : xLabels.getLabels()) {
			drawXTick(g, label);
		}
		
		for (AxisLabel label : yLabels.getLabels()) {
			drawYTick(g, label);
		}
	}
	
	private void drawXTick(Graphics2D g, AxisLabel label) {
		int i = plotScale.getPixelX(label.getLocation());
		
		g.setColor(mainColor);
		g.drawLine(i, plotScale.getPixelYBottom(), i, plotScale.getPixelYBottom()+majorAxisLength);
		
		if (label != null) {
			Rectangle2D lb = g.getFontMetrics().getStringBounds(label.getText(), g);
			g.drawString(label.getText(), (float)(i-lb.getCenterX()), (float)(plotScale.getPixelYBottom()+majorAxisLength-lb.getMinY()+1));
		}
	}
	
	private void drawYTick(Graphics2D g, AxisLabel label) {
		int j = plotScale.getPixelY(label.getLocation());
		
		g.setColor(mainColor);
		g.drawLine(plotScale.getPixelXLeft(), j, plotScale.getPixelXLeft()-majorAxisLength, j);
		
		if (label != null) {
			Rectangle2D lb = g.getFontMetrics().getStringBounds(label.getText(), g);
			g.drawString(label.getText(), (float)(plotScale.getPixelXLeft()-majorAxisLength-lb.getMaxX()-1), (float)(j-lb.getCenterY()));
		}
	}
}
