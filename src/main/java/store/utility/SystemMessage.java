package store.utility;

import static store.utility.SystemConstantVariable.INPUT_CONFIRM_STRING;
import static store.utility.SystemConstantVariable.INPUT_REJECT_STRING;

public enum SystemMessage {
    GREETING("안녕하세요. W편의점입니다.\n"
            + "현재 보유하고 있는 상품입니다."),
    PURCHASE_GUIDE("구매하실 상품명과 수량을 입력해 주세요. (예: %s)"),
    NO_PROMOTION_GUIDE("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? ("
                                                        + INPUT_CONFIRM_STRING +"/"+ INPUT_REJECT_STRING +")"),
    YES_PROMOTION_GUIDE("현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? ("
                                                         + INPUT_CONFIRM_STRING +"/"+ INPUT_REJECT_STRING +")"),
    MEMBERSHIP_GUIDE("멤버십 할인을 받으시겠습니까? (" + INPUT_CONFIRM_STRING +"/"+ INPUT_REJECT_STRING +")"),
    DISMISSAL("감사합니다. 구매하고 싶은 다른 상품이 있나요? (" + INPUT_CONFIRM_STRING +"/"+ INPUT_REJECT_STRING +")");

    private final String message;

    SystemMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
