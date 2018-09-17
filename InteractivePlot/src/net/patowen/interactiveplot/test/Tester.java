package net.patowen.interactiveplot.test;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import net.patowen.interactiveplot.AxisLabel;
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
		function.setFunction(x -> x*x+1);
		
		settings.setPlotData(function);
		settings.setYAxisScale(new LogAxisScale());
		
		frame.setLayout(new BorderLayout());
		frame.add(new InteractivePlot(settings), BorderLayout.CENTER);
		frame.add(new AxisLabel("Original Value", AxisLabel.Orientation.HORIZONTAL, null), BorderLayout.SOUTH);
		frame.add(new AxisLabel("Squared plus one", AxisLabel.Orientation.VERTICAL, null), BorderLayout.WEST);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
