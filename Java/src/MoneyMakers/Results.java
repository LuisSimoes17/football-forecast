package MoneyMakers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Results {

    private String Division;
    private String season;
    private int HomeTeamId;
    private int AwayTeamId;
    private String HomeTeamName;
    private String AwayTeamName;
    private Date GameDate;
    private String Full_Time_Home_Team_Goals;
    private String Full_Time_Away_Team_Goals;
    private String Full_Time_Result;
    private String Market_average_home_win_odds;
    private String Market_average_draw_win_odds;
    private String Market_average_away_win_odds;
    private String Market_average_over_2_5_goals;
    private String Market_average_under_2_5_goals;

    public Results(String division, String season, String homeTeamName, String awayTeamName, String gameDate, String full_Time_Home_Team_Goals, String full_Time_Away_Team_Goals, String full_Time_Result, String market_average_home_win_odds, String market_average_draw_win_odds, String market_average_away_win_odds, String market_average_over_2_5_goals, String market_average_under_2_5_goals) throws ParseException {
      /*  HomeTeamId = homeTeamId;
        AwayTeamId = awayTeamId;*/
        this.season = season;
        Division = division;
        HomeTeamName = homeTeamName;
        AwayTeamName = awayTeamName;
        Full_Time_Home_Team_Goals = full_Time_Home_Team_Goals;
        Full_Time_Away_Team_Goals = full_Time_Away_Team_Goals;
        Full_Time_Result = full_Time_Result;
        Market_average_home_win_odds = market_average_home_win_odds;
        Market_average_draw_win_odds = market_average_draw_win_odds;
        Market_average_away_win_odds = market_average_away_win_odds;
        Market_average_over_2_5_goals = market_average_over_2_5_goals;
        Market_average_under_2_5_goals = market_average_under_2_5_goals;
        String[] dateTmp = gameDate.split("/");
        String day = dateTmp[0];
        String month = dateTmp[1];
        String year = "";
        if(dateTmp[2].length() == 2)
            year = "20" + dateTmp[2];
        else
            year = dateTmp[2];
        String gameDateHammer = day + "/" + month + "/" + year;
        if(!gameDateHammer.isEmpty())
            GameDate = new SimpleDateFormat("dd/MM/yyyy").parse(gameDateHammer);
    }

    public Results(String division, String homeTeamName, String awayTeamName, String gameDate, String full_Time_Home_Team_Goals, String full_Time_Away_Team_Goals, String full_Time_Result, String market_average_home_win_odds, String market_average_draw_win_odds, String market_average_away_win_odds, String market_average_over_2_5_goals, String market_average_under_2_5_goals) throws ParseException {
      /*  HomeTeamId = homeTeamId;
        AwayTeamId = awayTeamId;*/
        Division = division;
        HomeTeamName = homeTeamName;
        AwayTeamName = awayTeamName;
        Full_Time_Home_Team_Goals = full_Time_Home_Team_Goals;
        Full_Time_Away_Team_Goals = full_Time_Away_Team_Goals;
        Full_Time_Result = full_Time_Result;
        Market_average_home_win_odds = market_average_home_win_odds;
        Market_average_draw_win_odds = market_average_draw_win_odds;
        Market_average_away_win_odds = market_average_away_win_odds;
        Market_average_over_2_5_goals = market_average_over_2_5_goals;
        Market_average_under_2_5_goals = market_average_under_2_5_goals;
        String[] dateTmp = gameDate.split("/");
        String day = dateTmp[0];
        String month = dateTmp[1];
        String year = "";
        if(dateTmp[2].length() == 2)
            year = "20" + dateTmp[2];
        else
            year = dateTmp[2];
        String gameDateHammer = day + "/" + month + "/" + year;
        if(!gameDateHammer.isEmpty())
            GameDate = new SimpleDateFormat("dd/MM/yyyy").parse(gameDateHammer);
    }

    public Results(String homeTeamName, String awayTeamName, String homeTeamScore, String awayTeamScore, String gameDate) throws ParseException {
        HomeTeamName = homeTeamName;
        AwayTeamName = awayTeamName;
        String[] dateTmp = gameDate.split("/");
       // if(dateTmp.length == 1)
          //  System.out.println(gameDate);
        String day = dateTmp[0];
        String month = dateTmp[1];
        String year = "";
        if(dateTmp[2].length() == 2)
            year = "20" + dateTmp[2];
        else
            year = dateTmp[2];
        String gameDateHammer = day + "/" + month + "/" + year;
        if(!gameDateHammer.isEmpty())
            GameDate = new SimpleDateFormat("dd/MM/yyyy").parse(gameDateHammer);
    }

    public Results(int HomeTeamId,int AwayTeamId, String homeTeamName, String awayTeamName, String homeTeamScore, String awayTeamScore) {
        HomeTeamId = HomeTeamId;
        AwayTeamId = AwayTeamId;
        HomeTeamName = homeTeamName;
        AwayTeamName = awayTeamName;
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

    public Date getGameDate() {
        return GameDate;
    }

    public void setGameDate(String gameDate) throws ParseException {
        GameDate = new SimpleDateFormat("dd/MM/yyyy").parse(gameDate);;
    }
    public void setGameDate(Date gameDate) {
        GameDate = gameDate;
    }

    public String getFull_Time_Home_Team_Goals() {
        return Full_Time_Home_Team_Goals;
    }

    public void setFull_Time_Home_Team_Goals(String full_Time_Home_Team_Goals) {
        Full_Time_Home_Team_Goals = full_Time_Home_Team_Goals;
    }

    public String getFull_Time_Away_Team_Goals() {
        return Full_Time_Away_Team_Goals;
    }

    public void setFull_Time_Away_Team_Goals(String full_Time_Away_Team_Goals) {
        Full_Time_Away_Team_Goals = full_Time_Away_Team_Goals;
    }

    public String getFull_Time_Result() {
        return Full_Time_Result;
    }

    public void setFull_Time_Result(String full_Time_Result) {
        Full_Time_Result = full_Time_Result;
    }

    public String getMarket_average_home_win_odds() {
        return Market_average_home_win_odds;
    }

    public void setMarket_average_home_win_odds(String market_average_home_win_odds) {
        Market_average_home_win_odds = market_average_home_win_odds;
    }

    public String getMarket_average_draw_win_odds() {
        return Market_average_draw_win_odds;
    }

    public void setMarket_average_draw_win_odds(String market_average_draw_win_odds) {
        Market_average_draw_win_odds = market_average_draw_win_odds;
    }

    public String getMarket_average_away_win_odds() {
        return Market_average_away_win_odds;
    }

    public void setMarket_average_away_win_odds(String market_average_away_win_odds) {
        Market_average_away_win_odds = market_average_away_win_odds;
    }

    public String getMarket_average_over_2_5_goals() {
        return Market_average_over_2_5_goals;
    }

    public void setMarket_average_over_2_5_goals(String market_average_over_2_5_goals) {
        Market_average_over_2_5_goals = market_average_over_2_5_goals;
    }

    public String getMarket_average_under_2_5_goals() {
        return Market_average_under_2_5_goals;
    }

    public void setMarket_average_under_2_5_goals(String market_average_under_2_5_goals) {
        Market_average_under_2_5_goals = market_average_under_2_5_goals;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getDivision() {
        return Division;
    }

    public void setDivision(String division) {
        Division = division;
    }
    public String getGameDateFormated() {
        if(GameDate != null){
        //System.out.println("BATATA " + getAwayTeamScore() + getHomeTeamScore() +getAwayTeamName() +getHomeTeamName());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(this.GameDate);
        }
        else
            return "00/00/0000";
    }
}
