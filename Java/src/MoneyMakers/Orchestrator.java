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

    private String header1 = "Date;" + "HomeTeamid;" + "HomeTeamName;" + "AwayTeamId;" + "AwayTeamName;" + "HomeGoals;" + "AwayGoals;";
    private String header = "Season;" + "Date;" + "HomeTeamid;" + "HomeTeamName;" + "AwayTeamId;" + "AwayTeamName;" + "HomeGoals;" + "AwayGoals;"
            + "FullTimeResult;" + "AverageHomeWinOdd;" + "AverageDrawWinOdd;" + "AverageAwayWinOdd;" + "AvgOver2_5;" + "AvgUnder2_5;" + "Div";
    private static final String idChar = '"' + "id" + '"';
    private static final String teamChar = '"' + "name" + '"';
    private static final String FilePath = "files/";
    private String CSV_FILE_NAME ;
    private int LeagueId ;



    public void start(int id, String years, String filename, String path) throws IOException, ParseException {
        this.CSV_FILE_NAME = filename + ".csv";
        this.LeagueId = id;
        //System.out.println("LeagueId: " + LeagueId + " filename:" + CSV_FILE_NAME + " years: " + years);
        ArrayList<Results> results = new ArrayList<Results>();
        HashMap<String, String> teamAndIds = new HashMap<String, String>();
        String[] tmpString = years.split(";");
        for (int i = 0; i < tmpString.length; i++) {

            results.addAll(getDataForYear(tmpString[i], path));
            HttpURLConnection con = OPENAPI(tmpString[i]);
            HashMap<String, String> tmp = fetchTeamAndIds(con,tmpString[i]);
            for (Map.Entry<String, String> entry : tmp.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                teamAndIds.put(key, value);
            }
            if (teamAndIds.size() != 0 && results.size() != 0) {
                sendToCSV(matchIdsAndTeams(teamAndIds, results), tmpString[i]);
            }
        }
        System.out.println("Created file " + CSV_FILE_NAME);
    }

    private ArrayList<Results> matchIdsAndTeams(HashMap<String, String> teamAndIds, ArrayList<Results> results) {

        ArrayList<TeamData> teamsData = new ArrayList<TeamData>();


        for (String name : teamAndIds.keySet()) {
            String key = name.toString().replaceAll("\"", "");
            String value = teamAndIds.get(name).toString().replaceAll("\"", "");
            teamsData.add(new TeamData(value, key));
        }
        /*for (TeamData team : teamsData) {
            System.out.println("team.getName(): " + team.getName());
        }*/
        for (Results result : results) {
            System.out.println("result.getHomeTeamName(): " + result.getHomeTeamName());
        }
        for (Results result : results) {
            for (TeamData team : teamsData) {
                if (result.getHomeTeamName().equalsIgnoreCase(team.getName())) {
                    result.setHomeTeamId(team.getId());
                    team.addHomeResults(result);
                    break;
                }
               /* else
                    System.out.println("result.getHomeTeamName(): " + result.getHomeTeamName() + " team.getName() " + team.getName());*/
                if (result.getAwayTeamName().equalsIgnoreCase(team.getName())) {
                    result.setAwayTeamId(team.getId());
                    team.addAwayResults(result);
                    break;
                }
             /*   else
                    System.out.println("result.getAwayTeamName(): " + result.getAwayTeamName() + " team.getName() " + team.getName());*/
            }
        }
        teamNameHammerHome(results,teamsData);
        teamNameHammerAway(results,teamsData);
        return results;
    }


    private void teamNameHammerHome(ArrayList<Results> results, ArrayList<TeamData> teamsData) {
        for (Results result : results) {
            if (result.homeTeamsWithId0())
                for (TeamData team : teamsData) {
                    if (result.getHomeTeamName().equalsIgnoreCase(team.getName()) ||
                            (result.getHomeTeamName().equalsIgnoreCase("MAN CITY") && team.getName().equalsIgnoreCase("MANCHESTER CITY")) ||
                            (result.getHomeTeamName().equalsIgnoreCase("MAN UNITED") && team.getName().equalsIgnoreCase("MANCHESTER UNITED")) ||
                            (result.getHomeTeamName().equalsIgnoreCase("HULL CITY") && team.getName().equalsIgnoreCase("HULL")) ||
                            (result.getHomeTeamName().equalsIgnoreCase("SHEFFIELD UTD") && team.getName().equalsIgnoreCase("SHEFFIELD UNITED")) ||
                            (result.getHomeTeamName().equalsIgnoreCase("STOKE CITY") && team.getName().equalsIgnoreCase("STOKE")) ||
                            (result.getHomeTeamName().equalsIgnoreCase("Beerschot VA") && team.getName().equalsIgnoreCase("BEERSCHOT WILRIJK")) ||
                            (result.getHomeTeamName().equalsIgnoreCase("BERGEN") && team.getName().equalsIgnoreCase("MONS")) ||
                            (result.getHomeTeamName().equalsIgnoreCase("CLUB BRUGGE") && team.getName().equalsIgnoreCase("Club Brugge KV")) ||
                            (result.getHomeTeamName().equalsIgnoreCase("Eupen") && team.getName().equalsIgnoreCase("AS Eupen")) ||
                            (result.getHomeTeamName().equalsIgnoreCase("GERMINAL") && team.getName().equalsIgnoreCase("Beerschot")) ||
                            (result.getHomeTeamName().equalsIgnoreCase("MECHELEN") && team.getName().equalsIgnoreCase("KV MECHELEN")) ||
                            (result.getHomeTeamName().equalsIgnoreCase("MOUSCRON") && team.getName().equalsIgnoreCase("ROYAL EXCEL MOUSCRON")) ||
                            (result.getHomeTeamName().equalsIgnoreCase("MOUSCRON-PERUWELZ") && team.getName().equalsIgnoreCase("RWDM")) ||
                            (result.getHomeTeamName().equalsIgnoreCase("OUD-HEVERLEE LEUVEN") && team.getName().equalsIgnoreCase("OH LEUVEN")) ||
                            (result.getHomeTeamName().equalsIgnoreCase("SERAING") && team.getName().equalsIgnoreCase("Seraing United")) ||
                            (result.getHomeTeamName().equalsIgnoreCase("ST TRUIDEN") && team.getName().equalsIgnoreCase("St. Truiden")) ||
                            (result.getHomeTeamName().equalsIgnoreCase("ST. GILLOISE") && team.getName().equalsIgnoreCase("UNION ST. GILLOISE")) ||
                            (result.getHomeTeamName().equalsIgnoreCase("STANDARD") && team.getName().equalsIgnoreCase("STANDARD LIEGE")) ||
                            (result.getHomeTeamName().equalsIgnoreCase("WAREGEM") && team.getName().equalsIgnoreCase("ZULTE WAREGEM")) ||
                            (result.getHomeTeamName().equalsIgnoreCase("Westerlo") && team.getName().equalsIgnoreCase("KVC Westerlo"))) {
                        result.setHomeTeamId(team.getId());
                        team.addHomeResults(result);
                        break;
                    }
                }
        }
    }

    private void teamNameHammerAway(ArrayList<Results> results, ArrayList<TeamData> teamsData) {
        for (Results result : results) {
            if (result.awayTeamsWithId0())
                for (TeamData team : teamsData) {
                    if (result.getAwayTeamName().equalsIgnoreCase(team.getName()) ||
                            (result.getAwayTeamName().equalsIgnoreCase("MAN CITY") && team.getName().equalsIgnoreCase("MANCHESTER CITY")) ||
                            (result.getAwayTeamName().equalsIgnoreCase("MAN UNITED") && team.getName().equalsIgnoreCase("MANCHESTER UNITED")) ||
                            (result.getAwayTeamName().equalsIgnoreCase("HULL CITY") && team.getName().equalsIgnoreCase("HULL")) ||
                            (result.getAwayTeamName().equalsIgnoreCase("SHEFFIELD UTD") && team.getName().equalsIgnoreCase("SHEFFIELD UNITED")) ||
                            (result.getAwayTeamName().equalsIgnoreCase("STOKE CITY") && team.getName().equalsIgnoreCase("STOKE")) ||
                            (result.getAwayTeamName().equalsIgnoreCase("Beerschot VA") && team.getName().equalsIgnoreCase("BEERSCHOT WILRIJK")) ||
                            (result.getAwayTeamName().equalsIgnoreCase("BERGEN") && team.getName().equalsIgnoreCase("MONS")) ||
                            (result.getAwayTeamName().equalsIgnoreCase("CLUB BRUGGE") && team.getName().equalsIgnoreCase("Club Brugge KV")) ||
                            (result.getAwayTeamName().equalsIgnoreCase("Eupen") && team.getName().equalsIgnoreCase("AS Eupen")) ||
                            (result.getAwayTeamName().equalsIgnoreCase("GERMINAL") && team.getName().equalsIgnoreCase("Beerschot")) ||
                            (result.getAwayTeamName().equalsIgnoreCase("MECHELEN") && team.getName().equalsIgnoreCase("KV MECHELEN")) ||
                            (result.getAwayTeamName().equalsIgnoreCase("MOUSCRON") && team.getName().equalsIgnoreCase("ROYAL EXCEL MOUSCRON")) ||
                            (result.getAwayTeamName().equalsIgnoreCase("MOUSCRON-PERUWELZ") && team.getName().equalsIgnoreCase("RWDM")) ||
                            (result.getAwayTeamName().equalsIgnoreCase("OUD-HEVERLEE LEUVEN") && team.getName().equalsIgnoreCase("OH LEUVEN")) ||
                            (result.getAwayTeamName().equalsIgnoreCase("SERAING") && team.getName().equalsIgnoreCase("Seraing United")) ||
                            (result.getAwayTeamName().equalsIgnoreCase("ST TRUIDEN") && team.getName().equalsIgnoreCase("St. Truiden")) ||
                            (result.getAwayTeamName().equalsIgnoreCase("ST. GILLOISE") && team.getName().equalsIgnoreCase("UNION ST. GILLOISE")) ||
                            (result.getAwayTeamName().equalsIgnoreCase("STANDARD") && team.getName().equalsIgnoreCase("STANDARD LIEGE")) ||
                            (result.getAwayTeamName().equalsIgnoreCase("WAREGEM") && team.getName().equalsIgnoreCase("ZULTE WAREGEM")) ||
                            (result.getAwayTeamName().equalsIgnoreCase("Westerlo") && team.getName().equalsIgnoreCase("KVC Westerlo"))) {
                        result.setAwayTeamId(team.getId());
                        team.addAwayResults(result);
                        break;
                    }
                }
        }
    }


    private ArrayList<Results> getHomeTeamScores(String[] results, String year) throws ParseException {

            // row 0 -> headers, não interessa para aqui
            ArrayList<Results> finalResults = new ArrayList<Results>();
            ArrayList<String> headers = new ArrayList<String>(Arrays.asList(results[0].split(",")));

            for (int i = 1; i < results.length; i++) {
                String[] tmp = new String[0];
                try {
                    tmp = results[i].split(",");
                    //Some files have lines with nothing, on those cases, we skip
                    if(tmp[headers.indexOf("Date")] == null || tmp[headers.indexOf("Date")].isEmpty()){
                        continue;
                    }
                    if (headers.indexOf("Avg>2.5") > 0)
                        finalResults.add(new Results(tmp[headers.indexOf("Div")], year.replaceAll(".csv", ""), tmp[headers.indexOf("HomeTeam")], tmp[headers.indexOf("AwayTeam")], tmp[headers.indexOf("Date")], tmp[headers.indexOf("FTHG")],
                                tmp[headers.indexOf("FTAG")], tmp[headers.indexOf("FTR")], tmp[headers.indexOf("AvgH")], tmp[headers.indexOf("AvgD")],
                                tmp[headers.indexOf("AvgA")], tmp[headers.indexOf("Avg>2.5")], tmp[headers.indexOf("Avg<2.5")]));
                    else
                        finalResults.add(new Results(tmp[headers.indexOf("Div")], year.replaceAll(".csv", ""), tmp[headers.indexOf("HomeTeam")], tmp[headers.indexOf("AwayTeam")], tmp[headers.indexOf("Date")], tmp[headers.indexOf("FTHG")],
                                tmp[headers.indexOf("FTAG")], tmp[headers.indexOf("FTR")], tmp[headers.indexOf("B365H")], tmp[headers.indexOf("B365D")],
                                tmp[headers.indexOf("B365A")], tmp[headers.indexOf("BbAv>2.5")], tmp[headers.indexOf("BbAv<2.5")]));
                } catch (ArrayIndexOutOfBoundsException e) {
                  continue;
                }
            }
            return finalResults;

    }

    private String readCVSFilesDates() throws FileNotFoundException, ParseException {
        File folder = new File("../Data/football-data/England/englishpremierleague/");
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

    private ArrayList<Results> getDataForYear(String year, String path) throws ParseException, FileNotFoundException {
        Scanner sc = new Scanner(new File(path + year));
        //parsing a CSV file into the constructor of Scanner class
        sc.useDelimiter("\n");
        String file = "";
        //setting comma as delimiter pattern
        while (sc.hasNext()) {
            file += sc.next() + "\n";
        }

        sc.close();
        //  System.out.println(file);
        return getHomeTeamScores(file.split("\n"), year);
    }


    private void sendToCSV(ArrayList<Results> results, String year) throws IOException {
        List<String[]> csvData = new ArrayList<>();
        csvData.add(new String[]{header});
        // sort(results);
        for (Results result : results)
            csvData.add(new String[]{getResultToCSVFormat(result)});


        try (CSVWriter writer = new CSVWriter(new FileWriter(new File(FilePath,CSV_FILE_NAME)))) {
            writer.writeAll(csvData, false);
        }
    }

    public String getResultToCSVFormat(Results result) {
        return result.getSeason() + ";" + result.getGameDateFormated() + ";" + result.getHomeTeamId() + ";" + result.getHomeTeamName() + ";" + result.getAwayTeamId() + ";" + result.getAwayTeamName() + ";" + result.getFull_Time_Home_Team_Goals() + ";" + result.getFull_Time_Away_Team_Goals() + ";"
                + result.getFull_Time_Result() + ";" + result.getMarket_average_home_win_odds() + ";" + result.getMarket_average_draw_win_odds() + ";" + result.getMarket_average_away_win_odds() + ";" + result.getMarket_average_over_2_5_goals() + ";"
                + result.getMarket_average_under_2_5_goals() + ";" + LeagueId + ";" ;
    }


    private static HashMap<String, String> fetchTeamAndIds(HttpURLConnection conn, String season) throws IOException {
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
                System.out.println("resultObj: " + resultArray );
                System.out.println("NOTHING TO SHOW");
            }
            return teamsAndIds;
        } catch (Exception e) {
            e.printStackTrace();
            br.close();
            return null;
        }
    }

    public void GetLeagues() throws IOException {

        try {
            //System.out.println("waiting 6 seconds");
            //Thread.sleep(6000);
            String u = "https://v3.football.api-sports.io/leagues";
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
            }
            else {
                BufferedReader br = null;
                br = null;
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String strCurrentLine;
                String result = "[";
                while ((strCurrentLine = br.readLine()) != null) {
                    result += strCurrentLine;
                }
                result += "]";
                CreateFile(result);
            }
            return ;
        } catch (Exception e) {
            e.printStackTrace();
            return ;
        }

    }

    public  HttpURLConnection OPENAPI(String year) throws IOException {

        try {
         //   System.out.println("waiting 6 seconds so the number of requests don't surpass the 10 request per minute");
            //Thread.sleep(6000);
            String u = "https://v3.football.api-sports.io/teams?league=" + LeagueId + "&season=" + year.replace(".csv", "");
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

    private static void CreateFile(String info) {
        try {
            File myObj = new File("Test.txt");
            if(!myObj.exists()){
                myObj.createNewFile();
            }else{
                System.out.println("File already exists");
            }
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
    }


}
