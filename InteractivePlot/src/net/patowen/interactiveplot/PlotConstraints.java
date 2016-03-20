package net.patowen.interactiveplot;

import java.util.Optional;

/**
 * This class provides restrictions into the allowed scales of the plot. For instance, a maximum
 * y-coordinate will prevent the plot from being zoomed to view anything above that coordinate.
 * @author Patrick Owen
 */
public class PlotConstraints
{
	private AxisScale xAxisScale;
	private AxisScale yAxisScale;
	
	// The chosen bounds may not go outside the rectangle defined by these values.
	private Optional<Double> xMinReal;
	private Optional<Double> xMaxReal;
	private Optional<Double> yMinReal;
	private Optional<Double> yMaxReal;
	
	private Optional<Double> xMin;
	private Optional<Double> xMax;
	private Optional<Double> yMin;
	private Optional<Double> yMax;
	
	/**
	 * Creates a {@code PlotConstraints} object with the default settings.
	 */
	public PlotConstraints() {
		xMinReal = Optional.empty();
		xMaxReal = Optional.empty();
		yMinReal = Optional.empty();
		yMaxReal = Optional.empty();
	}
	
	/**
	 * Sets the minimum x-coordinate that can be visible to the user.
	 * @param xMin the minimum x-coordinate in real coordinates
	 */
	public void setXMin(double xMin) {
		this.xMinReal = Optional.of(xMin);
	}
	
	/**
	 * Sets the maximum x-coordinate that can be visible to the user.
	 * @param xMax the maximum x-coordinate in real coordinates
	 */
	public void setXMax(double xMax) {
		this.xMaxReal = Optional.of(xMax);
	}
	
	/**
	 * Sets the minimum y-coordinate that can be visible to the user.
	 * @param yMin the minimum y-coordinate in real coordinates
	 */
	public void setYMin(double yMin) {
		this.yMinReal = Optional.of(yMin);
	}
	
	/**
	 * Sets the maximum x-coordinate that can be visible to the user.
	 * @param yMax the maximum y-coordinate in real coordinates
	 */
	public void setYMax(double yMax) {
		this.yMaxReal = Optional.of(yMax);
	}
	
	/**
	 * Sets the minimum and maximum x- and y-coordinates that can be visible to the user.
	 * @param xMin the minimum x-coordinate in real coordinates
	 * @param xMax the maximum x-coordinate in real coordinates
	 * @param yMin the minimum y-coordinate in real coordinates
	 * @param yMax the maximum y-coordinate in real coordinates
	 */
	public void setBounds(double xMin, double xMax, double yMin, double yMax) {
		this.xMinReal = Optional.of(xMin);
		this.xMaxReal = Optional.of(xMax);
		this.yMinReal = Optional.of(yMin);
		this.yMaxReal = Optional.of(yMax);
	}
	
	void setAxes(AxisScale xAxisScale, AxisScale yAxisScale) {
		this.xAxisScale = xAxisScale;
		this.yAxisScale = yAxisScale;
	}
	
	void applyConstraints(PlotBounds plotBounds) {
		applyAxes();
		applyXBounds(plotBounds);
		applyYBounds(plotBounds);
	}
	
	// Converts the given real coordinates to linear coordinates.
	private void applyAxes() {
		if (xMinReal.isPresent()) {
			xMin = Optional.of(xAxisScale.getLinear(xMinReal.get()));
		} else {
			xMin = Optional.empty();
		}
		
		if (xMaxReal.isPresent()) {
			xMax = Optional.of(xAxisScale.getLinear(xMaxReal.get()));
		} else {
			xMax = Optional.empty();
		}
		
		if (yMinReal.isPresent()) {
			yMin = Optional.of(yAxisScale.getLinear(yMinReal.get()));
		} else {
			yMin = Optional.empty();
		}
		
		if (yMaxReal.isPresent()) {
			yMax = Optional.of(yAxisScale.getLinear(yMaxReal.get()));
		} else {
			yMax = Optional.empty();
		}
	}
	
	private void applyXBounds(PlotBounds plotBounds) {
		// Convert left and right to min and max.
		boolean leftIsMin = plotBounds.getXLeft() < plotBounds.getXRight();
		double plotXMin = leftIsMin ? plotBounds.getXLeft() : plotBounds.getXRight();
		double plotXMax = leftIsMin ? plotBounds.getXRight() : plotBounds.getXLeft();
		
		// If the graph is zoomed out too far, set the plot to its maximum bounds.
		if (xMin.isPresent() && xMax.isPresent() && plotXMax - plotXMin >= xMax.get() - xMin.get()) {
			plotXMin = xMin.get();
			plotXMax = xMax.get();
		// Otherwise, translate the plot to the right location.
		} else if (xMin.isPresent() && plotXMin < xMin.get()) {
			plotXMax += xMin.get() - plotXMin;
			plotXMin = xMin.get();
		} else if (xMax.isPresent() && plotXMax > xMax.get()) {
			plotXMin -= plotXMax - xMax.get();
			plotXMax = xMax.get();
		}
		
		// Convert min and max to left and right.
		plotBounds.setXLeft(leftIsMin ? plotXMin : plotXMax);
		plotBounds.setXRight(leftIsMin ? plotXMax : plotXMin);
	}
	
	private void applyYBounds(PlotBounds plotBounds) {
		// Convert left and right to min and max.
		boolean topIsMin = plotBounds.getYTop() < plotBounds.getYBottom();
		double plotYMin = topIsMin ? plotBounds.getYTop() : plotBounds.getYBottom();
		double plotYMax = topIsMin ? plotBounds.getYBottom() : plotBounds.getYTop();
		
		// If the graph is zoomed out too far, set the plot to its maximum bounds.
		if (yMin.isPresent() && yMax.isPresent() && plotYMax - plotYMin >= yMax.get() - yMin.get()) {
			plotYMin = yMin.get();
			plotYMax = yMax.get();
		// Otherwise, translate the plot to the right location.
		} else if (yMin.isPresent() && plotYMin < yMin.get()) {
			plotYMax += yMin.get() - plotYMin;
			plotYMin = yMin.get();
		} else if (yMax.isPresent() && plotYMax > yMax.get()) {
			plotYMin -= plotYMax - yMax.get();
			plotYMax = yMax.get();
		}
		
		// Convert min and max to left and right.
		plotBounds.setYTop(topIsMin ? plotYMin : plotYMax);
		plotBounds.setYBottom(topIsMin ? plotYMax : plotYMin);
	}
}
