package net.patowen.interactiveplot;

/**
 * Represents the location of the mouse on an {@link InteractivePlot} when a mouse event has been
 * triggered. It contains the location of the mouse in absolute coordinates (relative to the
 * top-left corner of the {@link InteractivePlot} component). It also contains the location of the
 * mouse in pixel coordinates, bounded by the edges of the visible portion of the plot. The location
 * of the mouse in screen coordinates, linear coordinates and real coordinates can be retrieved.
 * @author Patrick Owen
 */
public class PlotMouseLocation {
	private final PlotScale plotScale;
	
	private final int xLeft;
	private final int xRight;
	private final int yTop;
	private final int yBottom;
	
	private final int absoluteX;
	private final int absoluteY;
	private final int pixelX;
	private final int pixelY;
	
	PlotMouseLocation(PlotScale plotScale, int leftMargin, int topMargin, int absoluteX, int absoluteY) {
		this.plotScale = plotScale;
		
		xLeft = leftMargin;
		xRight = leftMargin + plotScale.getWidth() - 1;
		yTop = topMargin;
		yBottom = topMargin + plotScale.getHeight() - 1;
		
		this.absoluteX = absoluteX;
		this.absoluteY = absoluteY;
		
		int pixelX = absoluteX - leftMargin;
		int pixelY = absoluteY - topMargin;
		
		if (pixelX < plotScale.getPixelXLeft()) {
			pixelX = plotScale.getPixelXLeft();
		}
		
		if (pixelX > plotScale.getPixelXRight()) {
			pixelX = plotScale.getPixelXRight();
		}
		
		if (pixelY < plotScale.getPixelYTop()) {
			pixelY = plotScale.getPixelYTop();
		}
		
		if (pixelY > plotScale.getPixelYBottom()) {
			pixelY = plotScale.getPixelYBottom();
		}
		
		this.pixelX = pixelX;
		this.pixelY = pixelY;
	}
	
	/**
	 * Returns the x-coordinate of the mouse in absolute coordinates.
	 */
	public int getAbsoluteX() {
		return absoluteX;
	}
	
	/**
	 * Returns the y-coordinate of the mouse in absolute coordinates.
	 */
	public int getAbsoluteY() {
		return absoluteY;
	}
	
	/**
	 * Returns the x-coordinate of the mouse relative to the left or right edge of the plot
	 * window.
	 * @param rightEdge If set to true, the x-coordinate is relative to the right edge of the plot
	 * window. Otherwise, it is relative to the left edge.
	 */
	public int getRelativeX(boolean rightEdge) {
		return rightEdge ? absoluteX - xRight : absoluteX - xLeft;
	}
	
	/**
	 * Returns the y-coordinate of the mouse relative to the top or bottom edge of the plot
	 * window.
	 * @param rightEdge If set to true, the x-coordinate is relative to the bottom edge of the plot
	 * window. Otherwise, it is relative to the top edge.
	 */
	public int getRelativeY(boolean bottomEdge) {
		return bottomEdge ? absoluteY - yBottom : absoluteY - yTop;
	}
	
	/**
	 * Returns whether the mouse is inside the plot, including the boundary.
	 */
	public boolean inPlot() {
		return absoluteX >= xLeft && absoluteX <= xRight
				&& absoluteY >= yTop && absoluteY <= yBottom;
	}
	
	/**
	 * Returns the x-coordinate of the mouse in pixel coordinates.
	 */
	public int getPixelX() {
		return pixelX;
	}
	
	/**
	 * Returns the y-coordinate of the mouse in pixel coordinates.
	 */
	public int getPixelY() {
		return pixelY;
	}
	
	/**
	 * Returns the x-coordinate of the mouse in screen coordinates, assuming the mouse is centered
	 * on the pixel.
	 */
	public double getScreenX() {
		return pixelX + 0.5;
	}
	
	/**
	 * Returns the y-coordinate of the mouse in screen coordinates, assuming the mouse is centered
	 * on the pixel.
	 */
	public double getScreenY() {
		return pixelY + 0.5;
	}
	
	/**
	 * Returns the x-coordinate of the mouse in linear coordinates, assuming the mouse is centered
	 * on the pixel.
	 */
	public double getLinearX() {
		return plotScale.getLinearX(getScreenX());
	}
	
	/**
	 * Returns the y-coordinate of the mouse in linear coordinates, assuming the mouse is centered
	 * on the pixel.
	 */
	public double getLinearY() {
		return plotScale.getLinearY(getScreenY());
	}
	
	/**
	 * Returns the x-coordinate of the mouse in real coordinates, assuming the mouse is centered
	 * on the pixel.
	 */
	public double getRealX() {
		return plotScale.getRealX(getScreenX());
	}
	
	/**
	 * Returns the y-coordinate of the mouse in real coordinates, assuming the mouse is centered
	 * on the pixel.
	 */
	public double getRealY() {
		return plotScale.getRealY(getScreenY());
	}
}
