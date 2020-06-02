//import javafx.application.Application;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//
///**
// * class to test anything
// */
//public class Tester extends Application {
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage stage) throws IOException {
////        stage.setTitle("Line Chart Sample");
////        //defining the axes
////        final NumberAxis xAxis = new NumberAxis();
////        final NumberAxis yAxis = new NumberAxis();
////        xAxis.setLabel("Date");
////        //creating the chart
////        final LineChart<Number,Number> lineChart =
////                new LineChart<Number,Number>(xAxis,yAxis);
////        lineChart.setAnimated(false);
////
////        lineChart.setTitle("Daily Depiction for Specified Period");
////        //defining a series
////        XYChart.Series glucose_levels_series = new XYChart.Series();
////        glucose_levels_series.setName("Glucose Level (mg/dL)");
////        //populating the series with data
////        glucose_levels_series.getData().add(new XYChart.Data(1, 23));
////
////        XYChart.Series carb_intake_series = new XYChart.Series();
////        carb_intake_series.setName("Carb Intake (grams)");
////        //populating the series with data
////        carb_intake_series.getData().add(new XYChart.Data(1, 25));
////
////
////        Scene scene  = new Scene(lineChart,800,600);
////        lineChart.getData().add(glucose_levels_series);
////        lineChart.getData().add(carb_intake_series);
////        //stage.setScene(scene);
////        //stage.show();
////
////        WritableImage snapShot = scene.snapshot(null);
////        ImageIO.write(SwingFXUtils.fromFXImage(snapShot, null), "png", new File("test.png"));
//        //LineChartGenerator.drawChart(selectedDailyValues);
//        //FreeLineChartGenerator.generateChart(selectedDailyValues);
//    }
//}
