/*
 * Copyright (c) 2023 Anton Skripin
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package ansk.development;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Class to construct, show, and save charts with data from experiments.
 */
public class ChartConstructor extends JFrame {

    public ChartConstructor() {

    }

    /**
     * Displays a chart in a frame and saves it.
     *
     * @param name        of a chart
     * @param xName       name of X axis
     * @param gremlinData data for gremlin
     * @param shaclData   data for shacl
     */
    public void displayAndSave(String name, String xName, Map<Double, Double> gremlinData, Map<Double, Double> shaclData) {
        JFreeChart chart = constructChart(name, xName, gremlinData, shaclData);
        displayChart(chart);
        saveChart(name, chart);
    }

    /**
     * Displays a chart in a frame.
     *
     * @param name        of a chart
     * @param xName       name of X axis
     * @param gremlinData data for gremlin
     * @param shaclData   data for shacl
     */
    public void displayChart(String name, String xName, Map<Double, Double> gremlinData, Map<Double, Double> shaclData) {
        JFreeChart chart = constructChart(name, xName, gremlinData, shaclData);
        displayChart(chart);
    }

    /**
     * Saves a chart.
     *
     * @param name        of a chart
     * @param xName       name of X axis
     * @param gremlinData data for gremlin
     * @param shaclData   data for shacl
     */
    public void saveChart(String name, String xName, Map<Double, Double> gremlinData, Map<Double, Double> shaclData) {
        JFreeChart chart = constructChart(name, xName, gremlinData, shaclData);
        saveChart(name, chart);
    }

    private JFreeChart constructChart(String name, String xName, Map<Double, Double> gremlinData, Map<Double, Double> shaclData) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                name, // Chart title
                xName,
                "Execution time (ms)",
                constructDataset(gremlinData, shaclData),
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        customizeChart(chart);
        return chart;
    }

    private void customizeChart(JFreeChart chart) {
        XYPlot plot = chart.getXYPlot();

        var renderer = new XYSplineRenderer();

        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesPaint(1, Color.BLUE);
        renderer.setSeriesStroke(1, new BasicStroke(2.0f));

        plot.setRenderer(renderer);

        chart.getLegend().setFrame(BlockBorder.NONE);
    }

    private void displayChart(JFreeChart chart) {
        ChartPanel chartPanel = new ChartPanel(chart);
        setContentPane(chartPanel);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
    }

    private void saveChart(String name, JFreeChart chart) {
        try {
            ChartUtils.saveChartAsPNG(new File(String.format("./images/%s.png", name)), chart, 800, 800);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private XYDataset constructDataset(Map<Double, Double> gremlinData, Map<Double, Double> shaclData) {
        var gremlin = new XYSeries("Gremlin");
        gremlinData.forEach(gremlin::add);

        var shacl = new XYSeries("Shacl");
        shaclData.forEach(shacl::add);

        var dataset = new XYSeriesCollection();
        dataset.addSeries(gremlin);
        dataset.addSeries(shacl);
        return dataset;
    }
}
