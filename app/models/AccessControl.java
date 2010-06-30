package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class AccessControl extends Model {

    @ManyToOne
    public Customer customer;

    @Required
    public String IP;

    @Column(unique = true)
    @Required
    @MinSize(16)
    public String token;

    @Required
    public String domainName;

    public Date expirationDate;

    @Required
    @ManyToOne
    public Referential referential;

    @Override
    public String toString() {
        return this.domainName + " ( token:" + this.token + ", IP:" + this.IP + " )";
    }

}
