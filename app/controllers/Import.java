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
        File mediapostFile = Play.getFile("data/hffvnn85.tri");
        BufferedReader  mediapostBr = new BufferedReader(new FileReader(mediapostFile));
        String line;
        int i = 0, ignoredCities = 0;
        City currentCity = null;
        while((line = mediapostBr.readLine()) != null) {

            // Voies
            if(currentCity != null && line.substring(0, 1).equals("V")) {
                Way way = new Way();
                way.cityInseeCode = currentCity.inseeCode;
                way.matriculation = line.substring(6, 14);
                way.name = line.substring(54, 86).trim();
                way.save();
            }

            // Voies synonymes (ancienne appellation ou appellation locale)
            else if(currentCity != null && line.substring(0, 1).equals("W")) {
                Way way = new Way();
                way.cityInseeCode = currentCity.inseeCode;
                way.synonym = line.substring(6, 14);
                way.matriculation = line.substring(14, 22);
                way.name = line.substring(54, 86).trim();
                way.save();
            }

            // Localité
            else if(line.substring(0, 1).equals("L")) {
                City.em().clear();
                String inseeCode = line.substring(1, 6);
                if(currentCity == null || currentCity != null && !inseeCode.equals(currentCity.inseeCode)) {
                    currentCity = new City();
                    currentCity.inseeCode = inseeCode;
                    currentCity.postalCode = line.substring(93, 98);
                    currentCity.name = line.substring(39, 71).trim();
                    currentCity.save();
                }
                else {
                    ignoredCities++;
                }
            }

            i++;
            if(i%1000==0) {
                Logger.info("%d processed rows...", i);
            }
        }
        Logger.info("%s ignored cities", ignoredCities);
        renderText("ok");
    }

}
