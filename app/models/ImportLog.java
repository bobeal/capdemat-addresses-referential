package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class ImportLog extends Model {

	@ManyToOne
	public Import importEntity;

	@Enumerated(EnumType.STRING)
	public Error error;

	public String message;

	public Date date = new Date();

	public ImportLog(Import currentImport, Error error, String message) {
		this.importEntity = currentImport;
		this.error = error;
		this.message = message;
	}

	public static enum Error {
		NONE, FORMAT, ALREADY_EXISTS
	}

}
