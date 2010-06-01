package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class ExpirationNotification extends Model {

    @ManyToOne
    public AccessControl accessControl;

    public Date expirationDate;
    public String recipientMail;
    public Date mailingDate;
    public Integer step;

    public ExpirationNotification(Integer step, AccessControl accessControl) {
        this.accessControl = accessControl;
        this.step = step;
        this.expirationDate = accessControl.expirationDate;
        this.recipientMail = accessControl.customer.email;
        this.mailingDate = new Date();
    }

    public static boolean exists(ExpirationNotification notification) {
        return find("accessControl = ? and step = ? and expirationDate = ?", notification.accessControl, notification.step, notification.expirationDate).first() != null;
    }

    public static void renewal() {
        // TODO v2
    }

}
