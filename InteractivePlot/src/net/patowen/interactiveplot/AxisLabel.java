package net.patowen.interactiveplot;

/**
 * Represents a single label on either axis of the graph. Stores the position of the label in real
 * coordinates.
 * @author Patrick Owen
 */
public class AxisLabel {
	private double location;
	private String text;
	
	/**
	 * Creates a new {@code AxisLabel} with the given position in real coordinates and the given
	 * label text.
	 * @param location the x- or y-coordinate of the label
	 * @param text the text that the label displays
	 */
	public AxisLabel(double location, String text) {
		this.location = location;
		this.text = text;
	}
	
	/**
	 * Returns the x- or y-coordinate of the label.
	 */
	public double getLocation() {
		return location;
	}
	
	/**
	 * Returns the text displayed by the label.
	 */
	public String getText() {
		return text;
	}
}
