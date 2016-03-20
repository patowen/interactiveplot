package net.patowen.interactiveplot;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Function;

/**
 * TODO: Use Java's built-in Function class.
 */
public class PlotDataFunction extends PlotData {
	private Function<Double, Double> fun;
	private ArrayBlockingQueue<Function<Double, Double>> funQueue;
	
	public PlotDataFunction() {
		funQueue = new ArrayBlockingQueue<>(256);
		fun = null;
	}
	
	public synchronized void addFunction(Function<Double, Double> fun) {
		funQueue.offer(fun);
	}
	
	public synchronized void switchFunction() {
		if (!funQueue.isEmpty()) {
			fun = funQueue.poll();
			repaint();
		}
	}
	
	public void drawData(Graphics2D g, PlotScale plotScale) {
		if (fun != null) {
			synchronized (fun) {
				g.setColor(Color.BLUE);
				for (int i = plotScale.getPixelXLeft(); i <= plotScale.getPixelXRight(); i++) {
					int j = plotScale.getPixelY(fun.apply(plotScale.getRealX(i+0.5)));
					g.drawLine((int)i, j, (int)i, plotScale.getPixelYBottom());
				}
			}
		}
	}
}
