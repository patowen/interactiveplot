package net.patowen.interactiveplot;

// TODO Remove.
public class Analyzer
{
	public static String getBinary(long value, int numDigits) {
		StringBuilder str = new StringBuilder();
		for (int i=0; i<numDigits; i++) {
			str.append((value >> (numDigits-1-i))&1);
		}
		return str.toString();
	}
	

	public static String getBinary(int value, int numDigits) {
		StringBuilder str = new StringBuilder();
		for (int i=0; i<numDigits; i++) {
			str.append((value >> (numDigits-1-i))&1);
		}
		return str.toString();
	}
	
	public static String getBinary(double value) {
		long longValue = Double.doubleToRawLongBits(value);
		StringBuilder str = new StringBuilder();
		for (int i=0; i<64; i++) {
			if (i == 1 || i == 12) str.append(" ");
			str.append((longValue >> (63-i))&1);
		}
		return str.toString();
	}
	
	public static class SplitDouble {
		public long coefficient;
		public int exponent;
		public SplitDouble(long coefficient, int exponent) {
			this.coefficient = coefficient;
			this.exponent = exponent;
		}
	}
	
	/**
	 * Contract: value is non-negative
	 */
	public static SplitDouble splitDouble(double value) {
		long fraction = (Double.doubleToRawLongBits(value) & 0x000f_ffff_ffff_ffffL) | 0x0010_0000_0000_0000L;
		int exp = Math.getExponent(value);
		if (exp < Double.MIN_EXPONENT) {
			exp += 1;
			fraction &= 0x000f_ffff_ffff_ffffL;
		} else if (exp > Double.MAX_EXPONENT) {
			throw new IllegalArgumentException("Argument must be a real number");
		}
		exp -= 52;
		return new SplitDouble(fraction, exp);
	}
}
