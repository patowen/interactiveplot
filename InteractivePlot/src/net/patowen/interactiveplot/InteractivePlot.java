package net.patowen.interactiveplot;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;

import javax.swing.JComponent;

/**
 * This class is a swing component that can be used to graph data in a flexible manner.
 * @author Patrick Owen
 */
@SuppressWarnings("serial")
public final class InteractivePlot extends JComponent {
	private PlotStyle axisStyle;
	
	private int width, height;
	
	private PlotScale plotScale;
	private PlotData plotData;
	
	private PlotMouseHandler mouseHandler;
	
	/**
	 * Creates an {@code InteractivePlot} with the given settings.
	 * @param settings the desired properties of the plot.
	 */
	public InteractivePlot(Settings settings) {
		mouseHandler = settings.mouseHandler;
		if (mouseHandler != null) {
			new MouseHandler(this);
		}
		mouseHandler.setParent(this);
		new ComponentHandler(this);
		
		axisStyle = settings.axisStyle;
		
		setPreferredSize(new Dimension(
				settings.plotWidth + axisStyle.getHorizontalMargin(),
				settings.plotHeight + axisStyle.getVerticalMargin()));
		setMinimumSize(new Dimension(axisStyle.getHorizontalMargin()+16, axisStyle.getVerticalMargin()+16));
		
		plotScale = PlotScale.getBuilder()
				.setBounds(settings.bounds)
				.setSize(settings.plotWidth, settings.plotHeight)
				.setConstraints(settings.constraints)
				.setAxisScales(settings.xAxisScale, settings.yAxisScale)
				.build();
		
		plotData = settings.plotData;
		if (plotData == null) {
			throw new IllegalArgumentException("No plot data set.");
		}
		plotData.setParent(this);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		AffineTransform savedTransform = g2.getTransform();
		g2.translate(axisStyle.getLeftMargin(), axisStyle.getTopMargin());
		
		axisStyle.preparePainting(plotScale);
		axisStyle.paintBackground(g2);
		
		Shape savedClip = g2.getClip();
		g2.setClip(0, 0, plotScale.getWidth(), plotScale.getHeight());
		plotData.drawData(g2, plotScale);
		g2.setClip(savedClip);
		
		axisStyle.paintForeground(g2);
		
		g2.setTransform(savedTransform);
	}
	
	/**
	 * Returns a {@code PlotMouseLocation} based on the given absolute coordinates of the mouse.
	 * @param x the x-coordinate of the mouse in absolute coordinates.
	 * @param y the y-coordinate of the mosue in absolute coordinates.
	 */
	public PlotMouseLocation getMouseLocation(int x, int y) {
		return new PlotMouseLocation(plotScale, axisStyle.getLeftMargin(), axisStyle.getTopMargin(), x, y);
	}
	
	private class MouseHandler extends MouseAdapter {
		private MouseHandler(JComponent component) {
			component.addMouseListener(this);
			component.addMouseMotionListener(this);
			component.addMouseWheelListener(this);
		}
		
		public void mousePressed(MouseEvent e) {
			mouseHandler.mousePressed(plotScale, getMouseLocation(e.getX(), e.getY()), e);
		}
		
		public void mouseReleased(MouseEvent e) {
			mouseHandler.mouseReleased(plotScale, getMouseLocation(e.getX(), e.getY()), e);
		}
		
		public void mouseDragged(MouseEvent e) {
			mouseHandler.mouseDragged(plotScale, getMouseLocation(e.getX(), e.getY()), e);
		}
		
		public void mouseWheelMoved(MouseWheelEvent e) {
			mouseHandler.mouseWheelMoved(plotScale, getMouseLocation(e.getX(), e.getY()), e);
		}
	}
	
	private class ComponentHandler extends ComponentAdapter {
		private ComponentHandler(JComponent component) {
			component.addComponentListener(this);
		}
		
		public void componentResized(ComponentEvent e) {
			width = getWidth();
			height = getHeight();
			plotScale.setSize(
					Math.max(2, width-axisStyle.getHorizontalMargin()),
					Math.max(2, height-axisStyle.getVerticalMargin()));
			repaint();
		}
	}
	
	/**
	 * Contains the settings that will be created with an {@code InteractivePlot} with these
	 * settings.
	 * @author Patrick Owen
	 */
	public static final class Settings {
		private PlotStyle axisStyle;
		private PlotConstraints constraints;
		private PlotBounds bounds;
		private PlotData plotData;
		private int plotWidth;
		private int plotHeight;
		private AxisScale xAxisScale;
		private AxisScale yAxisScale;
		private PlotMouseHandler mouseHandler;
		
		/**
		 * Constructs a {@code InteractivePlot.Settings} object with default settings. Every setting
		 * has a default except for the plot data, which must be set manually.
		 */
		public Settings() {
			axisStyle = new BasicPlotStyle();
			constraints = new PlotConstraints();
			bounds = new PlotBounds(0, 1, 0, 1);
			plotData = null;
			plotWidth = 640;
			plotHeight = 480;
			xAxisScale = new LinearAxisScale();
			yAxisScale = new LinearAxisScale();
			mouseHandler = new BasicPlotMouseHandler();
		}
		
		/**
		 * Sets the axis style, which determines the look and feel of the plot.
		 */
		public void setAxisStyle(PlotStyle plotStyle) {
			if (plotStyle == null) {
				throw new IllegalArgumentException("plotStyle cannot be null");
			}
			this.axisStyle = plotStyle;
		}
		
		/**
		 * Sets the plot constraints, which limit how the user can navigate the plot to prevent
		 * issues from panning and zooming in an unintended way.
		 */
		public void setPlotConstraints(PlotConstraints constraints) {
			if (constraints == null) {
				throw new IllegalArgumentException("constraints cannot be null");
			}
			this.constraints = constraints;
		}
		
		/**
		 * Sets the coordinates of the boundaries of the plot in real coordinates.
		 * @param xLeft the x-coordinate of the left edge of the plot
		 * @param xRight the x-coordinate of the right edge of the plot
		 * @param yTop the y-coordinate of the top edge of the plot
		 * @param yBottom the y-coordinate of the bottom edge of the plot
		 */
		public void setBounds(double xLeft, double xRight, double yTop, double yBottom) {
			this.bounds = new PlotBounds(xLeft, xRight, yTop, yBottom);
		}
		
		/**
		 * Sets the plot data, which determines the contents of the plot. This is a required
		 * setting, as it has no default.
		 */
		public void setPlotData(PlotData plotData) {
			if (plotData == null) {
				throw new IllegalArgumentException("plotData cannot be null");
			}
			this.plotData = plotData;
		}
		
		/**
		 * Sets the intended size of the viewing portion of the plot, including the boundary, in
		 * pixels.
		 * @param plotWidth the width of the plot in pixels
		 * @param plotHeight the height of the plot in pixels
		 */
		public void setPlotSize(int plotWidth, int plotHeight) {
			this.plotWidth = plotWidth;
			this.plotHeight = plotHeight;
		}
		
		/**
		 * Sets the scale and labeling schema of the x-axis.
		 */
		public void setXAxisScale(AxisScale xAxisScale) {
			if (xAxisScale == null) {
				throw new IllegalArgumentException("xAxisScale cannot be null");
			}
			this.xAxisScale = xAxisScale;
		}
		
		/**
		 * Sets the scale and labeling schema of the y-axis.
		 */
		public void setYAxisScale(AxisScale yAxisScale) {
			if (yAxisScale == null) {
				throw new IllegalArgumentException("yAxisScale cannot be null");
			}
			this.yAxisScale = yAxisScale;
		}
		
		public void setMouseHandler(PlotMouseHandler mouseHandler) {
			if (mouseHandler == null) {
				throw new IllegalArgumentException("mouseHandler cannot be null");
			}
			this.mouseHandler = mouseHandler;
		}
	}
}
