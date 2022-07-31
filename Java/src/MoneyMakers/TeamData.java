package MoneyMakers;

import java.util.ArrayList;

public class TeamData {

    private int id;
    private String name;
    private ArrayList<Results> HomeResults;
    private ArrayList<Results> AwayResults;

    public TeamData(String id, String name, ArrayList<Results> HomeResults,ArrayList<Results> AwayResults) {
        this.id = Integer.parseInt(id);
        this.name = name;
        this.HomeResults = HomeResults;
        this.AwayResults = HomeResults;
    }

    public TeamData(String id, String name) {
        this.id = Integer.parseInt(id);
        this.name = name;
        this.HomeResults = new ArrayList<Results>();
        this.AwayResults = new ArrayList<Results>();
    }


    public int getId() {
        return id;
    }

    public void setId(String id) {
        this.id = Integer.parseInt(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Results> getHomeResults() {
        return HomeResults;
    }

    public void setHomeResults(ArrayList<Results> homeResults) {
        this.HomeResults = homeResults;
    }

    public void addHomeResults(Results results) {
        this.HomeResults.add(results);
    }

    public void addAwayResults(Results results) {
        this.AwayResults.add(results);
    }

    public ArrayList<Results> getAwayResults() {
        return AwayResults;
    }

    public void setAwayResults(ArrayList<Results> awayResults) {
        AwayResults = awayResults;
    }

    public String getNameAndResultsAsString() {
        String nameAndResults = this.id + ";" + this.name + ";";
        for (Results results : this.HomeResults)
            nameAndResults += results.getHomeTeamName() + " " + results.getHomeTeamScore() + ":" + results.getAwayTeamScore() + " " + results.getAwayTeamName() + ";";
        return nameAndResults;
    }


}
