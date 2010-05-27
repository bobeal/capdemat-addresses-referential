package models;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.apache.commons.io.FileUtils;

import play.Play;
import play.db.jpa.Model;
import play.libs.Codec;
import play.vfs.VirtualFile;

@Entity
public class Import extends Model {

	@ManyToOne
	public Referential referential;

	@Enumerated(EnumType.STRING)
	public Type type;

	@OneToMany(mappedBy="importEntity")
	@OrderBy("importDate desc")
	public List<ImportLog> logs = new ArrayList<ImportLog>();

	public String originalFileName;
	public String fileName;

	@Transient
	public File file;

	public Date importStart = new Date();
	public Date importStop = null;

	public Long line;

	public static String path = "/data/imports/";

	public File getFile() {
		return Play.getFile(path + this.fileName);
	}

	public static Import queue(Referential referential, File csvFile, Type type) throws IOException {
		File storedFile = Play.getFile(path + Codec.UUID());
		storedFile.createNewFile();
		FileUtils.copyFile(csvFile, storedFile);

		Import currentImport = new Import();
		currentImport.type = type;
		currentImport.referential = referential;
		currentImport.originalFileName = csvFile.getName();
		currentImport.fileName = Codec.UUID();
		currentImport.save();

		return currentImport;
	}

	public void log(ImportLog.Error error, String message) {
		new ImportLog(this, error, message).save();
	}

	public static Import findFirstInQueue() {
		return Import.find("importStop is null order by date").first();
	}

	public static enum Type {
		CITY, WAY
	}

}
