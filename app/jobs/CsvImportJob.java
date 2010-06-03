package jobs;

import java.io.IOException;
import java.util.Date;

import models.City;
import models.Import;
import models.Way;
import play.Logger;
import play.Play;
import play.jobs.Every;
import play.jobs.Job;

@Every("10s")
public class CsvImportJob extends Job<Void> {

    @Override
    public void doJob() {
        if(Play.id.equals("test")) return;
        Import currentImport = Import.findFirstInQueue();
        if(currentImport != null) {
            try {
                if(currentImport.importStart == null) {
                    currentImport.importStart = new Date();
                    currentImport.save();
                }
                switch(currentImport.type) {
                    case CITY:
                        City.importCsv(currentImport);
                        break;
                    case WAY:
                        Way.importCsv(currentImport);
                        break;
                }
                currentImport.importStop = new Date();
                currentImport.save();
            } catch (IOException e) {
                Logger.warn("%s - %s", e.getMessage(), e.getCause());
            }
        }
    }

}
