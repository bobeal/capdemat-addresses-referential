package controllers;

import java.util.List;
import models.City;
import play.mvc.Controller;
import java.util.Date;
import play.Logger;

public class Cities extends Services {

    public static void search(String callback, String search, Boolean postalCode) {
        if (search == null) {
            response.status = 400;
            renderJSON("{message: \"The parameter 'search' is required.\"}");
        }
        Date start = new Date();
        String referentialCode = getAccessControl().referential.code;
        List<City> cities = City.search(referentialCode, search, postalCode != null ? postalCode : false);
        Logger.debug("%s ms", new Date().getTime() - start.getTime());
        String jsonResult = City.toJson(cities);
        if (callback != null) {
            response.contentType = "text/javascript";
            renderText(callback + "(" + jsonResult + ")");
        }
        renderJSON(jsonResult);
    }
}
