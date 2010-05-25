package controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.City;
import models.Referential;

import au.com.bytecode.opencsv.CSVWriter;
import play.Play;
import play.libs.Codec;
import play.mvc.Controller;

public class Export extends Controller {

	public static void citiesCSV(String referentialCode) throws IOException {
		Referential referential = Referential.find("code=?", referentialCode).first();
		File file = Play.getFile("data/tmp/"+Codec.UUID());
		if(!file.exists()) file.createNewFile();
		CSVWriter writer = new CSVWriter(new FileWriter(file));
		List<City> cities = City.find("referential=?",referential).fetch();
		for(City city : cities) {
			List<String> row = new ArrayList<String>();
			row.add(city.inseeCode);
			row.add(city.name);
			row.add(city.postalCode);
		}
	}

}
