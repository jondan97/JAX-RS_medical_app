package model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * main entity in the system, which is also 'mapped' in the database
 * all the attributes requested in the coursework have all been collected in one entity, based on the developer's opinion
 */
@Entity
@Table(name = "daily_values")
public class DailyValues {
    private int id;
    private int glucose_level;
    private int carb_intake;
    private double medication_dose;
    private Date input_date;

    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column
    public int getId() {
        return id;
    }

    public void setId(int email) {
        this.id = email;
    }

    @Column
    public int getGlucose_level() {
        return glucose_level;
    }

    public void setGlucose_level(int glucoseLevel) {
        this.glucose_level = glucoseLevel;
    }

    @Column
    public int getCarb_intake() {
        return carb_intake;
    }

    public void setCarb_intake(int carbIntake) {
        this.carb_intake = carbIntake;
    }

    @Column
    public double getMedication_dose() {
        return medication_dose;
    }

    public void setMedication_dose(double medicationDose) {
        this.medication_dose = medicationDose;
    }

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    public Date getInput_date() {
        return input_date;
    }

    public void setInput_date(Date inputDate) {
        this.input_date = inputDate;
    }
}
