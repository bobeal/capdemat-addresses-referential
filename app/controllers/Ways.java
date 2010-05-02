package controllers;

import java.util.List;
import models.Way;
import play.mvc.Controller;
import java.util.Date;
import play.Logger;

public class Ways extends Controller {

    public static void search(String callback, String city, String search) {
        if (city == null || search == null) {
            response.status = 400;
            renderJSON("{message: \"Parameters 'city' and 'search' are required.\"}");
        }
        Date start = new Date();
        List<Way> ways = Way.search(city, search);
        Logger.debug("%s ms", new Date().getTime() - start.getTime());
        String jsonResult = Way.toJson(ways);
        if (callback != null) {
            response.contentType = "text/javascript";
            renderText(callback + "(" + jsonResult + ")");
        }
        renderJSON(jsonResult);
    }
}
