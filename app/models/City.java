package models;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import play.db.jpa.Model;
import play.templates.JavaExtensions;

@Entity
public class City extends Model {

    @OneToMany(mappedBy="city", cascade=CascadeType.ALL)
    public List<Way> ways;

    @Column(length=5, unique=true)
    public String inseeCode;

    @Column(length=5)
    public String postalCode;

    @Column(length=38)
    public String name;

    public static List<City> search(String search) {
        String cleanSearch = JavaExtensions.noAccents(search).toUpperCase();
        return City.find("postalCode = ? OR name LIKE ? order by name", cleanSearch, "%"+cleanSearch+"%").fetch(10);
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
