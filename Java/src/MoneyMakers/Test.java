package MoneyMakers;

import java.io.IOException;
import java.text.ParseException;

public class Test {

    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("Start extracting");
        Orchestrator orchestrator = new Orchestrator();
        orchestrator.start();
        System.out.println("Done");
    }

}