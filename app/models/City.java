package models;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import play.db.jpa.Model;

@Entity
public class City extends Model {

    @OneToMany(mappedBy="city", cascade=CascadeType.REMOVE)
    public List<Way> ways;

    public String inseeCode;
    public String postalCode;
    public String name;

    @Override
    public String toString() {
        return this.name + " " + this.postalCode + " ( insee: " + this.inseeCode + " )";
    }


}
