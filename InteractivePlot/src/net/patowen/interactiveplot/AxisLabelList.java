package net.patowen.interactiveplot;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents all the axis labels for a given axis, either the x- or y-axis.
 * @author Patrick Owen
 */
public class AxisLabelList {
	private List<AxisLabel> labels;
	
	/**
	 * Creates an empty {@code AxisLabelList}.
	 */
	public AxisLabelList() {
		labels = new ArrayList<>();
	}
	
	/**
	 * Returns a {@link List} containing all the labels on the corresponding axis in no guaranteed
	 * order.
	 */
	public List<AxisLabel> getLabels() {
		return labels;
	}
	
	/**
	 * Adds a label to the corresponding axis.
	 * @param label the text and location of the label to add
	 */
	public void addLabel(AxisLabel label) {
		labels.add(label);
	}
}
