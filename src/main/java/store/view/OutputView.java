package store.view;

import java.util.List;
import store.utility.SystemConstantVariable;
import store.utility.SystemMessage;

public class OutputView {
    private static final String BULLETIN = "- ";

    public void printGreeting(){
        System.out.printf(SystemMessage.GREETING.getMessage() + "%n", SystemConstantVariable.STORE_NAME);
        lineBreak();
    }
    public void printCurrentStock(List<String> currentStock){
        currentStock.forEach(stockLine -> System.out.println(BULLETIN+stockLine));
        lineBreak();
    }

    public void printReceipt(String receipt){
        System.out.printf(receipt);
        lineBreak();
    }

    public void printError(String error){
        System.out.println(error);
    }

    public void lineBreak(){
        System.out.println("\n");
    }
}
