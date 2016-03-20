package net.patowen.interactiveplot;

/**
 * This is the default {@link PlotMouseHandler}. It handles scrolling to zoom by choosing the axis
 * that the mouse is closer to.
 * @author Patrick Owen
 */
public class BasicPlotMouseHandler extends PlotMouseHandler {
	private double wheelScale;
	
	/**
	 * Creates a {@code BasicPlotMouseHandler} with the default settings.
	 */
	public BasicPlotMouseHandler() {
		wheelScale = 1.2;
	}
	
	/**
	 * Sets the scale factor of a zoom corresponding to a single click on the mouse wheel.
	 */
	public void setWheelScale(double wheelScale) {
		this.wheelScale = wheelScale;
	}
	
	public void mouseWheelMoved(PlotScale plotScale, PlotMouseLocation location, double scrollAmount) {
		int xBias = Math.max(-location.getRelativeX(false), location.getRelativeX(true));
		int yBias = Math.max(-location.getRelativeY(false), location.getRelativeY(true));
		if (yBias > xBias) {
			plotScale.zoomX(location.getLinearX(), Math.pow(wheelScale, scrollAmount));
		} else if (xBias > yBias) {
			plotScale.zoomY(location.getLinearY(), Math.pow(wheelScale, scrollAmount));
		}
		repaint();
	}
}
