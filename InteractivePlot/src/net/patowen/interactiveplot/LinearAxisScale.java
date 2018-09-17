package net.patowen.interactiveplot;

/**
 * This is the most basic type of axis scale. Linear coordinates and real coordinates are
 * equivalent, and axes are labeled with numbers that are multiples of some power of ten times 1, 2,
 * or 5. Labels are made to be as close to each other as possible up to the minimum interval.
 * @author Patrick Owen
 */
public class LinearAxisScale extends AxisScale
{
	public double getReal(double linear) {
		return linear;
	}
	
	public double getLinear(double real) {
		return real;
	}
	
	public TickLabelList getLabels(double linearMin, double linearMax, double minLinearInterval) {
		TickLabelList labels = new TickLabelList();
		
		Decimal spacing = Decimal.nextIncrement(minLinearInterval);
		for (Decimal tick = Decimal.getLowerBound(linearMin, spacing);
				tick.compare(linearMax) <= 0; tick = tick.plus(spacing)) {
			if (tick.compare(linearMin) >= 0) {
				labels.addLabel(new TickLabel(tick.getDoubleValue(), tick.toString(5)));
			}
		}
		
		return labels;
	}
}
