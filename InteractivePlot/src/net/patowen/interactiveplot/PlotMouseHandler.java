package net.patowen.interactiveplot;

import java.awt.Component;

/**
 * This provides a convenient way to define how the mouse can interact with the plot.
 * @author Patrick Owen
 */
public class PlotMouseHandler {
	private Component parent;
	
	void setParent(Component parent) {
		this.parent = parent;
	}
	
	/**
	 * Refreshes the graph to handle the view changing.
	 */
	protected void repaint() {
		parent.repaint();
	}
	
	/**
	 * This method is called when the user presses a mouse button.
	 * @param plotScale the scale that defines the current plot. This can be modified as needed.
	 * @param location the location of the mouse
	 * @param button which button is pressed
	 */
	public void mousePressed(PlotScale plotScale, PlotMouseLocation location, int button) {}
	
	/**
	 * This method is called when the user presses a mouse button.
	 * @param plotScale the scale that defines the current plot. This can be modified as needed.
	 * @param location the location of the mouse
	 * @param button which button is released
	 */
	public void mouseReleased(PlotScale plotScale, PlotMouseLocation location, int button) {}
	
	/**
	 * This method is called when the user moves the mouse with a button held down.
	 * @param plotScale the scale that defines the current plot. This can be modified as needed.
	 * @param location the location of the mouse
	 */
	public void mouseDragged(PlotScale plotScale, PlotMouseLocation location) {}
	
	/**
	 * This method is called when the user scrolls the mouse wheel.
	 * @param plotScale the scale that defines the current plot. This can be modified as needed.
	 * @param location the location of the mouse
	 * @param scrollAmount how much the user scrolled and by which direction
	 */
	public void mouseWheelMoved(PlotScale plotScale, PlotMouseLocation location, double scrollAmount) {}
}
