package MoneyMakers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

public class General {
    Orchestrator orchestrator = new Orchestrator();
    LeagueIds leagueIds = new LeagueIds();

    public void start() throws IOException, ParseException {
        readCVSFilesDates("../Data/football-data","");
        return;

    }

    private void readCVSFilesDates(String path, String dirName) throws IOException, ParseException {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        String years = "";

        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    //System.out.println("Found file " + listOfFiles[i].getName());
                    years += listOfFiles[i].getName() + ";";
                } else if (listOfFiles[i].isDirectory()) {
                    readCVSFilesDates(path + "/" + listOfFiles[i].getName() + "/", listOfFiles[i].getName());
                }
            }
            int id = leagueIds.getLeagueIds(dirName);
            path = path.replace("//", "/");
            if(id != 0){
                orchestrator.start(id,years, dirName, path);
            }
            else {
                System.out.println("No id found for league " + dirName);
            }
        } else {
            System.out.println("No files found on directory " + path);
        }
    }

}
