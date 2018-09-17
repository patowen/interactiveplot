package net.patowen.interactiveplot;

import java.awt.Graphics2D;

/**
 * This class contains all the necessary information to draw everything in the interactive plot
 * besides its data. Subclasses define the exact style of the plot.
 * @author Patrick Owen
 */
public abstract class PlotStyle {
	/**
	 * This field can be accessed in any overridden method to gather data about the dimensions or
	 * scale information of the plot.
	 */
	protected PlotScale plotScale;
	
	/**
	 * The labels that should be displayed on the x-axis of the graph.
	 */
	protected TickLabelList xLabels;
	
	/**
	 * The labels that should be displayed on the y-axis of the graph.
	 */
	protected TickLabelList yLabels;
	
	void preparePainting(PlotScale plotScale) {
		this.plotScale = plotScale;
		xLabels = plotScale.getXLabels(getMajorAxisSpacingX());
		yLabels = plotScale.getYLabels(getMajorAxisSpacingY());
	}
	
	/**
	 * Override this method to draw everything in the plot that should be drawn before the plot's
	 * data. The graphics context is translated so that the top-left corner of the viewing window,
	 * including its boundary, is at the origin.
	 * @param g the graphics context
	 */
	protected abstract void paintBackground(Graphics2D g);
	
	/**
	 * Override this method to draw everything in the plot that should be drawn after the plot's
	 * data. The graphics context is translated so that the top-left corner of the viewing window,
	 * including its boundary, is at the origin.
	 * @param g the graphics context
	 */
	protected abstract void paintForeground(Graphics2D g);
	
	/**
	 * Returns the width of the left margin of the plot in pixels.
	 */
	protected abstract int getLeftMargin();
	
	/**
	 * Returns the width of the top margin of the plot in pixels.
	 */
	protected abstract int getTopMargin();
	
	/**
	 * Returns the width of the right margin of the plot in pixels.
	 */
	protected abstract int getRightMargin();
	
	/**
	 * Returns the width of the bottom margin of the plot in pixels.
	 */
	protected abstract int getBottomMargin();
	
	/**
	 * Returns the spacing between the labels of the x-axis in pixels. The difference between the
	 * x-coordinates of two consecutive labels will usually be greater than or equal to the value
	 * returned. There may be a small error due to floating point precision errors.
	 */
	protected abstract int getMajorAxisSpacingX();
	
	/**
	 * Returns the spacing between the labels of the y-axis in pixels. The difference between the
	 * y-coordinates of two consecutive labels will usually be greater than or equal to the value
	 * returned. There may be a small error due to floating point precision errors.
	 */
	protected abstract int getMajorAxisSpacingY();
	
	int getHorizontalMargin() {
		return getLeftMargin() + getRightMargin();
	}
	
	int getVerticalMargin() {
		return getTopMargin() + getBottomMargin();
	}
}
