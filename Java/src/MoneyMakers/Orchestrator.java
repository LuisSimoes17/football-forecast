package MoneyMakers;

import com.opencsv.CSVWriter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Orchestrator {

    public static final int PREMIER_LEAGUE_ID = 39;
    public static final String SEASON = "2010";
    public static final String idChar = '"' + "id" + '"';
    public static final String teamChar = '"' + "name" + '"';
    public static final String CSV_FILE_NAME = "results.csv";


    public void start() throws IOException {
        HttpURLConnection con = OPENAPI();
        HashMap<String, String> teamAndIds = fetchTeamAndIds(con);
        if (teamAndIds.size() != 0)
            sendToCSV(teamAndIds);
    }


    private void sendToCSV(HashMap<String, String> teamAndIds) throws IOException {
        String[] header = {"id", "name"};

        List<String[]> csvData = new ArrayList<>();
        csvData.add(header);
        for (String name: teamAndIds.keySet()) {
            String key = name.toString();
            String value = teamAndIds.get(name).toString();
            csvData.add(new String[] {key,value});
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE_NAME))) {
            writer.writeAll(csvData, true);
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
         /*   for (String name: teamsAndIds.keySet()) {
                String key = name.toString();
                String value = teamsAndIds.get(name).toString();
                System.out.println("Equipa:" + key + ",id: " + value);
            }*/
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

/*
* n/a	n/a	n/a	n/a	n/a	Final time Home Goals	Final Time Away Goals	Final Time Result	Half Time Home Goals	Final Time Away Goals	Half time Result	n/a	Home Shots	Away Shots	Home Shots on Target	Away Shots on Target	Home faults	Away Faults	Home cournes	away cournes	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a	n/a
Div	Date	Time	HomeTeam	AwayTeam	FTHG	FTAG	FTR	HTHG	HTAG	HTR	Referee	HS	AS	HST	AST	HF	AF	HC	AC	HY	AY	HR	AR	B365H	B365D	B365A	BWH	BWD	BWA	IWH	IWD	IWA	PSH	PSD	PSA	WHH	WHD	WHA	VCH	VCD	VCA	MaxH	MaxD	MaxA	AvgH	AvgD	AvgA	B365>2.5	B365<2.5	P>2.5	P<2.5	Max>2.5	Max<2.5	Avg>2.5	Avg<2.5	AHh	B365AHH	B365AHA	PAHH	PAHA	MaxAHH	MaxAHA	AvgAHH	AvgAHA	B365CH	B365CD	B365CA	BWCH	BWCD	BWCA	IWCH	IWCD	IWCA	PSCH	PSCD	PSCA	WHCH	WHCD	WHCA	VCCH	VCCD	VCCA	MaxCH	MaxCD	MaxCA	AvgCH	AvgCD	AvgCA	B365C>2.5	B365C<2.5	PC>2.5	PC<2.5	MaxC>2.5	MaxC<2.5	AvgC>2.5	AvgC<2.5	AHCh	B365CAHH	B365CAHA	PCAHH	PCAHA	MaxCAHH	MaxCAHA	AvgCAHH	AvgCAHA
* */


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

    private static void CreateFile(String info) {
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
    }

}
