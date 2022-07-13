package MoneyMakers;

import java.util.ArrayList;

public class TeamData {

    private int id;
    private String name;
    private ArrayList<Results> results;

    public TeamData(String id, String name, ArrayList<Results> results) {
        this.id = Integer.parseInt(id);
        this.name = name;
        this.results = results;
    }

    public TeamData(String id, String name) {
        this.id = Integer.parseInt(id);
        this.name = name;
        this.results = new ArrayList<Results>();
    }


    public int getId() {
        return id;
    }

    public void setId(String id) {
        this.id = Integer.parseInt(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Results> getResults() {
        return results;
    }

    public String getNameAndResultsAsString() {
        String nameAndResults = this.id + ";" + this.name + ";";
        for (Results results : this.results)
            nameAndResults += results.getHomeTeamName() + " " + results.getHomeTeamScore() + ":" + results.getAwayTeamScore() + " " + results.getAwayTeamName() + ";";
        return nameAndResults;
    }

    public void setResults(ArrayList<Results> results) {
        this.results = results;
    }

    public void addResults(Results results) {
        this.results.add(results);
    }

}
