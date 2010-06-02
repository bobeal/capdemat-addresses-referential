package controllers.admin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.PersistenceException;

import org.apache.commons.io.FileUtils;

import exceptions.BadCSVLineFormatException;

import au.com.bytecode.opencsv.CSVReader;
import models.Import;
import models.Referential;
import models.Way;
import models.City;
import models.Import.Type;
import play.Logger;
import play.Play;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.libs.Codec;
import play.mvc.Controller;

public class Imports extends Admin {

    public static void index() {
        List<Import> imports = Import.find("order by depositDate desc").fetch();
        List<Referential> referentials = Referential.find("order by name").fetch();
        render(imports, referentials);
    }

    public static void logs(Long importId) {
        Import currentImport = Import.findById(importId);
        notFoundIfNull(currentImport);
        render(currentImport);
    }

    public static void upload(@Required Long referentialId, @Required Import.Type importType, @Required File csvFile) throws IOException, NoSuchAlgorithmException {
        Referential referential = null;
        if(!Validation.hasErrors()) {
            referential = Referential.findById(referentialId);
            if(referential == null) {
                Validation.addError("referentialId", "validation.required");
            }
            if(Import.fileExists(csvFile)) {
                Validation.addError("csvFile", "validation.fileExists");
            }
        }
        if(Validation.hasErrors()) {
            params.flash();
            Validation.keep();
            Imports.index();
        }
        Import currentImport = Import.queue(referential, csvFile, importType);
        if(currentImport == null) {
            flash.put("error", "import.nok");
            Imports.index();
        }
        flash.put("success", "import.ok");
        Imports.index();
    }

    public static void mediapost(Long limit) throws FileNotFoundException, IOException {
        File mediapostFile = Play.getFile("data/hffvnn85.tri");
        Referential referential = Referential.find("code=?","HEXAVIA").first();
        if(referential == null) referential = new Referential("HEXAVIA","Hexavia").save();
        BufferedReader mediapostBr = new BufferedReader(new FileReader(mediapostFile));
        String line;
        int i = 0, ignoredCities = 0;
        City currentCity = null;
        while ((line = mediapostBr.readLine()) != null) {

            // Voies
            if (currentCity != null && line.substring(0, 1).equals("V")) {
                Way way = new Way();
                way.referential = referential;
                way.cityInseeCode = currentCity.inseeCode;
                way.matriculation = line.substring(6, 14);
                way.name = line.substring(54, 86).trim();
                way.save();
            }

            // Voies synonymes (ancienne appellation ou appellation locale)
            else if (currentCity != null && line.substring(0, 1).equals("W")) {
                Way way = new Way();
                way.referential = referential;
                way.cityInseeCode = currentCity.inseeCode;
                way.synonym = line.substring(6, 14);
                way.matriculation = line.substring(14, 22);
                way.name = line.substring(54, 86).trim();
                way.save();
            }

            // Localité
            else if (line.substring(0, 1).equals("L")) {
                City.em().clear();
                String inseeCode = line.substring(1, 6);
                if (currentCity == null || currentCity != null && !inseeCode.equals(currentCity.inseeCode)) {
                    currentCity = new City();
                    currentCity.referential = referential;
                    currentCity.inseeCode = inseeCode;
                    currentCity.postalCode = line.substring(93, 98);
                    currentCity.name = line.substring(39, 71).trim();
                    currentCity.save();
                } else {
                    ignoredCities++;
                }
            }

            i++;
            if (i % 1000 == 0) {
                Logger.info("%d processed rows...", i);
            }
            if (limit != null && i > limit) {
                break;
            }
        }
        Logger.info("%s ignored cities", ignoredCities);
        renderText("ok");
    }
}
