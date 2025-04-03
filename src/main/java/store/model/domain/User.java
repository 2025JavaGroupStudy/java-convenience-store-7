package store.model.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.model.domain.Membership.Type;
import store.utility.ErrorMessage;
import store.utility.Pair;

public class User {
    Receipt receipt;
    Membership membership;
    boolean useMembership;
    boolean receiptAvailable;

    public User(){
        membership = Type.NORMAL.create();
        receipt = Receipt.makeReceipt();
        receiptAvailable = false;
        useMembership = false;
    }

    public void useMembership(boolean membership) {
        this.useMembership = membership;
    }

    public void calculateReceipt(){
        receipt.setTotalStockPrice();
        receipt.setTotalPromotionPrice();

        int membershipDiscount = 0;
        if(useMembership) {
            membershipDiscount = membership.discount(receipt.getTotalStockPriceWithoutPromotion());
        }
        receipt.setTotalMembershipPrice(membershipDiscount);
        receipt.setFinalPayPrice();

        receiptAvailable = true;
    }

    public String getReceiptText(){
        if(receiptAvailable){
            return receipt.toString();
        }
        throw new IllegalStateException(
                String.format(ErrorMessage.ACCESS_BEFORE_INITIALIZATION.getMessage(),
                        "required receipt's values", "getReceiptText"));
    }

    public void resetReceipt(){
        receipt = null;
        receipt = Receipt.makeReceipt();
    }

    public ReceiptProxy accessReceipt(){
        return new ReceiptProxy(receipt);
    }

    public static class ReceiptProxy {
        private final Receipt receipt;
        private ReceiptProxy(Receipt receipt) {
            this.receipt = receipt;
        }

        public void addPurchasedStock(Stock stock){
            receipt.addPurchasedStock(stock);
        }

        public void addPromotionStock(Stock stock){
            receipt.addPromotionStock(stock);
        }
    }
}
