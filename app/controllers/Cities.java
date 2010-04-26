package controllers;

import java.util.List;
import models.City;
import play.mvc.Controller;

public class Cities extends Controller {

    public static void search(String search) {
        List<City> cities = City.search(search);
        renderJSON(City.toJson(cities));
    }

}
