package net.patowen.interactiveplot;

/**
 * This class defines a solid rectangle in real coordinates or linear coordinates that can be used
 * as a set of boundaries for a plot. A graph my have several sets of boundaries. Directions such
 * as left, right, top, and bottom represent what the user sees on the screen. For instance,
 * depending on the orientation of the plot, yTop may be greater than or less than yBottom.
 * @author Patrick Owen
 */
public class PlotBounds {
	// The x and y coordinates of the center of the boundary pixels in each direction.
	private double xLeft;
	private double xRight;
	private double yTop;
	private double yBottom;
	
	/**
	 * Creates a {@code PlotBounds} object with the given boundary coordinates.
	 * @param xLeft the x-coordinate of the left edge of the boundary
	 * @param xRight the x-coordinate of the right edge of the boundary
	 * @param yTop the y-coordinate of the top edge of the boundary
	 * @param yBottom the y-coordinate of the bottom edge of the boundary
	 */
	public PlotBounds(double xLeft, double xRight, double yTop, double yBottom) {
		setBounds(xLeft, xRight, yTop, yBottom);
	}
	
	/**
	 * Creates a {@code PlotBounds} object with the same bounds as the argument.
	 */
	public PlotBounds(PlotBounds data) {
		this(data.xLeft, data.xRight, data.yTop, data.yBottom);
	}
	
	/**
	 * Creates and returns a {@code PlotBounds} object with the boundary given in linear coordinates
	 * assuming the current boundary is given in real coordinates.
	 * @param xAxisScale the x-axis used in the conversion to linear coordinates
	 * @param yAxisScale the y-axis used in the conversion to linear coordinates
	 */
	public PlotBounds getLinearBounds(AxisScale xAxisScale, AxisScale yAxisScale) {
		return new PlotBounds(
				xAxisScale.getLinear(xLeft),
				xAxisScale.getLinear(xRight),
				yAxisScale.getLinear(yTop),
				yAxisScale.getLinear(yBottom));
	}
	
	/**
	 * Modifies the boundary coordinates to match the arguments.
	 * @param xLeft the x-coordinate of the left edge of the boundary
	 * @param xRight the x-coordinate of the right edge of the boundary
	 * @param yTop the y-coordinate of the top edge of the boundary
	 * @param yBottom the y-coordinate of the bottom edge of the boundary
	 */
	public void setBounds(double xLeft, double xRight, double yTop, double yBottom) {
		this.xLeft = xLeft;
		this.xRight = xRight;
		this.yTop = yTop;
		this.yBottom = yBottom;
	}
	
	/**
	 * Modifies the boundary coordinates to be the same as those of the argument.
	 */
	public void setBounds(PlotBounds data) {
		setBounds(data.xLeft, data.xRight, data.yTop, data.yBottom);
	}
	
	/**
	 * Modifies the x-coordinate of the left edge of the boundary.
	 */
	public void setXLeft(double xLeft) {
		this.xLeft = xLeft;
	}
	
	/**
	 * Modifies the x-coordinate of the right edge of the boundary.
	 */
	public void setXRight(double xRight) {
		this.xRight = xRight;
	}
	
	/**
	 * Modifies the y-coordinate of the top edge of the boundary.
	 */
	public void setYTop(double yTop) {
		this.yTop = yTop;
	}
	
	/**
	 * Modifies the y-coordinate of the bottom edge of the boundary.
	 */
	public void setYBottom(double yBottom) {
		this.yBottom = yBottom;
	}
	
	/**
	 * Returns the x-coordinate of the left edge of the boundary.
	 */
	public double getXLeft() {
		return xLeft;
	}
	
	/**
	 * Returns the x-coordinate of the right edge of the boundary.
	 */
	public double getXRight() {
		return xRight;
	}
	
	/**
	 * Returns the y-coordinate of the top edge of the boundary.
	 */
	public double getYTop() {
		return yTop;
	}
	
	/**
	 * Returns the y-coordinate of the bottom edge of the boundary.
	 */
	public double getYBottom() {
		return yBottom;
	}
}
