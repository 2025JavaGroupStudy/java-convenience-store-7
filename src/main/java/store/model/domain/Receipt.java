package store.model.domain;

import java.util.TreeMap;
import store.utility.ErrorMessage;
import store.utility.SystemConstantVariable;

public class Receipt {
    private static final String HEADLINE = "===========%s 편의점=============\n"
                                         + "상품명 수량 금액";
    private static final String MIDLINE = "===========증 정=============\n";
    private static final String ENDLINE = "==============================\n";

    // The Integer in the Map is for total price
    // TreeMap는 comparable 또는 comparator를 설정 시 alphabetical 정렬로 저장됨.
    private final TreeMap<Stock, Integer> purchasedStocks;
    private final TreeMap<Stock, Integer> promotionStocks;
    private int totalStockPrice;
    private int totalStockQuantity;
    private int totalPromotionPrice;
    private int totalMembershipPrice;
    private int finalPayPrice;

    private Receipt() {
        this.purchasedStocks = new TreeMap<>();
        this.promotionStocks = new TreeMap<>();
    }

    // Public method to get the single instance
    public static Receipt makeReceipt() {
        return new Receipt();
    }

    public void addPurchasedStock(Stock stock){
        purchasedStocks.put(stock, stock.getTotalPrice());
    }

    public void addPromotionStock(Stock stock){
        promotionStocks.put(stock, stock.getTotalPrice());
    }

    public void setTotalStockPrice(){
        totalStockPrice = purchasedStocks.values().stream().reduce(0, Integer::sum);
        totalStockQuantity = purchasedStocks.descendingKeySet().stream().mapToInt(Stock::getQuantity).sum();
    }

    public void setTotalPromotionPrice() {
        totalPromotionPrice = promotionStocks.values().stream().reduce(0, Integer::sum);
    }

    public void setTotalMembershipPrice(int price) {
        totalMembershipPrice = price;
    }

    public void setFinalPayPrice(){
        finalPayPrice = totalStockPrice - totalPromotionPrice - totalMembershipPrice;
    }

    public int getTotalStockPriceWithoutPromotion(){
        int result = totalStockPrice - totalPromotionPrice;
        if(result<=0) {
            throw new IllegalStateException(String.format(ErrorMessage.ACCESS_BEFORE_INITIALIZATION.getMessage(), "totalStockPrice", "getTotalStockPriceWithoutPromotion"));
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder receiptBuilder = new StringBuilder();

        generateTopReceipt(receiptBuilder);
        generateMidReceipt(receiptBuilder);
        generateEndReceipt(receiptBuilder);

        return receiptBuilder.toString();
    }

    private void generateTopReceipt(StringBuilder receiptBuilder){
        receiptBuilder.append(String.format(HEADLINE, SystemConstantVariable.STORE_NAME));
        purchasedStocks.descendingKeySet()
                .forEach(stock ->
                        receiptBuilder.append(stock.getProductName())
                                .append(" ")
                                .append(stock.getQuantity())
                                .append(" ")
                                .append(String.format("%,d", stock.getTotalPrice()))
                                .append("\n")
                );
    }

    private void generateMidReceipt(StringBuilder receiptBuilder){
        receiptBuilder.append(MIDLINE);
        promotionStocks.descendingKeySet().forEach(stock->
                receiptBuilder.append(stock.getProductName())
                        .append(" ")
                        .append(stock.getQuantity())
                        .append("\n")
        );
    }

    private void generateEndReceipt(StringBuilder receiptBuilder){
        receiptBuilder.append(ENDLINE)
                .append("총구매액").append(" ").append(totalStockQuantity).append(" ").append(String.format("%,d", totalStockPrice))
                .append("\n")
                .append("행사할인").append(" ").append(String.format("-%,d", totalPromotionPrice))
                .append("\n")
                .append("멤버십할인").append(" ").append(String.format("-%,d", totalMembershipPrice))
                .append("\n")
                .append("내실돈").append(" ").append(String.format("%,d", finalPayPrice))
                .append("\n");
    }
}