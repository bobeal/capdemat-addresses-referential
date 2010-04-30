package models;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import play.db.jpa.Model;
import play.modules.search.Field;
import play.modules.search.Indexed;
import play.modules.search.Search;
import play.templates.JavaExtensions;
import play.Logger;
import java.util.Arrays;

@Entity
@Indexed
public class Way extends Model {

    @Column(length=5)
    @Field
    public String cityInseeCode;

    @Column(length=32)
    @Field
    public String name;

    @Column(length=8)
    public String matriculation;

    @Column(length=8, nullable=true)
    public String synonym = null;

    public static List<Way> search(String city, String search) {
        String cleanSearch = JavaExtensions.noAccents(search).toUpperCase().replace("'", " ").trim();
        if(cleanSearch.length() <= 1) {
            return new ArrayList<Way>();
        }
        String luceneQuery = "cityInseeCode:\"" + city + "\" AND (name:\"" + cleanSearch + "\"";
        List<String> clearnSearchTb = Arrays.asList(cleanSearch.split(" "));
        String wordsTokenized = "";
        for(String word : clearnSearchTb) {
            if(word.length() > 1) {
                if(wordsTokenized.length() > 0) wordsTokenized += " AND ";
                wordsTokenized += "name:" + word;
            }
        }
        wordsTokenized += "*";
        if(wordsTokenized.length() > 0) {
            if(clearnSearchTb.size() > 1) {
              luceneQuery += " OR (" + wordsTokenized + ")";
            }
            else {
              luceneQuery += " OR " + wordsTokenized;
            }
        }
        luceneQuery += ")";
        Logger.debug("%s", luceneQuery);
        return Search.search(luceneQuery, Way.class).page(0, 10).fetch();
    }

    public static String toJson(List<Way> cities) {
        List<String> jsonWays = new ArrayList<String>();
        for(Way way : cities) {
            jsonWays.add(way.toJson());
        }
        return jsonWays.toString();
    }

    public Map<String, Object> getJsonMap() {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("cityInseeCode", this.cityInseeCode);
        jsonMap.put("name", this.name);
        jsonMap.put("matriculation", this.matriculation);
        if(this.synonym != null) jsonMap.put("synonym", this.synonym);
        return jsonMap;
    }

    public String toJson() {
        return new Gson().toJson(this.getJsonMap());
    }

    @Override
    public String toString() {
        return this.name + " " + this.matriculation + " ( city insee: " + this.cityInseeCode + " )";
    }

}
