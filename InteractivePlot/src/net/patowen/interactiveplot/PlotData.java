package net.patowen.interactiveplot;

import java.awt.Component;
import java.awt.Graphics2D;

/**
 * Subclasses of {@code PlotData} define the data that is drawn in a plot. It has access to the
 * relevant {@link PlotScale} object to convert pixel and screen coordinates, used for drawing, to
 * real coordinates and back.
 * @author Patrick Owen
 */
public abstract class PlotData {
	private Component parent;
	
	void setParent(Component parent) {
		this.parent = parent;
	}
	
	/**
	 * Triggers the parent {@link InteractivePlot} to refresh. This should be called whenever the
	 * data changes.
	 */
	public void repaint() {
		if (parent != null) {
			parent.repaint();
		}
	}
	
	/**
	 * Draws the contents of the plot. The origin is set to the top-left coordinate of the plot,
	 * so coordinates in drawing functions must correspond to pixel coordinates.
	 * @param g the graphics context
	 * @param plotScale the current zoom state of the plot, used to convert between real coordinates
	 * and pixel coordinates
	 */
	public abstract void drawData(Graphics2D g, PlotScale plotScale);
}
