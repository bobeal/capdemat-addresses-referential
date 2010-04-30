package models;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import play.Logger;
import play.db.jpa.Model;
import play.modules.search.Field;
import play.modules.search.Indexed;
import play.modules.search.Search;
import play.templates.JavaExtensions;

@Entity
@Indexed
public class City extends Model {

    @Column(length=5, unique=true)
    public String inseeCode;

    @Column(length=5)
    @Field
    public String postalCode;

    @Column(length=38)
    @Field
    public String name;

    public static List<City> search(String search, Boolean postalCode) {
        String cleanSearch = JavaExtensions.noAccents(search).toUpperCase().replace("'", " ").trim();
        if(cleanSearch.length() <= 1) {
            return new ArrayList<City>();
        }
        if(postalCode) {
          return Search.search("postalCode:" + cleanSearch + " OR postalCode:" + cleanSearch + "*", City.class)
            .orderBy("postalCode")
            .page(0, 10)
            .fetch();
        }
        else {
          String luceneQuery = "name:\"" + cleanSearch + "\"";
          String wordsTokenized = "";
          for(String word : cleanSearch.split(" ")) {
              if(word.length() > 0) {
                  if(wordsTokenized.length() > 0) wordsTokenized += " AND ";
                  if(word.equals("SAINT")) {
                    wordsTokenized += "(name:ST OR name:SAINT)";
                  }
                  else {
                    wordsTokenized += "name:" + word;
                  }
              }
          }
          wordsTokenized += "*";
          if(wordsTokenized.length()>0) {
              luceneQuery += " OR (" + wordsTokenized + ")";
          }
          Logger.debug("%s", luceneQuery);
          return Search.search(luceneQuery, City.class).page(0, 10).fetch();
        }
    }

    public static String toJson(List<City> cities) {
        List<String> jsonCities = new ArrayList<String>();
        for(City city : cities) {
            jsonCities.add(city.toJson());
        }
        return jsonCities.toString();
    }

    public Map<String, Object> getJsonMap() {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("name", this.name);
        jsonMap.put("inseeCode", this.inseeCode);
        jsonMap.put("postalCode", this.postalCode);
        return jsonMap;
    }

    public String toJson() {
        return new Gson().toJson(this.getJsonMap());
    }

    @Override
    public String toString() {
        return this.name + " " + this.postalCode + " ( insee: " + this.inseeCode + " )";
    }

}
