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

    public String header1 = "Date;" + "HomeTeamid;" + "HomeTeamName;" + "AwayTeamId;" + "AwayTeamName;" + "HomeGoals;" + "AwayGoals;";
    public String header = "Season;" + "Date;" + "HomeTeamid;" + "HomeTeamName;" + "AwayTeamId;" + "AwayTeamName;" + "HomeGoals;" + "AwayGoals;"
            + "FullTimeResult;" + "AverageHomeWinOdd;" + "AverageDrawWinOdd;" + "AverageAwayWinOdd;" + "AvgOver2_5;" + "AvgUnder2_5;" + "Div";
    public static final int PREMIER_LEAGUE_ID = 39;
    public static final String idChar = '"' + "id" + '"';
    public static final String teamChar = '"' + "name" + '"';
    public static final String CSV_FILE_NAME = "football-data_wIndexes.csv";



    public void start() throws IOException, ParseException {
        String years = readCVSFilesDates();
        ArrayList<Results> results = new ArrayList<Results>();
        HashMap<String, String> teamAndIds = new HashMap<String, String>();
        String[] tmpString = years.split(";");
        for (int i = 0; i < tmpString.length; i++) {

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

        for (String name : teamAndIds.keySet()) {
            String key = name.toString().replaceAll("\"", "");
            String value = teamAndIds.get(name).toString().replaceAll("\"", "");
            teamsData.add(new TeamData(value, key));
        }
        for (Results result : results) {
            for (TeamData team : teamsData) {
               /* System.out.println(result.getHomeTeamName());
                System.out.println(result.getAwayTeamName());*/
                if (result.getHomeTeamName().equalsIgnoreCase(team.getName()) ||
                        (result.getHomeTeamName().equalsIgnoreCase("MAN CITY") && team.getName().equalsIgnoreCase("MANCHESTER CITY")) ||
                        (result.getHomeTeamName().equalsIgnoreCase("MAN UNITED") && team.getName().equalsIgnoreCase("MANCHESTER UNITED"))||
                        (result.getHomeTeamName().equalsIgnoreCase("Hull") && team.getName().equalsIgnoreCase("Hull City")) ||
                        (result.getHomeTeamName().equalsIgnoreCase("Sheffield United") && team.getName().equalsIgnoreCase("Sheffield Utd")) ||
                        (result.getHomeTeamName().equalsIgnoreCase("Stoke") && team.getName().equalsIgnoreCase("Stoke City"))) {
                    team.addHomeResults(result);
                    result.setHomeTeamId(team.getId());
                }
                if (result.getAwayTeamName().equalsIgnoreCase(team.getName()) ||
                        (result.getAwayTeamName().equalsIgnoreCase("MAN CITY") && team.getName().equalsIgnoreCase("MANCHESTER CITY")) ||
                        (result.getAwayTeamName().equalsIgnoreCase("MAN UNITED") && team.getName().equalsIgnoreCase("MANCHESTER UNITED")) ||
                        (result.getAwayTeamName().equalsIgnoreCase("Hull") && team.getName().equalsIgnoreCase("Hull City")) ||
                        (result.getAwayTeamName().equalsIgnoreCase("Sheffield United") && team.getName().equalsIgnoreCase("Sheffield Utd")) ||
                        (result.getAwayTeamName().equalsIgnoreCase("Stoke") && team.getName().equalsIgnoreCase("Stoke City"))) {
                    team.addAwayResults(result);
                    result.setAwayTeamId(team.getId());
                }
            }
        }
        return results;
    }
    /*Hull -> Hull City
Sheffield United -> Sheffield Utd
Stoke -> Stoke City*/

    private ArrayList<Results> getHomeTeamScores(String[] results) throws ParseException {
        // row 0 -> headers, não interessa para aqui
        ArrayList<Results> finalResults = new ArrayList<Results>();
        ArrayList<String> headers = new ArrayList<String>(Arrays.asList(results[0].split(",")));

        for (int i = 1; i < results.length; i++) {
            String[] tmp = results[i].split(",");
            int season = getSeason(tmp[headers.indexOf("Date")]);
            if(headers.indexOf("Avg>2.5") > 0)
            finalResults.add(new Results(tmp[headers.indexOf("Div")],season,tmp[headers.indexOf("HomeTeam")],tmp[headers.indexOf("AwayTeam")],tmp[headers.indexOf("Date")],tmp[headers.indexOf("FTHG")],
                    tmp[headers.indexOf("FTAG")],tmp[headers.indexOf("FTR")],tmp[headers.indexOf("AvgH")],tmp[headers.indexOf("AvgD")],
                    tmp[headers.indexOf("AvgA")],tmp[headers.indexOf("Avg>2.5")],tmp[headers.indexOf("Avg<2.5")]));
            else
                finalResults.add(new Results(tmp[headers.indexOf("Div")],season,tmp[headers.indexOf("HomeTeam")],tmp[headers.indexOf("AwayTeam")],tmp[headers.indexOf("Date")],tmp[headers.indexOf("FTHG")],
                        tmp[headers.indexOf("FTAG")],tmp[headers.indexOf("FTR")],tmp[headers.indexOf("B365H")],tmp[headers.indexOf("B365D")],
                        tmp[headers.indexOf("B365A")],tmp[headers.indexOf("BbAv>2.5")],tmp[headers.indexOf("BbAv<2.5")]));

        }
        return finalResults;
    }

    private int getSeason(String date) {
      //  14/08/10
        int month = Integer.parseInt(date.split("/")[1].replaceAll("0",""));
        int year = Integer.parseInt(date.split("/")[2].replaceAll("0",""));
        if(month>7)
            return year;
        else
            return  year -1;
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
            file += sc.next() + "\n";
        }

        sc.close();
        //  System.out.println(file);
        return getHomeTeamScores(file.split("\n"));
    }


    private void sendToCSV(ArrayList<Results> results, String year) throws IOException {
        List<String[]> csvData = new ArrayList<>();
        csvData.add(new String[]{header});
        // sort(results);
        for (Results result : results)
            csvData.add(new String[]{getResultToCSVFormat(result)});


        try (CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE_NAME))) {
            writer.writeAll(csvData, false);
        }
    }

    public String getResultToCSVFormat(Results result) {
        return result.getSeason() + ";" + result.getGameDateFormated() + ";" + result.getHomeTeamId() + ";" + result.getHomeTeamName() + ";" + result.getAwayTeamId() + ";" + result.getAwayTeamName() + ";" + result.getFull_Time_Home_Team_Goals() + ";" + result.getFull_Time_Away_Team_Goals() + ";"
                + result.getFull_Time_Result() + ";" + result.getMarket_average_home_win_odds() + ";" + result.getMarket_average_draw_win_odds() + ";" + result.getMarket_average_away_win_odds() + ";" + result.getMarket_average_over_2_5_goals() + ";"
                + result.getMarket_average_under_2_5_goals() + ";" + PREMIER_LEAGUE_ID + ";" ;
    }


    private static HashMap<String, String> fetchTeamAndIds(HttpURLConnection conn) throws IOException {
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
            for (String keyStr : jo.keySet()) {
                Object keyvalue = jo.get(keyStr);
                if (keyStr.equals("response")) {
                    result = keyvalue.toString();
                    break;
                }
            }
              //System.out.println(result);

            JSONArray resultArray = new JSONArray(result);
            String id = "";
            String team = "";
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject resultObj = resultArray.getJSONObject(i);
                for (String keyStr : resultObj.keySet()) {
                    Object keyvalue = resultObj.get(keyStr);
                    // System.out.println("key: " + keyStr + " value: " + keyvalue);
                    String[] keyvalueString = keyvalue.toString().split(",");
                    if (keyStr.equals("team")) {
                        //System.out.println("keyStr " + keyStr);
                        for (int j = 0; j < keyvalueString.length; j++) {
                            // System.out.println("keyvalueString[j]: " + keyvalueString[j] + " " + keyvalueString[j].toString());
                            if (keyvalueString[j].contains(teamChar)) {
                                team = keyvalueString[j].split(":")[1];
                                // System.out.println("team: " + team);
                            }
                            if (keyvalueString[j].contains(idChar)) {
                                id = keyvalueString[j].split(":")[1].replaceAll("\"", "").replaceAll("}","");
                                //System.out.println("id: " + id);
                            }
                        }
                    }
                }
                teamsAndIds.put(team, id);

            }
            if (teamsAndIds.size() == 0) {
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
            System.out.println("waiting 10 seconds");
            Thread.sleep(6000);
            String u = "https://v3.football.api-sports.io/teams?league=" + PREMIER_LEAGUE_ID + "&season=" + year.replace(".csv", "");
            URL url = new URL(u);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-RapidAPI-Host", "v3.football.api-sports.io");
            conn.setRequestProperty("X-RapidAPI-Key", "15f514ec0209e0f49397240d5e166bd9");


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

    private boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) return false;

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
