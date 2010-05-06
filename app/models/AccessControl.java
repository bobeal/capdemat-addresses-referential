package models;

import javax.persistence.Column;
import javax.persistence.Entity;

import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class AccessControl extends Model {

    @Required
    public String IP;

    @Column(unique = true)
    @Required
    @MinSize(16)
    public String token;

    @Required
    public String name;
    
}
