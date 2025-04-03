package store.view;

import static store.utility.SystemConstantVariable.INPUT_EXAMPLES;
import store.utility.InputFormatter;
import store.utility.SystemMessage;
import store.view.provider.DefaultInputProvider;
import store.view.provider.interfaces.InputProvider;

public class InputView{
    InputProvider inputProvider;

    public InputView(){ inputProvider = new DefaultInputProvider(); }
    public InputView(InputProvider inputProvider){ this.inputProvider = inputProvider;}

    public static InputView createByProvider(InputProvider inputProvider){
        return new InputView(inputProvider);
    }

    public String readPurchaseItems(){
        System.out.printf(SystemMessage.PURCHASE_GUIDE.getMessage() + "%n", InputFormatter.stockFormat(INPUT_EXAMPLES));
        return inputProvider.readNormal();
    }

    public String readIfRegularPricePurchase(String productName, int stockAmount){
        System.out.printf(SystemMessage.NO_PROMOTION_GUIDE.getMessage() + "%n", productName, stockAmount);
        return inputProvider.readYorN();
    }

    public String readIfAdditionalPurchase(String productName, int stockAmount){
        System.out.printf(SystemMessage.YES_PROMOTION_GUIDE.getMessage() + "%n", productName, stockAmount);
        return inputProvider.readYorN();
    }

    public String readIfMembershipDiscount(){
        System.out.println(SystemMessage.MEMBERSHIP_GUIDE.getMessage());
        return inputProvider.readYorN();
    }

    public String readDismissal(){
        System.out.println(SystemMessage.DISMISSAL.getMessage());
        return inputProvider.readYorN();
    }

    //for debugging
//    public String myIdentity(){
//        return inputProvider.toString();
//    }
}
