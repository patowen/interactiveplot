package net.patowen.interactiveplot;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.function.Function;

public class PlotDataFunction extends PlotData {
	private Object lock = new Object();
	private Function<Double, Double> fun;
	
	public PlotDataFunction() {
		fun = null;
	}
	
	public synchronized void setFunction(Function<Double, Double> fun) {
		synchronized (lock) {
			this.fun = fun;
		}
	}
	
	public void drawData(Graphics2D g, PlotScale plotScale) {
		synchronized (lock) {
			if (fun != null) {
				g.setColor(Color.BLUE);
				for (int i = plotScale.getPixelXLeft(); i <= plotScale.getPixelXRight(); i++) {
					int j = plotScale.getPixelY(fun.apply(plotScale.getRealX(i+0.5)));
					g.drawLine((int)i, j, (int)i, plotScale.getPixelYBottom());
				}
			}
		}
	}
}
