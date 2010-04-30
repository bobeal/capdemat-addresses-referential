package controllers;

import java.util.List;
import models.City;
import play.mvc.Controller;
import java.util.Date;
import play.Logger;

public class Cities extends Controller {

    public static void search(String callback, String search) {
        if(search == null) {
            response.status = 400;
            renderJSON("{message: \"The parameter 'search' is required.\"}");
        }
        Date start = new Date();
        List<City> cities = City.search(search);
        Logger.debug("%s ms", new Date().getTime() - start.getTime());
        String jsonResult = City.toJson(cities);
        if(callback != null) {
          response.contentType = "text/javascript";
          renderText(callback + "(" + jsonResult + ")");
        }
        renderJSON(jsonResult);
    }

}
