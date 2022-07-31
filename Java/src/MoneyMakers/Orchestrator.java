package MoneyMakers;

import com.opencsv.CSVWriter;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ResultTreeType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Orchestrator {

    public static final int PREMIER_LEAGUE_ID = 39;
    public static final String idChar = '"' + "id" + '"';
    public static final String teamChar = '"' + "name" + '"';
    public static final String CSV_FILE_NAME = "football-data_wIndexes.csv";
    public static final String header = "Date;"+"HomeTeamid;"+"HomeTeamName;"+"AwayTeamId;"+"AwayTeamName;"+"HomeGoals;"+"AwayGoals;";


    public void start() throws IOException, ParseException {
        String years = readCVSFilesDates();
        ArrayList<Results> results = new ArrayList<Results>();
        HashMap<String, String> teamAndIds = new HashMap<String, String>();
        String[] tmpString = years.split(";");
        for(int i = 0; i < tmpString.length; i++) {

            results.addAll(getDataForYear(tmpString[i]));
            HttpURLConnection con = OPENAPI(tmpString[i]);
            HashMap<String, String> tmp = fetchTeamAndIds(con);
            for (Map.Entry<String, String> entry : tmp.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                teamAndIds.put(key, value);
            }
            if (teamAndIds.size() != 0 && results.size() != 0) {
                sendToCSV(matchIdsAndTeams(teamAndIds, results), tmpString[i]);
            }
        }

    }

    private ArrayList<Results> matchIdsAndTeams(HashMap<String, String> teamAndIds, ArrayList<Results> results) {

        ArrayList<TeamData> teamsData = new ArrayList<TeamData>();

        for (String name: teamAndIds.keySet()) {
            String key = name.toString().replaceAll("\"","");
            String value = teamAndIds.get(name).toString().replaceAll("\"","");
            teamsData.add(new TeamData(value,key));
        }
        for(Results result: results){
            for (TeamData team: teamsData){
                if(result.getHomeTeamName().equalsIgnoreCase(team.getName()) ||
                        (result.getHomeTeamName().equalsIgnoreCase("MAN CITY") && team.getName().equalsIgnoreCase("MANCHESTER CITY")) ||
                        (result.getHomeTeamName().equalsIgnoreCase("MAN UNITED") && team.getName().equalsIgnoreCase("MANCHESTER UNITED"))) {
                    team.addHomeResults(result);
                    result.setHomeTeamId(team.getId());
                }
                if(result.getAwayTeamName().equalsIgnoreCase(team.getName()) ||
                        (result.getAwayTeamName().equalsIgnoreCase("MAN CITY") && team.getName().equalsIgnoreCase("MANCHESTER CITY")) ||
                        (result.getAwayTeamName().equalsIgnoreCase("MAN UNITED") && team.getName().equalsIgnoreCase("MANCHESTER UNITED"))) {
                    team.addAwayResults(result);
                    result.setAwayTeamId(team.getId());
                }
            }
        }
        return results;
    }

    private ArrayList<Results> getHomeTeamScores(String[] results) throws ParseException {
        // row 0 -> headers, não interessa para aqui
        ArrayList<Results> finalResults = new ArrayList<Results>();
        for(int i = 1; i < results.length; i++){
            String[] tmp = results[i].split(",");
            if(tmp.length >= 6)
             finalResults.add(new Results(tmp[3],tmp[4],tmp[5],tmp[6],tmp[1]));
        }
        return finalResults;
    }

    private String readCVSFilesDates() throws FileNotFoundException, ParseException {
        File folder = new File("../Data/football-data/englishpremierleague/");
        File[] listOfFiles = folder.listFiles();
        String years = "";

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
               // System.out.println("File " + listOfFiles[i].getName());
                years += listOfFiles[i].getName() + ";";
            } else if (listOfFiles[i].isDirectory()) {
               // System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
        return years;
    }

    private ArrayList<Results> getDataForYear(String year) throws ParseException, FileNotFoundException {
        Scanner sc = new Scanner(new File("../Data/football-data/englishpremierleague/" + year));
        //parsing a CSV file into the constructor of Scanner class
        sc.useDelimiter("\n");
        String file = "";
        //setting comma as delimiter pattern
        while (sc.hasNext()) {
            file+= sc.next() + "\n";
        }

        sc.close();
      //  System.out.println(file);
        return getHomeTeamScores(file.split("\n"));
    }



    private void sendToCSV(ArrayList<Results> results, String year) throws IOException {
        List<String[]> csvData = new ArrayList<>();
        csvData.add(new String[]{header});
       // sort(results);
        for(Results result : results)
        csvData.add(new String[] {getResultToCSVFormat(result)});


        try (CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE_NAME))) {
            writer.writeAll(csvData, false);
        }
    }
    public String getResultToCSVFormat(Results result) {
        return result.getGameDateFormated() + ";" + result.getHomeTeamId() + ";" + result.getHomeTeamName()  + ";" + result.getAwayTeamId() + ";" + result.getAwayTeamName() + ";" + result.getHomeTeamScore() + ";"+ result.getAwayTeamScore() + ";";

    }



    private static HashMap<String, String>  fetchTeamAndIds(HttpURLConnection conn) throws IOException {
        BufferedReader br = null;
        try {
            br = null;
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String strCurrentLine;
            String result = "[";
            while ((strCurrentLine = br.readLine()) != null) {
                result += strCurrentLine;
            }
            result += "]";
            br.close();

            JSONArray inputArray = new JSONArray(result);

            JSONObject jo = inputArray.getJSONObject(0);
            //System.out.println(jo);
            HashMap<String, String> teamsAndIds = new HashMap<>();
            for(String keyStr : jo.keySet())
            {
                Object keyvalue = jo.get(keyStr);
                if(keyStr.equals("response")) {
                    result = keyvalue.toString();
                    break;
                }
            }
            //  System.out.println(result);

            JSONArray resultArray = new JSONArray(result);
            String id = "";
            String team = "";
            for(int i = 0; i<resultArray.length() ;i++) {
                JSONObject resultObj = resultArray.getJSONObject(i);
                for (String keyStr : resultObj.keySet()) {
                    Object keyvalue = resultObj.get(keyStr);
                    // System.out.println("key: " + keyStr + " value: " + keyvalue);
                    String[] keyvalueString  = keyvalue.toString().split(",");
                    if(keyStr.equals("venue")) {
                        for (int j = 0; j < keyvalueString.length; j++) {
                            //   System.out.println("keyvalueString[j]: " + keyvalueString[j] + " " + keyvalueString[j].toString());
                            if (keyvalueString[j].contains(idChar)) {
                                id = keyvalueString[j].split(":")[1];
                                //System.out.println("id: " + id);
                            }
                        }
                    }
                    else if(keyStr.equals("team")) {
                        for (int j = 0; j < keyvalueString.length; j++) {
                            // System.out.println("keyvalueString[j]: " + keyvalueString[j] + " " + keyvalueString[j].toString());
                            if (keyvalueString[j].contains(teamChar)) {
                                team = keyvalueString[j].split(":")[1];
                                // System.out.println("team: " + team);
                            }
                        }
                    }
                }
                teamsAndIds.put(team,id);

            }
            if(teamsAndIds.size() == 0)
            {
                System.out.println("NOTHING TO SHOW");
            }
            return teamsAndIds;
        } catch (Exception e) {
            e.printStackTrace();
            br.close();
            return null;
        }
    }

    public static HttpURLConnection OPENAPI(String year) throws IOException {


        try {
            String u = "https://api-football-v1.p.rapidapi.com/v3/teams?league=" + PREMIER_LEAGUE_ID +"&season=" + year.replace(".csv","");
            URL url = new URL(u);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com");
            conn.setRequestProperty("X-RapidAPI-Key", "9TW4PwlX6smsh65fadWDII0s5Eefp1E877cjsn62XI8AChow8z");



            conn.connect();

            //Check if connect is made
            int responseCode = conn.getResponseCode();
            // 200 OK
            if (responseCode != 200) {
                System.out.println("ERROR, HttpResponseCode:" + responseCode);
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {

            }
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sort(ArrayList<Results> list) {

        list.sort((o1, o2)
                -> o1.getGameDate().compareTo(
                o2.getGameDate()));
    }

    private boolean containsIgnoreCase(String str, String searchStr)     {
        if(str == null || searchStr == null) return false;

        final int length = searchStr.length();
        if (length == 0)
            return true;

        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length))
                return true;
        }
        return false;
    }

  /*  private static void CreateFile(String info) {
        try {
            File myObj = new File("filename.txt");
            if (myObj.createNewFile()) {
                myObj.delete();
                myObj = new File("filename.txt");
            }
            Files.write(Paths.get(String.valueOf(myObj)), info.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return ;
    }*/


}
