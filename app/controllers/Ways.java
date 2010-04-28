package controllers;

import java.util.List;
import models.Way;
import play.mvc.Controller;

public class Ways extends Controller {

    public static void search(String callback, String city, String search) {
        if(city == null || search == null) {
            response.status = 400;
            renderJSON("{message: \"Parameters 'city' and 'search' are required.\"}");
        }
        List<Way> ways = Way.search(city, search);
        String jsonResult = Way.toJson(ways);
        if(callback != null) {
          response.contentType = "text/javascript";
          renderText(callback + "(" + jsonResult + ")");
        }
        renderJSON(jsonResult);
    }

}
