package net.patowen.interactiveplot;

/**
 * This provides a convenient way to define how the mouse can interact with the plot.
 * @author Patrick Owen
 */
public class PlotMouseHandler {
	/**
	 * This method is called when the user scrolls the mouse wheel.
	 * @param plotScale the scale that defines the current plot. This can be modified as needed.
	 * @param location the location of the mouse
	 * @param scrollAmount how much the user scrolled and by which direction
	 */
	public void mouseWheelMoved(PlotScale plotScale, PlotMouseLocation location, double scrollAmount) {}
}
