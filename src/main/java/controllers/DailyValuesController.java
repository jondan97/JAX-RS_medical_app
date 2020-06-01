package controllers;

import dto.DailyValuesBetweenRangeDTO;
import model.DailyValues;
import service.DailyValueService;
import util.Time;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;

/**
 * class that handles everything that has to do with the daily value requests and responses in the system, mostly CRUD operations
 */
@Path("/dailyValues")
public class DailyValuesController {
    @Inject
    private DailyValueService dailyValueService;

    /**
     * this method creates a json which include the dto with all the data requested in the coursework, all data were
     * included in the same method for efficiency reasons (reduce database reads)
     *
     * @param startDateStr : the starting (older) date that the user has requested to the daily values from
     * @param endDateStr   : the end date (more recent) date that the user has requested
     * @return : returns a data transfer object with the averages glucose level, carb intake and a list with all the values in included in that date range
     */
    @Path("/range")
    @GET
    @Produces("application/json")
    public DailyValuesBetweenRangeDTO findAllDailyValuesBetween(@QueryParam("from") String startDateStr,
                                                                @QueryParam("to") String endDateStr) {
        Date[] dateRange = Time.handleDates(startDateStr, endDateStr);
        Date fromDate = dateRange[0];
        Date toDate = dateRange[1];
        return dailyValueService.fetchAndCalculateDailyValuesBetweenRange(fromDate, toDate);
    }

    /**
     * method which saves or updates an existing item on the database, if the id is 0, then a new item will be created,
     * if id is not zero, an item will be saved unless an id that does not exist is requested, which in this case a new
     * record will be created
     *
     * @return : String that will be returned as a text/plain response: 'daily value saved'
     */
    @Path("save/{id}/{glucose_level}/{carb_intake}/{medication_dose}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String saveDailyValue(@PathParam("id") int id,
                                 @PathParam("glucose_level") final int glucose_level,
                                 @PathParam("carb_intake") final int carb_intake,
                                 @PathParam("medication_dose") final int medication_dose) {
        DailyValues dailyValues = new DailyValues();
        //this means: 'create new object'
        if (id != 0) {
            dailyValues.setId(id);
        }
        dailyValues.setGlucose_level(glucose_level);
        dailyValues.setCarb_intake(carb_intake);
        dailyValues.setMedication_dose(medication_dose);
        dailyValues.setInput_date(new Date());
        dailyValueService.saveDailyValue(dailyValues);
        return "Daily Value Saved!";
    }

    /**
     * method that deletes an item from the database, takes an ID as request and if it exists on the database, it will
     * be deleted
     *
     * @return : String that will be returned as a text/plain response: 'daily value deleted'
     */
    @Path("delete/{id}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteDailyValue(@PathParam("id") final int id) {
        DailyValues dailyValues = new DailyValues();
        dailyValues.setId(id);
        dailyValueService.deleteDailyValue(dailyValues);
        return "Daily Value Deleted!";
    }
}
