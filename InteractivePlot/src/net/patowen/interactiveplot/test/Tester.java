package net.patowen.interactiveplot.test;

import javax.swing.JFrame;

import net.patowen.interactiveplot.InteractivePlot;
import net.patowen.interactiveplot.LogAxisScale;
import net.patowen.interactiveplot.PlotDataFunction;

public class Tester {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		InteractivePlot.Settings settings = new InteractivePlot.Settings();
		settings.setBounds(-3, 3, 10, 1);
//		PlotConstraints constraints = new PlotConstraints();
//		constraints.setBounds(-1, 1, 0, 1);
//		settings.setPlotConstraints(constraints);
		
		PlotDataFunction function = new PlotDataFunction();
		function.addFunction(x -> x*x+1);
		function.switchFunction();
		
		settings.setPlotData(function);
		settings.setYAxisScale(new LogAxisScale());
		
		frame.add(new InteractivePlot(settings));
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
