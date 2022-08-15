package MoneyMakers;

import java.io.IOException;
import java.text.ParseException;

public class Test {

    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("Start extracting");
        General general = new General();
        general.start();
        /*OrchestratorFromAPI orchestratorFromAPI = new OrchestratorFromAPI();
        orchestrator.start();*/
        System.out.println("Done");
    }

}