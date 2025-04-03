package store;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import store.controller.StoreController;

public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        AppConfig appConfig = new AppConfig();
        new Application().run(appConfig);
    }

    public void run(AppConfig appConfig){
        boolean repeatInput = true;
        boolean repeatProgram = true;
        StoreController storeController = appConfig.getStoreController();
        String accessingUserID = storeController.initializing();

        while(repeatProgram){
            storeController.startPurchase();
            while(repeatInput){
                repeatInput = storeController.purchasing(accessingUserID);
            }

            repeatInput = true;

            while(repeatInput){
                int result = storeController.endPurchase(accessingUserID);
                if(result < 1) repeatInput = false;
                if(result < 0) repeatProgram = false;
            }
            repeatInput = true;
        }
        storeController.disconnectUser(accessingUserID);
    }

    public void testRun(AppConfig appConfig, Breakpoint breakpoint){
        boolean repeatInput = true;
        boolean repeatProgram = true;
        StoreController storeController = appConfig.getStoreController();
        String accessingUserID = storeController.initializing();

        while(repeatProgram){
            storeController.startPurchase();
            if(breakpoint==Breakpoint.PRODUCT_GUIDE) break;
            while(repeatInput){
                repeatInput = storeController.purchasing(accessingUserID);
                if(breakpoint==Breakpoint.FIRST_PURCHASE_ATTEMPT) break;
            }

            if(breakpoint==Breakpoint.FIRST_PURCHASE_ATTEMPT) break;
            repeatInput = true;

            while(repeatInput){
                int result = storeController.endPurchase(accessingUserID);
                if(result < 1) repeatInput = false;
                if(result < 0) repeatProgram = false;
            }
            repeatInput = true;
        }
        storeController.disconnectUser(accessingUserID);
    }

    public enum Breakpoint{
        PRODUCT_GUIDE,
        FIRST_PURCHASE_ATTEMPT,
        NONE
    }
}
