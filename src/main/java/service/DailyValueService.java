package service;

import dto.DailyValuesBetweenRangeDTO;
import model.DailyValues;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jfree.chart.JFreeChart;
import util.HibernateUtil;
import util.LineChartGenerator;

import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * main service that handles the database/transaction operations for the daily values entinty
 */
@Stateless
public class DailyValueService {

    /**
     * this method either creates a new record in the database or updates an existing one, depending on the id and if it
     * existing in the repository
     *
     * @param dailyValues : the dailyValues entity that the user requested to save in the repository
     */
    public void saveDailyValue(DailyValues dailyValues) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            // save the dailyValues object
            session.saveOrUpdate(dailyValues);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * this method deletes a row from the database, if it exists
     *
     * @param dailyValues : daily value entity that was requested to be deleted
     */
    public void deleteDailyValue(DailyValues dailyValues) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            // save the dailyValues object
            session.delete(dailyValues);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * this method fetches all the results from the daily values table, between a specific range, and also calculates
     * the average glucose level and carb intake fields, they are all done in a single method to achieve efficiency
     * instead of having to fetch the same results 3 times (1 time the original, 1 time to calculate the average glucose
     * etc.)
     *
     * @param fromDate : starting date
     * @param toDate   : finishing date
     * @return : returns a data transfer object for the controller to respond with
     */
    public DailyValuesBetweenRangeDTO fetchAndCalculateDailyValuesBetweenRange(Date fromDate, Date toDate) throws IOException {
        DailyValuesBetweenRangeDTO dailyValuesBetweenRangeDTO = new DailyValuesBetweenRangeDTO();
        List<DailyValues> selectedDailyValuesList = findAllDailyValuesBetweenRange(fromDate, toDate);
        //one-liner to calculate the average
        //'orElse' used in case something goes wrong and average cannot be calculated
        double average_glucose_level =
                selectedDailyValuesList.stream().mapToInt(DailyValues::getGlucose_level).average().orElse(0);
        double average_carb_intake =
                selectedDailyValuesList.stream().mapToInt(DailyValues::getCarb_intake).average().orElse(0);
        String imageAsString = generateImageToString(selectedDailyValuesList);

        dailyValuesBetweenRangeDTO.setDailyValuesList(selectedDailyValuesList);
        dailyValuesBetweenRangeDTO.setAverage_glucose_level(average_glucose_level);
        dailyValuesBetweenRangeDTO.setAverage_carb_intake(average_carb_intake);
        dailyValuesBetweenRangeDTO.setImageAsBase64String(imageAsString);

        return dailyValuesBetweenRangeDTO;
    }

    /**
     * private method that is used to fetch all the daily values between two ranges, if the range does not exist or
     * one of the two dates is null, then it fetches all the records included in the repository
     *
     * @param fromDate : starting date
     * @param toDate   : finishing date
     * @return : returns a list with all the daily values between a range
     */
    private List<DailyValues> findAllDailyValuesBetweenRange(Date fromDate, Date toDate) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        //if one of the dates is null, then fetch all (default choice as requested in the coursework)
        if (fromDate == null || toDate == null) {
            return session.createQuery("SELECT a FROM DailyValues a ORDER BY input_date", DailyValues.class).getResultList();
        } else {
            //fetch results between two dates
            return session.createQuery("FROM DailyValues AS dv WHERE dv.input_date BETWEEN :fromDate AND :toDate ORDER BY input_date")
                    .setParameter("fromDate", fromDate)
                    .setParameter("toDate", toDate)
                    .getResultList();
        }
    }

    /**
     * this method takes the list of the selected values, generates a line chart through the JFreeChart library, turns
     * it into an image, then encodes it with the base64 encoder and returns it as a string
     *
     * @param selectedDailyValuesList : the list of the daily values elements to create the chart from
     * @return : returns the image as a string, encoded in base64 format
     */
    private String generateImageToString(List<DailyValues> selectedDailyValuesList) throws IOException {
        JFreeChart generatedChart = LineChartGenerator.generateChart(selectedDailyValuesList);
        BufferedImage image = generatedChart.createBufferedImage(640, 480);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}
