package util;

import model.DailyValues;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.IOException;
import java.util.List;

/**
 * class that handles everything that has to do with the generation of a line chart, based on the JFreeChart library
 */
public class LineChartGenerator {

    /**
     * this method takes a list of DailyValues and generates a chart based on them
     *
     * @param selectedDailyValuesList : the range of the DailyValues list
     * @return: returns a generated chart, based on the values inputted
     */
    public static JFreeChart generateChart(List<DailyValues> selectedDailyValuesList) throws IOException {
        // Create dataset
        DefaultCategoryDataset dataset = createDataset(selectedDailyValuesList);
        // Create chart
        JFreeChart chart = ChartFactory.createLineChart(
                "Daily Values for Specified Period", // Chart title
                "Date", // X-Axis Label
                "", // Y-Axis Label
                dataset
        );
        //set dates axis vertically
        chart.getCategoryPlot().getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        return chart;
    }

    /**
     * this method takes the DailyValues list and populates a dataset, in order to be used to create a chart
     *
     * @param selectedDailyValuesList : the DailyValuesList between two dates (all if no period specified)
     * @return : a populated dataset
     */
    private static DefaultCategoryDataset createDataset(List<DailyValues> selectedDailyValuesList) {
        String series1 = "Glucose Level (mg/dL)";
        String series2 = "Carb Intake (grams)";
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        //set the values and create the dataset
        for (DailyValues selectedDailyValues : selectedDailyValuesList) {
            dataset.addValue(selectedDailyValues.getGlucose_level(), series1, Time.returnYYYYMMDDString(selectedDailyValues.getInput_date()));
            dataset.addValue(selectedDailyValues.getCarb_intake(), series2, Time.returnYYYYMMDDString(selectedDailyValues.getInput_date()));
        }
        return dataset;
    }
}

