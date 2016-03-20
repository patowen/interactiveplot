package net.patowen.interactiveplot;

/**
 * Contains all information regarding the size of the plot window and how pixels correspond to
 * coordinates in the graph.
 * @author Patrick Owen
 */
public final class PlotScale {
	// Width and height of the inside portion of the graph, including the boundary, in number
	// of pixels.
	private int gWidth;
	private int gHeight;
	
	// The x and y coordinates of the center of the boundary pixels in each direction in
	// linear coordinates.
	private PlotBounds chosenBounds;
	
	private PlotConstraints constraints;
	
	private AxisScale xAxisScale;
	private AxisScale yAxisScale;
	
	// Anything beyond here should be cut off. These are the x and y coordinates of the
	// very edge of the graph.
	private PlotBounds edgeBounds;
	
	static Builder getBuilder() {
		return new Builder();
	}
	
	private PlotScale(Builder builder) {
		gWidth = builder.gWidth;
		gHeight = builder.gHeight;
		
		xAxisScale = builder.xAxisScale;
		yAxisScale = builder.yAxisScale;
		
		chosenBounds = builder.realBounds.getLinearBounds(xAxisScale, yAxisScale);
		constraints = builder.constraints;
		constraints.setAxes(xAxisScale, yAxisScale);
		
		edgeBounds = new PlotBounds(chosenBounds);
		applyConstraints();
	}
	
	/**
	 * Sets the size of the inside portion of the graph, including the boundary, in number
	 * of pixels. The graph contents are resized so that the center of the boundary pixels
	 * are anchored in every direction.
	 * @param width the size in the horizontal direction
	 * @param height the size in the vertical direction
	 */
	public void setSize(int width, int height) {
		gWidth = width;
		gHeight = height;
		
		setEdgeBounds();
	}
	
	/**
	 * Sets the x and y coordinates of the center of the boundary pixels in each direction.
	 * @param xLeft the x-coordinate of the center of the left boundary pixels
	 * @param xRight the x-coordinate of the center of the right boundary pixels
	 * @param yTop the y-coordinate of the center of the top boundary pixels
	 * @param yBottom the y-coordinate of the center of the bottom boundary pixels
	 */
	public void setBounds(double xLeft, double xRight, double yTop, double yBottom) {
		chosenBounds.setBounds(xLeft, xRight, yTop, yBottom);
		
		applyConstraints();
	}
	
	/**
	 * Adjusts the graph bounds so that the given real x-coordinate is fixed and view range
	 * of the graph is increased by the scale.
	 * @param x the fixed x-coordinate
	 * @param scale the factor by which to increase the horizontal view range
	 */
	public void zoomX(double x, double scale) {
		chosenBounds.setXLeft((chosenBounds.getXLeft()-x)*scale+x);
		chosenBounds.setXRight((chosenBounds.getXRight()-x)*scale+x);
		
		applyConstraints();
	}
	
	/**
	 * Adjusts the graph bounds so that the given real y-coordinate is fixed and view range
	 * of the graph is increased by the scale.
	 * @param y the fixed y-coordinate
	 * @param scale the factor by which to increase the vertical view range
	 */
	public void zoomY(double y, double scale) {
		chosenBounds.setYTop((chosenBounds.getYTop()-y)*scale+y);
		chosenBounds.setYBottom((chosenBounds.getYBottom()-y)*scale+y);
		
		applyConstraints();
	}
	
	/**
	 * Sets the extreme horizontal bounds of the graph based on the given bounds and the size of the graph.
	 */
	private void setXEdgeBounds() {
		double xRange = chosenBounds.getXRight() - chosenBounds.getXLeft();
		double xEdgeOffset = xRange / (gWidth-1) / 2;
		edgeBounds.setXLeft(chosenBounds.getXLeft() - xEdgeOffset);
		edgeBounds.setXRight(chosenBounds.getXRight() + xEdgeOffset);
	}
	
	/**
	 * Sets the extreme vertical bounds of the graph based on the given bounds and the size of the graph.
	 */
	private void setYEdgeBounds() {
		double yRange = chosenBounds.getYBottom() - chosenBounds.getYTop();
		double yEdgeOffset = yRange / (gHeight-1) / 2;
		edgeBounds.setYTop(chosenBounds.getYTop() - yEdgeOffset);
		edgeBounds.setYBottom(chosenBounds.getYBottom() + yEdgeOffset);
	}
	
	// Sets the extreme bounds of the plot based on the given bounds and the size of the graph.
	private void setEdgeBounds() {
		setXEdgeBounds();
		setYEdgeBounds();
	}
	
	private void applyConstraints() {
		constraints.applyConstraints(chosenBounds);
		setEdgeBounds();
	}
	
	/**
	 * Returns the width of the plot window in pixels.
	 */
	public int getWidth() {
		return gWidth;
	}
	
	/**
	 * Returns the height of the plot window in pixels.
	 */
	public int getHeight() {
		return gHeight;
	}
	
	/**
	 * Returns the x-coordinate of the left edge of the plot window in screen coordinates.
	 */
	public double getScreenXLeft() {
		return 0;
	}
	
	/**
	 * Returns the x-coordinate of the right edge of the plot window in screen coordinates.
	 */
	public double getScreenXRight() {
		return gWidth;
	}
	
	/**
	 * Returns the y-coordinate of the top edge of the plot window in screen coordinates.
	 */
	public double getScreenYTop() {
		return 0;
	}
	
	/**
	 * Returns the y-coordinate of the bottom edge of the plot window in screen coordinates.
	 */
	public double getScreenYBottom() {
		return gHeight;
	}
	
	/**
	 * Returns the x-coordinate of the leftmost pixels of the plot window in pixel coordinates.
	 */
	public int getPixelXLeft() {
		return 0;
	}
	
	/**
	 * Returns the x-coordinate of the rightmost pixels of the plot window in pixel coordinates.
	 */
	public int getPixelXRight() {
		return gWidth-1;
	}
	
	/**
	 * Returns the y-coordinate of the topmost pixels of the plot window in pixel coordinates.
	 */
	public int getPixelYTop() {
		return 0;
	}
	
	/**
	 * Returns the y-coordinate of the bottommost pixels of the plot window in pixel coordinates.
	 */
	public int getPixelYBottom() {
		return gHeight-1;
	}
	
	/**
	 * Returns the x-coordinate of the left edge of the plot window in real coordinates.
	 */
	public double getRealXLeft() {
		return edgeBounds.getXLeft();
	}
	
	/**
	 * Returns the x-coordinate of the right edge of the plot window in real coordinates.
	 */
	public double getRealXRight() {
		return edgeBounds.getXRight();
	}
	
	/**
	 * Returns the y-coordinate of the top edge of the plot window in real coordinates.
	 */
	public double getRealYTop() {
		return edgeBounds.getYTop();
	}
	
	/**
	 * Returns the y-coordinate of the bottom edge of the plot window in real coordinates.
	 */
	public double getRealYBottom() {
		return edgeBounds.getYBottom();
	}
	
	/**
	 * Converts the given x-coordinate from real coordinates to screen coordinates.
	 * @param realX the x-coordinate in real coordinates
	 * @return the x-coordinate in screen coordinates
	 */
	public double getScreenX(double realX)
	{
		return (xAxisScale.getLinear(realX)-edgeBounds.getXLeft())/(edgeBounds.getXRight()-edgeBounds.getXLeft())*gWidth;
	}
	
	/**
	 * Converts the given y-coordinate from real coordinates to screen coordinates.
	 * @param realY the y-coordinate in real coordinates
	 * @return the y-coordinate in screen coordinates
	 */
	public double getScreenY(double realY)
	{
		return (yAxisScale.getLinear(realY)-edgeBounds.getYTop())/(edgeBounds.getYBottom()-edgeBounds.getYTop())*gHeight;
	}
	
	/**
	 * Converts the given x-coordinate from real coordinates to pixel coordinates.
	 * @param realX the x-coordinate in real coordinates
	 * @return the x-coordinate in pixel coordinates
	 */
	public int getPixelX(double realX)
	{
		return (int)Math.floor(getScreenX(realX));
	}
	
	/**
	 * Converts the given y-coordinate from real coordinates to pixel coordinates.
	 * @param realY the y-coordinate in real coordinates
	 * @return the y-coordinate in pixel coordinates
	 */
	public int getPixelY(double realY)
	{
		return (int)Math.floor(getScreenY(realY));
	}
	
	/**
	 * Converts the given x-coordinate from screen coordinates to linear coordinates.
	 * @param screenX the x-coordinate in screen coordinates
	 * @return the x-coordinate in linear coordinates
	 */
	public double getLinearX(double screenX)
	{
		return screenX*(edgeBounds.getXRight()-edgeBounds.getXLeft())/gWidth + edgeBounds.getXLeft();
	}
	
	/**
	 * Converts the given y-coordinate from screen coordinates to linear coordinates.
	 * @param screenY the y-coordinate in screen coordinates
	 * @return the y-coordinate in linear coordinates
	 */
	public double getLinearY(double screenY)
	{
		return screenY*(edgeBounds.getYBottom()-edgeBounds.getYTop())/gHeight + edgeBounds.getYTop();
	}
	
	/**
	 * Converts the given x-coordinate from screen coordinates to real coordinates.
	 * @param screenX the x-coordinate in screen coordinates
	 * @return the x-coordinate in real coordinates
	 */
	public double getRealX(double screenX)
	{
		return xAxisScale.getReal(getLinearX(screenX));
	}
	
	/**
	 * Converts the given y-coordinate from screen coordinates to real coordinates.
	 * @param screenY the y-coordinate in screen coordinates
	 * @return the y-coordinate in real coordinates
	 */
	public double getRealY(double screenY)
	{
		return yAxisScale.getReal(getLinearY(screenY));
	}
	
	/**
	 * Converts the given distance in the horizontal direction from screen coordinates to linear
	 * coordinates. Any plot translation does not affect the output of this method.
	 * @param screenWidth the distance in screen coordinates
	 * @return the distance in real coordinates
	 */
	public double getLinearWidth(double screenWidth) {
		return Math.abs(screenWidth*(edgeBounds.getXRight()-edgeBounds.getXLeft())/gWidth);
	}
	
	/**
	 * Converts the given distance in the vertical direction from screen coordinates to linear
	 * coordinates. Any plot translation does not affect the output of this method.
	 * @param screenHeight the distance in screen coordinates
	 * @return the distance in real coordinates
	 */
	public double getLinearHeight(double screenHeight) {
		return Math.abs(screenHeight*(edgeBounds.getYBottom()-edgeBounds.getYTop())/gHeight);
	}
	
	/**
	 * Returns a suitable {@link AxisLabelList} object for the x-axis.
	 * @param spacing the minimum spacing of the labels in pixels
	 */
	public AxisLabelList getXLabels(int spacing) {
		boolean leftIsMin = getRealXLeft() < getRealXRight();
		return xAxisScale.getLabels(
				leftIsMin ? getRealXLeft() : getRealXRight(),
				leftIsMin ? getRealXRight() : getRealXLeft(),
				getLinearWidth(spacing));
	}
	
	/**
	 * Returns a suitable {@link AxisLabelList} object for the y-axis.
	 * @param spacing the minimum spacing of the labels in pixels
	 */
	public AxisLabelList getYLabels(int spacing) {
		boolean topIsMin = getRealYTop() < getRealYBottom();
		return yAxisScale.getLabels(
				topIsMin ? getRealYTop() : getRealYBottom(),
				topIsMin ? getRealYBottom() : getRealYTop(),
				getLinearHeight(spacing));
	}
	
	static class Builder {
		private int gWidth;
		private int gHeight;
		private PlotBounds realBounds;
		private PlotConstraints constraints;
		private AxisScale xAxisScale;
		private AxisScale yAxisScale;
		
		private Builder() {
			constraints = new PlotConstraints();
		}
		
		Builder setSize(int width, int height) {
			gWidth = width;
			gHeight = height;
			return this;
		}
		
		Builder setBounds(PlotBounds bounds) {
			realBounds = new PlotBounds(bounds);
			return this;
		}
		
		Builder setConstraints(PlotConstraints constraints) {
			this.constraints = constraints;
			return this;
		}
		
		Builder setAxisScales(AxisScale xAxisScale, AxisScale yAxisScale) {
			this.xAxisScale = xAxisScale;
			this.yAxisScale = yAxisScale;
			return this;
		}
		
		PlotScale build() {
			return new PlotScale(this);
		}
	}
}
