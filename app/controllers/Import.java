package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import models.Way;
import models.City;
import play.Logger;
import play.Play;
import play.mvc.Controller;

public class Import extends Controller {

    public static void mediapost() throws FileNotFoundException, IOException {
        City.deleteAll();
        Way.deleteAll();

        File mediapostFile = Play.getFile("data/hffvnn85.tri");
        BufferedReader  mediapostBr = new BufferedReader(new FileReader(mediapostFile));
        String line;
        int i = 0;
        while((line = mediapostBr.readLine()) != null) {
            City currentCity = null;

            // Voies
            if(line.substring(0, 1).equals("V")) {
                Way way = new Way();
                way.city = currentCity;
                way.matriculation = line.substring(7, 15);
                way.name = line.substring(54, 86).trim();
                way.save();
            }

            // Voies synonymes (ancienne appellation ou appellation locale)
            else if(line.substring(0, 1).equals("W")) {
                Way way = new Way();
                way.city = currentCity;
                way.synonym = line.substring(6,15);
                way.matriculation = line.substring(15, 22);
                way.name = line.substring(54, 86).trim();
                way.save();
            }

            // Localité
            else if(line.substring(0, 1).equals("L")) {
                City.em().clear();
                currentCity = new City();
                currentCity.inseeCode = line.substring(1, 6);
                currentCity.postalCode = line.substring(93, 99);
                currentCity.name = line.substring(39, 71).trim();
                currentCity.save();
            }

            i++;
            if(i%1000==0) {
                Logger.info("%d processed rows...", i);
            }
        }
        renderText("ok");
    }

}
