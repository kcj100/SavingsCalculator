package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class UserInterface {

    private BorderPane layout;
    private LineChart<Number, Number> lineChart;
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private VBox vbox;
    private BorderPane monthlyPane;
    private BorderPane interestPane;
    private double currentMonthlyValue;
    private double currentInterestRt;

    public UserInterface() {
        layout = new BorderPane();
        xAxis = new NumberAxis();
        yAxis = new NumberAxis();
        lineChart = new LineChart(xAxis, yAxis);
        vbox = new VBox();
        monthlyPane = new BorderPane();
        interestPane = new BorderPane();
        this.currentMonthlyValue = 50;
        this.currentInterestRt = 1.0;
    }

    public void start(BorderPane pane) {
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        Label monthlySavings = new Label("Monthly savings");
        Slider monthlySlider = new Slider();
        monthlySlider.setMin(25);
        monthlySlider.setMax(250);
        monthlySlider.setValue(50);
        monthlySlider.setMaxWidth(Double.MAX_VALUE);
        monthlySlider.setSnapToTicks(true);
        monthlySlider.setBlockIncrement(500);
        monthlySlider.setShowTickMarks(true);
        monthlySlider.setShowTickLabels(true);
        Label value1 = new Label(Double.toString(monthlySlider.getValue()));
        monthlySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            value1.setText(String.format("%.1f", newValue.doubleValue()));
            this.currentMonthlyValue = newValue.doubleValue();
            updateLineChart(lineChart);
        });
        monthlyPane.setLeft(monthlySavings);
        monthlyPane.setCenter(monthlySlider);
        monthlyPane.setRight(value1);
        VBox.setVgrow(monthlyPane, Priority.ALWAYS);

        Label yearlyInterest = new Label("Yearly interest rate");
        Slider interestSlider = new Slider();
        interestSlider.setMin(0);
        interestSlider.setMax(10);
        interestSlider.setValue(1.0);
        interestSlider.setPrefWidth(Double.MAX_VALUE);
        interestSlider.setMajorTickUnit(0.1);
        interestSlider.setMinorTickCount(10);
        interestSlider.setShowTickMarks(true);
        interestSlider.setShowTickLabels(true);
        Label value2 = new Label(Double.toString(interestSlider.getValue()));
        updateLineChart(lineChart);
        interestSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            value2.setText(String.format("%.2f", newValue.doubleValue()));
            this.currentInterestRt = newValue.doubleValue();
            updateLineChart(lineChart);
        });
        interestPane.setLeft(yearlyInterest);
        interestPane.setCenter(interestSlider);
        interestPane.setRight(value2);
        VBox.setVgrow(interestPane, Priority.ALWAYS);
        vbox.getChildren().addAll(monthlyPane, interestPane);
        vbox.setFillWidth(true);
        pane.setTop(vbox);
        addLineChart(pane);
    }

    private void addLineChart(BorderPane pane) {
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(30);
        xAxis.setAutoRanging(false);
        pane.setCenter(lineChart);
    }

    private XYChart.Series setLineChartMI() {
        XYChart.Series data = new XYChart.Series<>();
        for (int i = 0; i <= 30; i++) {
            data.getData().add(new XYChart.Data<>(i, calculateTotalSavings(currentMonthlyValue, currentInterestRt, i)));
        }
        return data;
    }

    private XYChart.Series setLineChartM() {
        XYChart.Series data = new XYChart.Series<>();
        for (int i = 0; i <= 30; i++) {
            data.getData().add(new XYChart.Data<>(i, calculateTotalSavings(currentMonthlyValue) * i));
        }
        return data;
    }

    private double calculateTotalSavings(double monthlySavings, double interestRate, int years) {
    double total = 0;
    double annualInterestRate = 1 + (interestRate / 100);
    double yearlySavings = monthlySavings * 12;
    double yearlyInterest = yearlySavings * (annualInterestRate - 1);
    for (int i = 1; i <= years; i++) {
        total += yearlySavings;
        total += yearlyInterest;
        yearlySavings *= annualInterestRate;
        yearlyInterest = yearlySavings * (annualInterestRate - 1);
    }
    return total;
}

    private double calculateTotalSavings(double monthlySavings) {
        return monthlySavings * 12;
    }

    private void updateLineChart(LineChart chart) {
        
        lineChart.getData().clear();
        if (currentInterestRt == 0) {
            yAxis.setUpperBound(calculateTotalSavings(currentMonthlyValue));
            lineChart.getData().add(setLineChartM());
        } else {
            yAxis.setUpperBound(calculateTotalSavings(currentMonthlyValue, currentInterestRt, 30));
            lineChart.getData().add(setLineChartMI());
            lineChart.getData().add(setLineChartM());
        }

    }

}
