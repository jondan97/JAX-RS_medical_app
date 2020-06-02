package gr.university.dscc.controllers;

import gr.university.dscc.dto.DailyValuesBetweenRangeDTO;
import gr.university.dscc.model.DailyValues;
import gr.university.dscc.service.DailyValueService;
import gr.university.dscc.util.Time;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Date;

/**
 * class that handles everything that has to do with the daily value requests and responses in the system, mostly CRUD operations
 */
@Path("/dailyValues")
public class DailyValuesController {
    @Inject
    private DailyValueService dailyValueService;

    @Context
    private HttpServletResponse response;

    /**
     * this method creates a json which include the gr.university.dscc.dto with all the data requested in the coursework, all data were
     * included in the same method for efficiency reasons (reduce database reads)
     *
     * @param fromDateStr : the starting (older) date that the user has requested to the daily values from
     * @param toDateStr   : the end date (more recent) date that the user has requested
     * @return : returns a data transfer object with the averages glucose level, carb intake and a list with all the values in included in that date range
     */
    @Path("/range")
    @GET
    @Produces("application/json")
    //@RolesAllowed("PHYSICIAN")
    public DailyValuesBetweenRangeDTO findAllDailyValuesBetween(@QueryParam("from") String fromDateStr,
                                                                @QueryParam("to") String toDateStr) throws IOException {
        Date[] dateRange = Time.handleDates(fromDateStr, toDateStr);
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
    @Path("save")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    //@RolesAllowed("ADMIN")
    public void saveDailyValue(@FormParam("id") final int id,
                               @FormParam("glucose_level") int glucose_level,
                               @FormParam("carb_intake") int carb_intake,
                               @FormParam("medication_dose") int medication_dose) throws IOException {
        DailyValues dailyValues = new DailyValues();
        //this means: 'create new object'
        if (id != 0) {
            dailyValues.setId(id);
        }
        //we don't want negative numbers
        if (glucose_level < 0)
            glucose_level = 0;
        if (carb_intake < 0)
            carb_intake = 0;
        if (medication_dose < 0)
            medication_dose = 0;

        dailyValues.setGlucose_level(glucose_level);
        dailyValues.setCarb_intake(carb_intake);
        dailyValues.setMedication_dose(medication_dose);
        //dailyValues.setInput_date(new Date());

        dailyValueService.saveDailyValue(dailyValues);
        response.sendRedirect("/medical");
    }

    /**
     * method that deletes an item from the database, takes an ID as request and if it exists on the database, it will
     * be deleted
     */
    @Path("delete")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    //@RolesAllowed("ADMIN")
    public void deleteDailyValue(@FormParam("id") final int id) throws IOException {
        DailyValues dailyValues = new DailyValues();
        dailyValues.setId(id);
        dailyValueService.deleteDailyValue(dailyValues);
        response.sendRedirect("/medical");
    }
}
