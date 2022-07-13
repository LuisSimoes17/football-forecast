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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Orchestrator {

    public static final int PREMIER_LEAGUE_ID = 39;
    public static final String SEASON = "2021";
    public static final String idChar = '"' + "id" + '"';
    public static final String teamChar = '"' + "name" + '"';
    public static final String CSV_FILE_NAME = "results.csv";
    public static final String header = "id;"+"name;"+"resultFromHomeTeam";


    public void start() throws IOException {
        HttpURLConnection con = OPENAPI();
        HashMap<String, String> teamAndIds = fetchTeamAndIds(con);
        ArrayList<Results> results = readCVSFile();

        if (teamAndIds.size() != 0 && results.size() !=0){
            sendToCSV(matchIdsAndTeams(teamAndIds, results));
        }



    }

    private ArrayList<TeamData> matchIdsAndTeams(HashMap<String, String> teamAndIds, ArrayList<Results> results) {

        ArrayList<TeamData> teamsData = new ArrayList<TeamData>();

        for (String name: teamAndIds.keySet()) {
            String key = name.toString().replaceAll("\"","");
            String value = teamAndIds.get(name).toString().replaceAll("\"","");
            teamsData.add(new TeamData(value,key));
        }
        for (TeamData team: teamsData){
            for(Results result: results){
                if(result.getHomeTeamName().equalsIgnoreCase(team.getName()) ||
                        (result.getHomeTeamName().equalsIgnoreCase("MAN CITY") && team.getName().equalsIgnoreCase("MANCHESTER CITY")) ||
                        (result.getHomeTeamName().equalsIgnoreCase("MAN UNITED") && team.getName().equalsIgnoreCase("MANCHESTER UNITED")))
                    team.addResults(result);
            }
        }
        return teamsData;
    }

    private ArrayList<Results> getHomeTeamScores(String[] results){
        // row 0 -> headers, não interessa para aqui
        ArrayList<Results> finalResults = new ArrayList<Results>();
        for(int i = 1; i < results.length; i++){
            String[] tmp = results[i].split(",");
            finalResults.add(new Results(tmp[3],tmp[4],tmp[5],tmp[6]));
        }
        return finalResults;
    }

    private ArrayList<Results> readCVSFile() throws FileNotFoundException {
        Scanner sc = new Scanner(new File("Premier League.csv"));
        //parsing a CSV file into the constructor of Scanner class
        sc.useDelimiter("\n");
        String file = "";
        //setting comma as delimiter pattern
        while (sc.hasNext()) {
            file+= sc.next() + "\n";
        }

        sc.close();
        return getHomeTeamScores(file.split("\n"));
    }



    private void sendToCSV(ArrayList<TeamData> teams) throws IOException {
        List<String[]> csvData = new ArrayList<>();
        csvData.add(new String[]{header});
        sort(teams);
        for (TeamData team: teams) {
            //String id = team.getId().toString();
            String nameAndResults = team.getNameAndResultsAsString();
            csvData.add(new String[] {nameAndResults});
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE_NAME))) {
            writer.writeAll(csvData, false);
        }
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

    public static HttpURLConnection OPENAPI() throws IOException {


        try {
            String u = "https://api-football-v1.p.rapidapi.com/v3/teams?league=" + PREMIER_LEAGUE_ID +"&season=" + SEASON;
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

    private void sort(ArrayList<TeamData> list) {

        list.sort((o1, o2)
                -> o1.getName().compareTo(
                o2.getName()));
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
