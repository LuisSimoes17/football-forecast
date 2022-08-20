package MoneyMakers;

public class LeagueIds {


    public LeagueIds() {
    }

    public int getLeagueIds(String name){

        switch (name) {
            case "Belgium_Jupiler":
                return 144;
            case "England_PremierLeague":
                return 39;
            case "England_Championship":
                return 40;
            case "England_League1":
                return 0;
            case "England_League2":
                return 0;
            case "England_Conference":
                return 0;
            case "France_Ligue1":
                return 61;
            case "France_Ligue2":
                return 62;
            case "Germany_Bundesliga1":
                return 78;
            case "Germany_Bundesliga2":
                return 79;
            case "Greece_SuperLeague":
                return 197;
            case "Italy_SerieA":
                return 135;
            case "Italy_SerieB":
                return 136;
            case "Netherlands_Eredivisie":
                return 88;
            case "Portugal_PrimeiraLiga":
                return 94;
            case "Spain_Laliga":
                return 140;
            case "Spain_Laliga2":
                return 141;
            case "Turkey_SuperLeague":
                return 203;
            default:
                return 0;
        }
    }
}
