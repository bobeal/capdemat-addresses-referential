package controllers;

import java.util.List;
import models.City;
import play.mvc.Controller;

public class Cities extends Controller {

    public static void search(String callback, String search) {
        if(search == null) {
            response.status = 400;
            renderJSON("{message: \"The parameter 'search' is required.\"}");
        }
        List<City> cities = City.search(search);
        String jsonResult = City.toJson(cities);
        if(callback != null) {
          response.contentType = "text/javascript";
          renderText(callback + "(" + jsonResult + ")");
        }
        renderJSON(jsonResult);
    }

}
