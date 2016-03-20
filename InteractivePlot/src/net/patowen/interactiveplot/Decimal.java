package net.patowen.interactiveplot;

import java.util.Optional;
import java.util.function.Function;

public class Decimal {
	// The value is coefficient * base ^ exponent
	private long coefficient; //Consider BigInteger
	private static final int BASE = 10;
	private int exponent;
	
	public Decimal(long coefficient, int exponent) {
		this.coefficient = coefficient;
		this.exponent = exponent;
		reduce();
	}
	
	public Decimal(Decimal n) {
		coefficient = n.coefficient;
		exponent = n.exponent;
	}
	
	public Decimal(long value) {
		this(value, 0);
	}
	
	public static Decimal fromDouble(double value, int numNumerals, Function<Double, Long> fun) {
		if (value == 0) {
			return new Decimal(0, 0);
		}
		boolean negative = false;
		if (value < 0) {
			value = -value;
			negative = true;
		}
		long coefficient = 0;
		int exponent = 0;
		long min = 1, max = BASE;
		for (int i=1; i<numNumerals; i++) {
			min = max;
			max *= BASE;
		}
		while (fun.apply(value) >= max) {
			value /= BASE;
			exponent ++;
		}
		while (fun.apply(value) <= min) {
			value *= BASE;
			exponent --;
		}
		coefficient = fun.apply(value);
		if (coefficient >= max) {
			coefficient /= BASE;
			exponent ++;
		}
		
		if (negative) {
			coefficient = -coefficient;
		}
		return new Decimal(coefficient, exponent);
	}
	
	public static Decimal fromDoubleRoundUp(double value, int numNumerals) {
		return fromDouble(value, numNumerals, x->(long)Math.ceil(x));
	}
	
	public static Decimal fromDoubleRoundDown(double value, int numNumerals) {
		return fromDouble(value, numNumerals, x->(long)Math.floor(x));
	}
	
	public static Decimal nextIncrement(double increment) {
		Decimal next = Decimal.fromDoubleRoundUp(increment, 1);
		if (next.coefficient > 5) {
			next.coefficient = 10;
			next.reduce();
		} else if (next.coefficient > 1) {
			next.coefficient = 5;
		}
		return next;
	}
	
	//TODO Begin log stuff
	public static int nextLogIncrement(double increment) {
		if (increment < 1) {
			return 1;
		} else {
			int nextIncrement = 3;
			while (increment >= nextIncrement) {
				nextIncrement *= 2;
			}
			return nextIncrement;
		}
	}
	
	public static Optional<Decimal> getMiddleValue(Decimal lowerBound, Decimal upperBound,
			double minLinearInterval, Function<Double, Double> realToLinear) {
		Decimal difference = upperBound.minus(lowerBound);
		Decimal interval;
		int numIntervals;
		if (difference.coefficient == 1) {
			interval = new Decimal(1, difference.exponent - 1);
			numIntervals = 10;
		} else {
			interval = new Decimal(1, difference.exponent);
			numIntervals = (int)difference.coefficient;
		}
		Decimal testMiddle = lowerBound;
		Optional<Decimal> currentMiddle = Optional.empty();
		double currentEdgeDifference = minLinearInterval;
		
		double upperBoundLinear = realToLinear.apply(upperBound.getDoubleValue());
		double lowerBoundLinear = realToLinear.apply(lowerBound.getDoubleValue());
		for (int i=0; i<numIntervals; i++) {
			testMiddle = testMiddle.plus(interval);
			double testMiddleLinear = realToLinear.apply(testMiddle.getDoubleValue());
			double testEdgeDifference = Math.min(upperBoundLinear-testMiddleLinear, testMiddleLinear-lowerBoundLinear);
			
			if (testEdgeDifference >= currentEdgeDifference) {
				currentMiddle = Optional.of(testMiddle);
				currentEdgeDifference = testEdgeDifference;
			}
		}
		
		return currentMiddle;
	}
	//TODO End log stuff
	
	public static Decimal getFirstMultiple(Decimal min, Decimal factor) {
		if (min.coefficient == 0) {
			return new Decimal(0, 0);
		}
		min = new Decimal(min);
		min.expand(factor.exponent);
		while (min.exponent < factor.exponent) {
			min.coefficient = min.coefficient/BASE + 1;
			min.exponent ++;
		}
		long secondFactor = -Math.floorDiv(-min.coefficient, factor.coefficient);
		return factor.times(secondFactor);
	}
	
	/**
	 * Returns a decimal close to but lower than val and divisible by factor.
	 * @param val
	 * @param factor The value returned will be divisible by this argument. It must be positive.
	 */
	public static Decimal getLowerBound(double val, Decimal factor) {
		if (val >= 0.0) {
			return getLowerBoundRaw(val, factor);
		} else {
			return getUpperBoundRaw(-val, factor).negate();
		}
	}
	
	/**
	 * Returns a decimal close to but higher than val and divisible by factor.
	 * @param val
	 * @param factor The value returned will be divisible by this argument. It must be positive.
	 */
	public static Decimal getUpperBound(double val, Decimal factor) {
		if (val >= 0.0) {
			return getUpperBoundRaw(val, factor);
		} else {
			return getLowerBoundRaw(-val, factor).negate();
		}
	}
	
	/**
	 * Returns a decimal close to but lower than val and divisible by factor.
	 * Contract: val, factor > 0 -> result <= val, factor | result.
	 * @param val
	 * @param factor
	 */
	private static Decimal getLowerBoundRaw(double val, Decimal factor) {
		long valCoeff = (long)Math.floor(Math.nextDown(val*Math.nextDown(Math.pow(BASE, -factor.exponent))));
		long factor2 = Math.floorDiv(valCoeff, factor.coefficient);
		return factor.times(factor2);
	}
	
	/**
	 * Returns a decimal close to but higher than val and divisible by factor.
	 * Contract: val, factor > 0 -> result >= val, factor | result.
	 * @param val
	 * @param factor
	 */
	private static Decimal getUpperBoundRaw(double val, Decimal factor) {
		long valCoeff = (long)Math.ceil(Math.nextUp(val*Math.nextUp(Math.pow(BASE, -factor.exponent))));
		long factor2 = Math.floorDiv(valCoeff, factor.coefficient)+1;
		return factor.times(factor2);
	}
	
	/**
	 * Returns -1 if the decimal < comparison, 1 if >, and 0 if =.
	 * Contract: This comparison must be monotonic and close to correct.
	 * Note: Rounding toward 0 consistently maintains monotonicity, even though there
	 * is a direction swap at 0.
	 * @param comparison
	 */
	public int compare(double comparison) {
		int sign = 1;
		if (coefficient < 0 && comparison < 0) {
			sign = -1;
		} else if (coefficient < 0) {
			return -1;
		} else if (comparison < 0) {
			return 1;
		}
		
		return sign*(new BinaryDecimal(coefficient*sign, 0, exponent)).compare(comparison);
	}
	
	/**
	 * Returns a double that is within 1 ulp of the decimal value (not verified).
	 */
	public double getDoubleValue() {
		return (double)coefficient*Math.pow(BASE, exponent);
	}
	
	private void reduce() {
		if (coefficient == 0) {
			exponent = 0;
		} else {
			while (coefficient % BASE == 0) {
				coefficient /= BASE;
				exponent ++;
			}
		}
	}
	
	private void expand(int newExponent) {
		if (coefficient == 0) {
			if (exponent > newExponent) {
				exponent = newExponent;
			}
		} else {
			while (exponent > newExponent) {
				coefficient *= BASE;
				exponent --;
			}
		}
	}
	
	public Decimal plus(Decimal n) {
		n = new Decimal(n);
		expand(n.exponent);
		n.expand(exponent);
		n.coefficient += coefficient;
		n.reduce();
		return n;
	}
	
	public Decimal minus(Decimal n) {
		n = new Decimal(n);
		expand(n.exponent);
		n.expand(exponent);
		n.coefficient = coefficient - n.coefficient;
		n.reduce();
		return n;
	}
	
	// Warning: this will overflow rather than losing precision.
	public Decimal times(Decimal n) {
		n = new Decimal(coefficient * n.coefficient, exponent + n.exponent);
		n.reduce();
		return n;
	}
	
	public Decimal times(long factor) {
		Decimal n = new Decimal(coefficient * factor, exponent);
		n.reduce();
		return n;
	}
	
	public Decimal negate() {
		return new Decimal(-coefficient, exponent);
	}
	
	public String toString(int maxPreferredDigits) {
		StringBuilder str = new StringBuilder();
		
		if (coefficient == 0) {
			str.append('0');
		} else if (exponent >= 0) {
			str.append(getStandardNotation());
		} else {
			str.append(getStandardNotation());
		}
		return str.toString();
	}
	
	private String getStandardNotation() {
		StringBuilder str = new StringBuilder();
		long coeff = coefficient, exp = exponent;
		boolean negative = false;
		if (coeff < 0) {
			coeff = -coeff;
			negative = true;
		}
		for (int i=0; i<exp; i++) {
			str.append('0');
		}
		for (; coeff != 0 || exp <= 0; coeff /= BASE, exp ++) {
			if (exponent < 0 && exp == 0) {
				str.append('.');
			}
			str.append(coeff % BASE);
		}
		if (negative) {
			str.append("-");
		}
		return str.reverse().toString();
	}
}
