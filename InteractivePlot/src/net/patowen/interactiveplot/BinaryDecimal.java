package net.patowen.interactiveplot;

class BinaryDecimal
{
	// The value is coefficient * 2 ^ binExponent * 10 ^ decExponent. This must be non-negative.
	private long coefficient; //Contract: coefficient is positive. Undefined results otherwise.
	private int binExponent;
	private int decExponent;
	
	public BinaryDecimal(long coefficient, int binExponent, int decExponent) {
		this.coefficient = coefficient;
		this.binExponent = binExponent;
		this.decExponent = decExponent;
	}
	
	public BinaryDecimal(BinaryDecimal old) {
		this(old.coefficient, old.binExponent, old.decExponent);
	}
	
	// Not-so-useful method: not monotonic.
	public double approximateValue() {
		return coefficient * Math.pow(2, binExponent) * Math.pow(10, decExponent);
	}
	
	public static BinaryDecimal fromDouble(double value) {
		if (value == 0) {
			return new BinaryDecimal(0, 0, 0);
		}
		
		long fraction = (Double.doubleToRawLongBits(value) & 0x000f_ffff_ffff_ffffL) | 0x0010_0000_0000_0000L;
		int exp = Math.getExponent(value);
		if (exp < Double.MIN_EXPONENT) {
			exp += 1;
			fraction &= 0x000f_ffff_ffff_ffffL;
		} else if (exp > Double.MAX_EXPONENT) {
			throw new IllegalArgumentException("Argument must be a real number"); // NaN is bad.
		}
		exp -= 52;
		
		while ((fraction & 1) == 0) {
			fraction >>>= 1;
			exp ++;
		}
		
		return new BinaryDecimal(fraction, exp, 0);
	}
	
	public String toString() {
		return coefficient + " " + binExponent + " " + decExponent;
	}
	
	/**
	 * Returns within 1 ulp of the highest representable number <= the current number, with the decExponent decreased by 1.
	 * Returns true if the result is exact.
	 * Contract: newValue <= oldValue, and everything including the behavior of exact is monotonic.
	 */
	public boolean decrementDecExponent() {
		boolean exact = true;
		
		if (coefficient == 0) {
			decExponent --;
			return true;
		}
		
		while (coefficient > Long.MAX_VALUE / 5L) {
			exact = exact && ((coefficient & 1L) == 0);
			coefficient >>>= 1;
			binExponent ++;
		}
		
		coefficient *= 5;
		binExponent ++;
		decExponent --;
		
		return exact;
	}
	
	/**
	 * Returns within 1 ulp of the highest representable number <= the current number, with the decExponent increased by 1.
	 * Returns true if the result is exact.
	 * Contract: newValue <= oldValue, and everything including the behavior of exact is monotonic.
	 */
	public boolean incrementDecExponent() {
		boolean exact = true;
		
		if (coefficient == 0) {
			decExponent ++;
			return true;
		}
		
		while ((coefficient & 0x4000_0000_0000_0000L) == 0) {
			coefficient <<= 1;
			binExponent --;
		}
		
		exact = exact && ((coefficient % 5) == 0);
		coefficient /= 5;
		binExponent --;
		decExponent ++;
		
		return exact;
	}
	
	/**
	 * Returns -1 if this < comparison, 1 if this > comparison, and 0 if this = comparison
	 * Contract: comparison >= 0
	 * @param comparison
	 * @return
	 */
	public int compare(double comparison) {
		if (comparison == 0 && coefficient == 0) return 0;
		if (comparison == 0) return 1;
		if (coefficient == 0) return -1;
		
		BinaryDecimal clone = new BinaryDecimal(this);
		boolean exact = true;
		
		while (clone.decExponent > 0) {
			exact = clone.decrementDecExponent() && exact;
		}
		
		while (clone.decExponent < 0) {
			exact = clone.incrementDecExponent() && exact;
		}
		
		return clone.compareRaw(comparison, exact?0:1);
	}
	
	/**
	 * Returns -1 if this < comparison, 1 if this > comparison, and equalValue if this = comparison
	 * Contract: decExponent = 0, coefficient != 0, comparison != 0, comparison >= 0
	 * @param comparison
	 * @param equalValue
	 */
	public int compareRaw(double comparison, int equalValue) {
		long fraction = (Double.doubleToRawLongBits(comparison) & 0x000f_ffff_ffff_ffffL) | 0x0010_0000_0000_0000L;
		int exp = Math.getExponent(comparison);
		if (exp < Double.MIN_EXPONENT) {
			exp += 1;
			fraction &= 0x000f_ffff_ffff_ffffL;
		} else if (exp > Double.MAX_EXPONENT) {
			throw new IllegalArgumentException("Argument must be a real number"); // NaN is bad.
		}
		exp -= 52;
		if (binExponent >= exp) {
			int shiftAmount = binExponent - exp;
			if (shiftAmount > 0 && (shiftAmount >= 63 || coefficient >>> (63 - shiftAmount) != 0)) {
				return 1; // temp will overflow
			}
			long temp = coefficient << shiftAmount; // binExponent must be reduced, so coefficient must be increased.
			if (temp < fraction) {
				return -1;
			} else if (temp > fraction) {
				return 1;
			} else {
				return equalValue;
			}
		} else {
			int shiftAmount = exp - binExponent;
			if (shiftAmount > 0 && (shiftAmount >= 63 || fraction >>> (63 - shiftAmount) != 0)) {
				return -1; // temp will overflow
			}
			long temp = fraction << shiftAmount; // exp must be reduced, so fraction must be increased.
			if (coefficient < temp) {
				return -1;
			} else if (coefficient > temp) {
				return 1;
			} else {
				return equalValue;
			}
		}
	}
}
