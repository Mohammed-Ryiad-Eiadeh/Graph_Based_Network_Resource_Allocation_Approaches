package CurvesPlot.org;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * This class is used to display the convergence curve
 */
public class ConvergenceCurvePlot {
    private final XYChart plot;

    /**
     * This instructor is used to construct the plot object which will be used to display the chart
     * @param avgFitnessScores The vector of mean fitness scores as one for an iteration
     */
    public ConvergenceCurvePlot(double[] avgFitnessScores) {
        if (avgFitnessScores == null) {
            throw new IllegalArgumentException("The fitnes scores holder is empty");
        }
        this.plot = new XYChartBuilder().
                title("Convergence Curve").
                width(400).
                height(400).
                yAxisTitle("AVG fitness").
                xAxisTitle("Iteration").
                theme(Styler.ChartTheme.Matlab).
                build();

        plot.getStyler().setLegendPosition(Styler.LegendPosition.InsideSE);
        plot.getStyler().setMarkerSize(0);
        DecimalFormat format = new DecimalFormat("0.000");
        plot.addSeries("GA", Arrays.stream(avgFitnessScores).map(i -> Double.parseDouble( format.format(i))).toArray()).setLineColor(Color.black);
    }

    /**
     * This method is used to display the plot
     */
    public void ShowPlot() {
        new SwingWrapper<>(plot).displayChart();
    }
}
