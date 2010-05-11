package models;

import javax.persistence.Entity;

import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Customer extends Model {

    @Required
    public String name;

    @Required
    @Email
    public String email;

    @Override
    public String toString() {
        return this.name + " ( " + this.email + " ) ";
    }

}
