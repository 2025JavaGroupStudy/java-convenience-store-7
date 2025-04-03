package store.utility;

public enum ErrorMessage {
    INPUT_NOT_FORMAT("%s가 아닌 다른 형식의 문자열이 입력되었습니다."),
    RESOURCE_COLUMN_WRONG("모델의 필드명과 리소스 내의 컬럼명 %s이 일치하지 않습니다."),
    RESOURCE_RANGE_WRONG("리소스 항목 %d은 정해진 규칙 %s의 범위를 초과합니다."),
    RESOURCE_LENGTH_WRONG("리소스 항목 리스트 길이가 모델의 필드 길이 %d와 일치하지 않습니다."),
    RESOURCE_NO_SUCH_INSTANCE("%s에 해당하는 명칭의 인스턴스가 존재하지 않습니다."),
    PROMOTION_DATE_OVERLAP("같은 명칭의 두 프로모션의 날짜가 겹칩니다."),
    PARAMETER_NOT_ACCEPTABLE("입력된 %s가 함수의 요구조건 %s에 어긋납니다."),
    ACCESS_BEFORE_INITIALIZATION("변수 %s가 초기화 전 함수 %s를 호출했습니다."),
    PURCHASE_NOT_ABLE("입력한 상품 개수 %d개는 재고를 초과해 구매할 수 없습니다."),;

    public static final String errorHeader = "[ERROR] ";
    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
