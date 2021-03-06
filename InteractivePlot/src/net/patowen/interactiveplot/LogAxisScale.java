package net.patowen.interactiveplot;

import java.util.Optional;

/**
 * This axis scale is used for a log scale used to plot things that are separated by several
 * orders of magnitude. Labeling is designed in a way to allow a reasonably even distribution
 * of labels no matter what the scale is.
 * @author Patrick Owen
 */
public class LogAxisScale extends AxisScale
{
	public double getReal(double linear) {
		return Math.pow(10, linear);
	}
	
	public double getLinear(double real) {
		return Math.log10(real);
	}
	
	public TickLabelList getLabels(double linearMin, double linearMax, double minLinearInterval) {
		TickLabelList labels = new TickLabelList();
		int spacing = Decimal.nextLogIncrement(minLinearInterval);
		for (int tick = Math.floorDiv((int)Math.floor(linearMin), spacing) * spacing;
				tick <= linearMax; tick += spacing) {
			Decimal value = new Decimal(1, tick);
			if (tick >= linearMin) {
				labels.addLabel(new TickLabel(value.getDoubleValue(), value.toString(5)));
			}
			if (spacing == 1) {
				fill(labels, value, value.times(new Decimal(1, 1)), linearMin, linearMax, minLinearInterval);
			}
		}
		
		return labels;
	}
	
	private void fill(TickLabelList labels, Decimal min, Decimal max, double linearMin, double linearMax, double minLinearInterval) {
		if (min.compare(Math.pow(10, linearMax)) > 0) return;
		if (max.compare(Math.pow(10, linearMin)) < 0) return;
		
		Optional<Decimal> middle = Decimal.getMiddleValue(min, max, minLinearInterval, x -> getLinear(x));
		if (middle.isPresent()) {
			double linearMiddle = getLinear(middle.get().getDoubleValue());
			if (linearMiddle >= linearMin && linearMiddle <= linearMax) {
				labels.addLabel(new TickLabel(middle.get().getDoubleValue(), middle.get().toString(5)));
			}
			fill(labels, min, middle.get(), linearMin, linearMax, minLinearInterval);
			fill(labels, middle.get(), max, linearMin, linearMax, minLinearInterval);
		}
	}
	
	// Will be useful later on.
	public TickLabelList getLabelsOld(double linearMin, double linearMax, double minLinearInterval) {
		TickLabelList labels = new TickLabelList();
		double realMin = getReal(linearMin), realMax = getReal(linearMax);
		
		double minInterval = getReal(linearMax) - getReal(linearMax-minLinearInterval);
		Decimal spacing = Decimal.nextIncrement(minInterval);
		for (Decimal tick = Decimal.getLowerBound(realMin, spacing);
				tick.compare(realMax) <= 0; tick = tick.plus(spacing)) {
			if (tick.compare(realMin) >= 0) {
				labels.addLabel(new TickLabel(tick.getDoubleValue(), tick.toString(5)));
			}
		}
		
		return labels;
	}
}
