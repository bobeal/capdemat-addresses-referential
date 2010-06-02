package jobs;

import java.io.IOException;
import java.util.Date;

import models.City;
import models.Import;
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
                switch(currentImport.type) {
                    case CITY:
                        if(currentImport.importStart == null) {
                            currentImport.importStart = new Date();
                            currentImport.save();
                        }
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
