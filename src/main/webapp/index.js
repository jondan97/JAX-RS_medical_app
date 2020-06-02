//method which handles the json that contains all the DailyValues, takes a json url and creates the table
//seen on the index.html file
function handleDailyValuesJson(jsonURL) {
    //read the json
    $.getJSON(jsonURL, function (data) {
        //for each DailyValues that is included within the DailyValues list
        $.each(data.dailyValuesList, function (index, dailyValues) {
            $("<tr class='dailyValue_row'>" +
                "<td> " +
                //input element to allow the user to edit the item
                "<input hidden type='number' id='updated_glucose_level_" + index + "' size='5' value='" + dailyValues.glucose_level + "'>" +
                //span element which shows the text that the user initially sees
                "<span id='glucose_level_text_" + index + "'>" + dailyValues.glucose_level + " mg/dL</span>" +
                "</td>" +
                "<td> " +
                "<input hidden type='number' id='updated_carb_intake_" + index + "' size='5' value='" + dailyValues.carb_intake + "'>" +
                "<span id='carb_intake_text_" + index + "'>" + dailyValues.carb_intake + " gr</span>" +
                "</td>" +
                "<td> " +
                "<input hidden type='number' id='updated_medication_dose_" + index + "' size='5' value='" + dailyValues.medication_dose + "'>" +
                "<span id='medication_dose_text_" + index + "'>" + dailyValues.medication_dose + " pill(s)</span>" +
                "</td>" +
                "<td>" + dailyValues.input_date.substring(0, 10) + "</td>" +
                "<td>" +
                //button that the user initially sees
                "<input onclick='editRow(" + index + ")' id='edit_button_" + index + "' type='button' value='Edit'>" +
                //the two buttons that the user sees after they click on the 'edit' button
                "<input hidden type='button' id='update_button_" + index + "' value='Update' onclick='updateRow(" + index + ")'>" +
                "<input hidden type='button' id='delete_button_" + index + "' value='Delete' onclick='deleteRow(" + index + ")'>" +
                //hidden id to distinguish between DailyValues items
                "<input hidden type='number' id='updated_dailyValues_id_" + index + "' value='" + dailyValues.id + "'>" +
                "</td>" +
                "</tr>")
                .insertAfter("#tableMainHeaders");
        });
        //append the averages at the bottom of the table
        let averagesRow = $("#averagesRow");
        averagesRow.append("<td class='average_row' colspan='2'>" + data.average_glucose_level.toFixed(2) + " mg/dL</td>");
        averagesRow.append("<td class='average_row' colspan='2'>" + data.average_carb_intake.toFixed(2) + " gr</td>");

        //image represented as encoded base64 string
        var base64_string = data.imageAsBase64String;
        //create the data url
        var image = "data:image/png;base64," + base64_string;
        //set the data url
        $("#chartAsImage").attr("src", image);
    })
}

//method which allows the rows of the main table to be edited, turns the text into input fields
function editRow(index) {
    //remove all the text first
    $("#glucose_level_text_" + index).remove();
    $("#carb_intake_text_" + index).remove();
    $("#medication_dose_text_" + index).remove();
    $("#edit_button_" + index).remove();
    //add the input fields and the buttons
    $("#updated_glucose_level_" + index).removeAttr('hidden');
    $("#updated_carb_intake_" + index).removeAttr('hidden');
    $("#updated_medication_dose_" + index).removeAttr('hidden');
    $("#update_button_" + index).removeAttr('hidden');
    $("#delete_button_" + index).removeAttr('hidden');
}

//method which gives the signal to the back-end that the user requested an update of a row
function updateRow(index) {
    prepareForm(index);
    let form = $("#updateOrSaveDailyValuesForm");
    form.attr('action', '/medical/api/dailyValues/save');
    form.submit();
}

//method which gives the signal to the back-end that the user requested a deletion of a row
function deleteRow(index) {
    prepareForm(index);
    let form = $("#updateOrSaveDailyValuesForm");
    form.attr('action', '/medical/api/dailyValues/delete');
    form.submit();
}

//prepares the 'hidden' form before the submission, adds the values to the form from the table row that was selected
function prepareForm(index) {
    $("#updated_glucose_level").val($("#updated_glucose_level_" + index).val());
    $("#updated_carb_intake").val($("#updated_carb_intake_" + index).val());
    $("#updated_medication_dose").val($("#updated_medication_dose_" + index).val());
    $("#updated_dailyValues_id").val($("#updated_dailyValues_id_" + index).val());
}

//removes all the DailyValue rows and their averages from the table and initiates a recalculation within the date range
//requested by the user
function defineDateRange() {
    //remove DailyValues rows, average rows and the image
    $(".dailyValue_row").remove();
    $(".average_row").remove();
    $("#chartAsImage").removeAttr("src");
    let dateFrom = $("#dateFrom").val();
    let dateTo = $("#dateTo").val();
    let jsonURL = "/medical/api/dailyValues/range?from=" + dateFrom + "&to=" + dateTo;
    handleDailyValuesJson(jsonURL);
}