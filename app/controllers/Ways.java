package controllers;

import java.util.Date;
import java.util.List;
import models.Way;
import play.mvc.*;

public class Ways extends Controller {

    public static void search(String city, String search) {
        notFoundIfNull(city);
        notFoundIfNull(search);
        List<Way> cities = Way.search(city, search);
        renderJSON(Way.toJson(cities));
    }

}
