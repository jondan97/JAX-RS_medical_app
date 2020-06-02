package gr.university.dscc.dto;

import gr.university.dscc.model.DailyValues;

import java.util.List;

/**
 * data transfer object, used as an efficient 'grouping' in order to get together all the necessary values requested in
 * the coursework
 */
public class DailyValuesBetweenRangeDTO {
    private double average_glucose_level;
    private double average_carb_intake;
    private List<DailyValues> dailyValuesList;
    private String imageAsBase64String;

    public void setAverage_glucose_level(double average_glucose_level) {
        this.average_glucose_level = average_glucose_level;
    }

    public void setAverage_carb_intake(double average_carb_intake) {
        this.average_carb_intake = average_carb_intake;
    }

    public void setDailyValuesList(List<DailyValues> dailyValuesList) {
        this.dailyValuesList = dailyValuesList;
    }

    public String getImageAsBase64String() {
        return imageAsBase64String;
    }

    public double getAverage_glucose_level() {
        return average_glucose_level;
    }

    public double getAverage_carb_intake() {
        return average_carb_intake;
    }

    public void setImageAsBase64String(String imageAsBase64String) {
        this.imageAsBase64String = imageAsBase64String;
    }

    public List<DailyValues> getDailyValuesList() {
        return dailyValuesList;
    }
}
