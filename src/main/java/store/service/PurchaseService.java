package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import store.model.domain.User;
import store.model.dto.CheckPromotionDTO;
import store.model.domain.Promotion;
import store.model.domain.Stock;
import store.service.RepositoryService.isItemType;
import store.utility.ErrorMessage;
import store.utility.Pair;

public class PurchaseService {
    UserService userService;
    RepositoryService repositoryService;

    public PurchaseService(UserService userService, RepositoryService repositoryService){
        this.userService = userService;
        this.repositoryService = repositoryService;
    }

    public void confirmPurchaseToReceipt(String userKey, Stock promotionStock, Stock totalStock){
        User user = userService.retrieveUser(userKey);
        user.accessReceipt().addPurchasedStock(totalStock);
        user.accessReceipt().addPromotionStock(promotionStock);
    }

    public void useMembership(String userKey, boolean isMembership){
        userService.retrieveUser(userKey).useMembership(isMembership);
    }

    //이건 조건을 다 뺴지 않는 이상 줄이기가 안되드라구요...쩔수없이 이거대로 하기로
    public CheckPromotionDTO checkPromotionForCustomerInput(String userKey, String productName, int customerQuantity){
        Pair<Stock, Stock> stocks = (Pair<Stock, Stock>) repositoryService.isItem(isItemType.STOCK, productName);
        if(stocks == null) throw new IllegalArgumentException(String.format(ErrorMessage.RESOURCE_NO_SUCH_INSTANCE.getMessage(), productName));

        Stock basePromoStock = stocks.getFirst();
        Stock baseNormStock = stocks.getSecond();
        Promotion promotion;

        if(basePromoStock == null) { promotion = null; }
        else { promotion = basePromoStock.getPromotion(); }

        int normalQuantity = baseNormStock.getQuantity();

        // Case 1: No promotion -> Check only normal stock
        if (promotion == null || !promotion.isDatePromotionPeriod(DateTimes.now())) {
            return new CheckPromotionDTO(normalQuantity >= customerQuantity, false, false, baseNormStock, 0, customerQuantity, 0);
        }

        // Case 2: Promotion applies
        int totalMinusPromotion = promotion.getBuy() + promotion.getGet();
        int promotionQuantity = basePromoStock.getQuantity();

        // Case 2-1: Not enough promo stock → Check normal stock
        if (promotionQuantity < totalMinusPromotion) {
            return new CheckPromotionDTO(normalQuantity >= customerQuantity, true, false, basePromoStock, customerQuantity, customerQuantity, 0);
        }

        // Case 2-2: Customer bought fewer than promo stock → Handle promo suggestion
        if (customerQuantity < promotionQuantity) {
            int infoQuantity = customerQuantity % totalMinusPromotion;
            if (infoQuantity == totalMinusPromotion - 1) {
                return new CheckPromotionDTO(true, true, true, basePromoStock, 1, 0, customerQuantity);
            }
            if (infoQuantity != 0) {
                return new CheckPromotionDTO(normalQuantity >= infoQuantity, true, false, basePromoStock, infoQuantity, infoQuantity, customerQuantity - infoQuantity);
            }
        }

        // Case 2-3: Promotion stock isn't enough for full purchase → Partial promo, rest normal
        if (promotionQuantity != totalMinusPromotion && totalMinusPromotion != customerQuantity) {
            int infoQuantity = customerQuantity - promotionQuantity + promotionQuantity % totalMinusPromotion;
            return new CheckPromotionDTO(
                    normalQuantity >= infoQuantity && promotionQuantity > customerQuantity - infoQuantity,
                    true,
                    false,
                    basePromoStock,
                    infoQuantity,
                    infoQuantity,
                    customerQuantity - infoQuantity
            );
        }

        // Case 2-4: Default fallback when above conditions don't match
        return new CheckPromotionDTO(
                normalQuantity + promotionQuantity >= customerQuantity,
                true,
                false,
                basePromoStock,
                0,
                Math.min(customerQuantity, normalQuantity),
                Math.min(customerQuantity, promotionQuantity)
        );
    }

    public String getReceipt(String userKey){
        User user = userService.retrieveUser(userKey);
        user.calculateReceipt();
        return user.getReceiptText();
    }

    public void resetReceipt(String userKey){
        User user = userService.retrieveUser(userKey);
        user.resetReceipt();
    }


}
