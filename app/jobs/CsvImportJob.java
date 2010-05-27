package jobs;

import java.io.IOException;
import java.util.Date;

import models.City;
import models.Import;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;

@Every("10s")
public class CsvImportJob extends Job<Void> {

	@Override
	public void doJob() {
		Import currentImport = Import.findFirstInQueue();
		if(currentImport != null) {
			try {
				switch(currentImport.type) {
					case CITY:
						City.importCsv(currentImport);
						currentImport.importStop = new Date();
						currentImport.save();
						break;
					case WAY:
						//TODO
						break;
				}
			} catch (IOException e) {
				Logger.warn("%s - %s", e.getMessage(), e.getCause());
			}
		}
	}

}
