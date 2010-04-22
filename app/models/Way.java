package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import play.db.jpa.Model;

@Entity
public class Way extends Model {

    @ManyToOne
    public City city;

    public String name;
    public String matriculation;

    public String synonym = null;

}
