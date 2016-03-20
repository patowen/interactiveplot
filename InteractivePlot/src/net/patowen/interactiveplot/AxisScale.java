package net.patowen.interactiveplot;

/**
 * This class provides the necessary information to convert between real and linear coordinates for
 * a single axis and label that axis in the best way.
 * @author Patrick Owen
 */
public abstract class AxisScale {
	/**
	 * Converts a single coordinate from linear coordinates to real coordinates.
	 * @param linear the coordinate in linear coordinates
	 * @return the coordinate in real coordinates
	 */
	public abstract double getReal(double linear);
	
	/**
	 * Converts a single coordinate from real coordinates to linear coordinates.
	 * @param linear the coordinate in real coordinates
	 * @return the coordinate in linear coordinates
	 */
	public abstract double getLinear(double real);
	
	/**
	 * Creates and returns a suitable {@link AxisLabelList} for the given bounds and interval.
	 * @param linearMin the minimum coordinate for a label in linear coordinates
	 * @param linearMax the maximum coordinate for a label in linear coordinates
	 * @param minLinearInterval the minimum interval between two labels in linear coordinates
	 */
	public abstract AxisLabelList getLabels(double linearMin, double linearMax, double minLinearInterval);
}
