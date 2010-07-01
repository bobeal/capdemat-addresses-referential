package models;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.gson.Gson;

import play.data.validation.Match;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.JPASupport;
import play.db.jpa.Model;

@Entity
public class Referential extends Model {

    @MinSize(3)
    @MaxSize(16)
    @Match("[A-Z0-9]+")
    @Column(length = 16)
    public String code;

    @Required
    public String name;

    public Referential(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Referential findByCode(String referentialCode) {
        return find("code = ?", referentialCode).first();
    }

    public Map<String, Object> toJsonMap() {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        if (this.id != null) jsonMap.put("id", this.id);
        jsonMap.put("code", this.code);
        jsonMap.put("name", this.name);
        return jsonMap;
    }

    public String toJson() {
        return new Gson().toJson(this.toJsonMap());
    }

    @Override
    public String toString() {
        return this.code + " ( " + this.name + " )";
    }


}
