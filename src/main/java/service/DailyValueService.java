package service;

import dto.DailyValuesBetweenRangeDTO;
import model.DailyValues;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import javax.ejb.Stateless;
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
    public DailyValuesBetweenRangeDTO fetchAndCalculateDailyValuesBetweenRange(Date fromDate, Date toDate) {
        DailyValuesBetweenRangeDTO dailyValuesBetweenRangeDTO = new DailyValuesBetweenRangeDTO();
        List<DailyValues> selectedDailyValues = findAllDailyValuesBetweenRange(fromDate, toDate);
        //one-liner to calculate the average
        double average_glucose_level =
                selectedDailyValues.stream().mapToInt(DailyValues::getGlucose_level).average().orElse(Integer.MIN_VALUE);
        //'orElse' used in case something goes wrong and average cannot be calculated
        double average_carb_intake =
                selectedDailyValues.stream().mapToInt(DailyValues::getCarb_intake).average().orElse(Integer.MIN_VALUE);
        dailyValuesBetweenRangeDTO.setDailyValuesList(selectedDailyValues);
        dailyValuesBetweenRangeDTO.setAverage_glucose_level(average_glucose_level);
        dailyValuesBetweenRangeDTO.setAverage_carb_intake(average_carb_intake);
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
            return session.createQuery("SELECT a FROM DailyValues a", DailyValues.class).getResultList();
        } else {
            //fetch results between two dates
            return session.createQuery("FROM DailyValues AS dv WHERE dv.input_date BETWEEN :fromDate AND :toDate")
                    .setParameter("fromDate", fromDate)
                    .setParameter("toDate", toDate)
                    .getResultList();
        }
    }
}
