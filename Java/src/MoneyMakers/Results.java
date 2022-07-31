package MoneyMakers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Results {
    private int HomeTeamId;
    private int AwayTeamId;
    private String HomeTeamName;
    private String AwayTeamName;
    private String HomeTeamScore;
    private String AwayTeamScore;
    private Date GameDate;


    public Results(String homeTeamName, String awayTeamName, String homeTeamScore, String awayTeamScore,String gameDate) throws ParseException {
        HomeTeamName = homeTeamName;
        AwayTeamName = awayTeamName;
        HomeTeamScore = homeTeamScore;
        AwayTeamScore = awayTeamScore;
        GameDate = new SimpleDateFormat("dd/MM/yyyy").parse(gameDate);  ;
    }

    public Results(int HomeTeamId,int AwayTeamId, String homeTeamName, String awayTeamName, String homeTeamScore, String awayTeamScore) {
        HomeTeamId = HomeTeamId;
        AwayTeamId = AwayTeamId;
        HomeTeamName = homeTeamName;
        AwayTeamName = awayTeamName;
        HomeTeamScore = homeTeamScore;
        AwayTeamScore = awayTeamScore;
    }

    public int getHomeTeamId() {
        return HomeTeamId;
    }

    public void setHomeTeamId(int homeTeamId) {
        HomeTeamId = homeTeamId;
    }

    public int getAwayTeamId() {
        return AwayTeamId;
    }

    public void setAwayTeamId(int awayTeamId) {
        AwayTeamId = awayTeamId;
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

    public Date getGameDate() {
        return GameDate;
    }

    public void setGameDate(String gameDate) throws ParseException {
        GameDate = new SimpleDateFormat("dd/MM/yyyy").parse(gameDate);;
    }

    public String getGameDateFormated() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(this.GameDate);
    }
}
