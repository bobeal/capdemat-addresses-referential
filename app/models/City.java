package models;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import play.Logger;
import play.db.jpa.Model;
import play.modules.search.Field;
import play.modules.search.Indexed;
import play.modules.search.Search;
import play.templates.JavaExtensions;

@Entity
@Indexed
public class City extends Model {

    @ManyToOne
    @Field(joinField = "code")
    public Referential referential;

    @Column(length = 5, unique = true)
    public String inseeCode;

    @Column(length = 5)
    @Field
    public String postalCode;

    @Column(length = 38)
    @Field
    public String name;

    public static List<City> search(String search, Boolean postalCode) {
        String cleanSearch = JavaExtensions.noAccents(search).toUpperCase().replace("'", " ").trim();
        if (cleanSearch.length() < 1) {
            return new ArrayList<City>();
        }
        if (postalCode) {
            return Search.search("postalCode:" + cleanSearch + " OR postalCode:" + cleanSearch + "*", City.class).orderBy("postalCode").page(0, 10).fetch();
        } else {
            String luceneQuery = "name:\"" + cleanSearch + "\"";
            if (cleanSearch.length() > 0) {
                String wordsTokenized = "";
                Boolean wildcardable = false;
                for (String word : cleanSearch.split(" ")) {
                    if (word.length() > 0) {
                        if (wordsTokenized.length() > 0) {
                            wordsTokenized += " AND ";
                        }
                        if (word.equals("SAINT")) {
                            wordsTokenized += "(name:ST OR name:SAINT)";
                            wildcardable = false;
                        } else if (word.equals("SAINTE")) {
                            wordsTokenized += "(name:STE OR name:SAINTE)";
                            wildcardable = false;
                        } else {
                            wordsTokenized += "name:" + word;
                            wildcardable = true;
                        }
                    }
                }
                if (wildcardable) {
                    wordsTokenized += "*";
                }
                if (wordsTokenized.length() > 0) {
                    luceneQuery += " OR (" + wordsTokenized + ")";
                }
            }
            Logger.debug("%s", luceneQuery);
            List<Long> cityIds = Search.search(luceneQuery, City.class).orderBy("name").page(0, 10).fetchIds();
            List<City> cities = new ArrayList<City>();
            if(cityIds.size() > 0) {
                cities = City.find("id in (?1) order by name", cityIds).fetch();
            }
            return cities;
        }
    }

    public static String toJson(List<City> cities) {
        List<String> jsonCities = new ArrayList<String>();
        for (City city : cities) {
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
