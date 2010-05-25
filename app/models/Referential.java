package models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

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

    @Override
    public String toString() {
        return this.code + " ( " + this.name + " )";
    }

}
