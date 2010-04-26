package models;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import play.db.jpa.Model;
import play.templates.JavaExtensions;

@Entity
public class Way extends Model {

    @ManyToOne(optional=false)
    public City city;

    @Column(length=32)
    public String name;

    @Column(length=8)
    public String matriculation;

    @Column(length=8, nullable=true)
    public String synonym = null;

    public static List<Way> search(String city, String search) {
        String cleanSearch = JavaExtensions.noAccents(search).toUpperCase();
        return Way.find("city.inseeCode = ? AND name LIKE ? order by name", city, "%"+cleanSearch+"%").fetch(20);
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
        jsonMap.put("name", this.name);
        jsonMap.put("matriculation", this.matriculation);
        if(this.synonym != null) jsonMap.put("synonum", this.synonym);
        return jsonMap;
    }

    public String toJson() {
        Map<String, Object> jsonMap = this.getJsonMap();
        jsonMap.put("city", this.city.getJsonMap());
        return new Gson().toJson(jsonMap);
    }

    @Override
    public String toString() {
        return this.city.name + " : " + this.name + " " + this.matriculation;
    }

}
