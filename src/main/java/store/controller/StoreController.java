package store.controller;

import java.util.List;
import java.util.Objects;
import store.model.domain.Stock;
import store.model.dto.CheckPromotionDTO;
import store.model.dto.CheckPromotionDTO.Type;
import store.service.PurchaseService;
import store.service.RepositoryService;
import store.service.StoreInitializeService;
import store.service.UserService;
import store.utility.ErrorMessage;
import store.utility.InputFormatter;
import store.utility.Parser;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    InputView inputView;
    OutputView outputView;
    StoreInitializeService storeInitializeService;
    PurchaseService purchaseService;
    RepositoryService repositoryService;
    UserService userService;

    public StoreController(InputView inputView, OutputView outputView,
                           StoreInitializeService storeInitializeService,
                           PurchaseService purchaseService,
                           RepositoryService repositoryService, UserService userService){
        this.inputView = inputView;
        this.outputView = outputView;
        this.storeInitializeService = storeInitializeService;
        this.purchaseService = purchaseService;
        this.repositoryService = repositoryService;
        this.userService = userService;
    }

    public String initializing(){
        try{
            storeInitializeService.setPromotions();
            storeInitializeService.setProductsAndStocks();
            return userService.createUser();
        }catch (Exception e){
            outputView.printError(ErrorMessage.errorHeader + e.getMessage());
            throw e;
        }
    }

    public void startPurchase(){
        outputView.printGreeting();
        List<String> currentStock = repositoryService.getCurrentStockStringList();
        outputView.printCurrentStock(currentStock);
    }

    public boolean purchasing(String userKey){
        try{
            String inputLine = inputView.readPurchaseItems();
            String[] splittedInput = InputFormatter.isStockFormat(inputLine);
            purchaseEachItem(userKey, splittedInput);
            outputView.printError("purchasing item executed");
            return false;
        }catch (Exception e){
            outputView.printError(ErrorMessage.errorHeader +  e.getMessage());
            return true;
        }
    }

    public int endPurchase(String userKey){
        try{
            String inputLine = inputView.readIfMembershipDiscount();
            purchaseService.useMembership(userKey, Objects.equals(inputLine, "Y"));
            String receipt = purchaseService.getReceipt(userKey);
            outputView.printReceipt(receipt);
            purchaseService.resetReceipt(userKey);
            inputLine = inputView.readDismissal();
            if(Objects.equals(inputLine, "N")) return -1;
            return 0;
        }catch (Exception e){
            outputView.printError(ErrorMessage.errorHeader + e.getMessage());
            return 1;
        }
    }

    public void disconnectUser(String userKey){
        userService.removeUser(userKey);
    }




    //------------------------------------------------------------------------------------------------//
    // private methods from here

    //정가 질문에 N 답할 시 정가 상품만 구매하지 않는 것으로 이해하고 코드 짰습니다.
    private CheckPromotionDTO readAccordingToCheckPromotionDTO(CheckPromotionDTO result, int customerQuantity) {
        if (!result.isPurchasable()) throw new IllegalArgumentException(String.format(ErrorMessage.PURCHASE_NOT_ABLE.getMessage(), customerQuantity));
        String productName = result.getProduct().getName();
        if (result.isRelatedStock() && result.isAdditionSuggest()) {
            String input = inputView.readIfAdditionalPurchase(productName, result.getInfoQuantity());
            if(Objects.equals(input, "N")){ result.changeQuantity(Type.PROMOTION,-1*result.getInfoQuantity());}
        }
        if (result.isRelatedStock() && result.getInfoQuantity() != 0) {
            String input = inputView.readIfRegularPricePurchase(productName, result.getInfoQuantity());
            if(Objects.equals(input, "N")){ result.changeQuantity(Type.NORMAL,-1*result.getInfoQuantity()); }
        }

        repositoryService.deductStock(productName, result.getPromotionQuantity(),result.getNormalQuantity());
        return result;
    }

    private void purchaseEachItem(String userKey, String[] splittedInput){
        for(int i=0; i<splittedInput.length/2; i++){
            int customerQuantity = Parser.NumberParse(splittedInput[i*2+1]);
            CheckPromotionDTO result = purchaseService.checkPromotionForCustomerInput(userKey, splittedInput[i*2], customerQuantity);
            result = readAccordingToCheckPromotionDTO(result, customerQuantity);
            purchaseService.confirmPurchaseToReceipt(userKey,
                    Stock.createStore(result.getProduct(), result.getPromotion(), result.getPromotionQuantity()),
                    Stock.createStore(result.getProduct(), result.getPromotion(), result.getNormalQuantity()+result.getPromotionQuantity())
            );
        }
    }
}
