package net.patowen.interactiveplot;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * This is the default {@link PlotMouseHandler}. It handles scrolling to zoom by choosing the axis
 * that the mouse is closer to.
 * @author Patrick Owen
 */
public class BasicPlotMouseHandler extends PlotMouseHandler {
	private double wheelScale;
	private double anchor;
	private boolean settingX;
	private boolean settingY;
	
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
	
	public void mousePressed(PlotScale plotScale, PlotMouseLocation location, MouseEvent e) {
		int xBias = Math.max(-location.getRelativeX(false), location.getRelativeX(true));
		int yBias = Math.max(-location.getRelativeY(false), location.getRelativeY(true));
		if (!location.inPlot()) {
			if (yBias > xBias) {
				settingX = true;
				settingY = false;
				anchor = location.getLinearX();
			} else if (xBias > yBias) {
				settingX = false;
				settingY = true;
				anchor = location.getLinearY();
			}
		}
	}
	
	public void mouseDragged(PlotScale plotScale, PlotMouseLocation location, MouseEvent e) {
		if (settingX) {
			plotScale.translateX(anchor - location.getLinearX());
		} else if (settingY) {
			plotScale.translateY(anchor - location.getLinearY());
		}
		repaint();
	}
	
	public void mouseReleased(PlotScale plotScale, PlotMouseLocation location, MouseEvent e) {
		settingX = false;
		settingY = false;
	}
	
	public void mouseWheelMoved(PlotScale plotScale, PlotMouseLocation location, MouseWheelEvent e) {
		int xBias = Math.max(-location.getRelativeX(false), location.getRelativeX(true));
		int yBias = Math.max(-location.getRelativeY(false), location.getRelativeY(true));
		if (yBias > xBias) {
			plotScale.zoomX(location.getLinearX(), Math.pow(wheelScale, e.getPreciseWheelRotation()));
		} else if (xBias > yBias) {
			plotScale.zoomY(location.getLinearY(), Math.pow(wheelScale, e.getPreciseWheelRotation()));
		}
		repaint();
	}
}
