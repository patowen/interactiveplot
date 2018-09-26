package net.patowen.interactiveplot;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
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
		double[] screenY = new double[plotScale.getWidth()];
		int off = plotScale.getPixelXLeft();
		
		synchronized (lock) {
			for (int i = 0; i < screenY.length; i++) {
				screenY[i] = plotScale.getScreenY(fun.apply(plotScale.getRealX(i+off+0.5)));
			}
		}
		
		if (fun != null) {
			g.setColor(Color.BLACK);
			for (int i = 0; i < screenY.length - 1; i++) {
				g.draw(new Line2D.Double(i+off, screenY[i], i+off+1, screenY[i+1]));
			}
		}
	}
}
