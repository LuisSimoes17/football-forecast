package MoneyMakers;

public class Results {
    private String HomeTeamName;
    private String AwayTeamName;
    private String HomeTeamScore;
    private String AwayTeamScore;


    public Results(String homeTeamName, String awayTeamName, String homeTeamScore, String awayTeamScore) {
        HomeTeamName = homeTeamName;
        AwayTeamName = awayTeamName;
        HomeTeamScore = homeTeamScore;
        AwayTeamScore = awayTeamScore;
    }


    public String getHomeTeamName() {
        return HomeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        HomeTeamName = homeTeamName;
    }

    public String getAwayTeamName() {
        return AwayTeamName;
    }

    public void setAwayTeamName(String awayTeamName) {
        AwayTeamName = awayTeamName;
    }

    public String getHomeTeamScore() {
        return HomeTeamScore;
    }

    public void setHomeTeamScore(String homeTeamScore) {
        HomeTeamScore = homeTeamScore;
    }

    public String getAwayTeamScore() {
        return AwayTeamScore;
    }

    public void setAwayTeamScore(String awayTeamScore) {
        AwayTeamScore = awayTeamScore;
    }
}
