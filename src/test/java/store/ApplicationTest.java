package store;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import store.Application.Breakpoint;
import store.controller.StoreController;
import store.view.InputView;
import store.view.provider.TestInputProvider;

import static camp.nextstep.edu.missionutils.test.Assertions.assertNowTest;
import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest extends NsTest {
    AppConfig appConfig = new AppConfig();
    @Test
    void 파일에_있는_상품_목록_출력() {
        assertNowTest(() -> {
            String[] YorN = new String[]{"N","N"};
            InputView mockInputView = InputView.createByProvider(new TestInputProvider("[물-1]", YorN));
            appConfig.mock.InputView.inject(mockInputView);
            runTest(Breakpoint.PRODUCT_GUIDE);
            assertThat(output()).contains(
                "- 콜라 1,000원 10개 탄산2+1",
                "- 콜라 1,000원 10개",
                "- 사이다 1,000원 8개 탄산2+1",
                "- 사이다 1,000원 7개",
                "- 오렌지주스 1,800원 9개 MD추천상품",
                "- 오렌지주스 1,800원 재고 없음",
                "- 탄산수 1,200원 5개 탄산2+1",
                "- 탄산수 1,200원 재고 없음",
                "- 물 500원 10개",
                "- 비타민워터 1,500원 6개",
                "- 감자칩 1,500원 5개 반짝할인",
                "- 감자칩 1,500원 5개",
                "- 초코바 1,200원 5개 MD추천상품",
                "- 초코바 1,200원 5개",
                "- 에너지바 2,000원 5개",
                "- 정식도시락 6,400원 8개",
                "- 컵라면 1,700원 1개 MD추천상품",
                "- 컵라면 1,700원 10개"
            );
        },LocalDate.of(2024, 11, 2).atStartOfDay());
    }

    @Test
    void 여러_개의_일반_상품_구매() {
        assertSimpleTest(() -> {
            String[] YorN = new String[]{"N","N"};
            InputView mockInputView = InputView.createByProvider(new TestInputProvider("[비타민워터-3],[물-2],[정식도시락-2]", YorN));
            appConfig.mock.InputView.inject(mockInputView);
            runTest(Breakpoint.NONE);
            assertThat(output().replaceAll("\\s", "")).contains("내실돈18,300");
        });
    }

    @Test
    void 기간에_해당하지_않는_프로모션_적용() {
        assertNowTest(() -> {
            String[] YorN = new String[]{"N","N"};
            InputView mockInputView = InputView.createByProvider(new TestInputProvider("[감자칩-2]", YorN));
            appConfig.mock.InputView.inject(mockInputView);
            runTest(Breakpoint.NONE);
            assertThat(output().replaceAll("\\s", "")).contains("내실돈3,000");
        }, LocalDate.of(2024, 2, 1).atStartOfDay());
    }

    @Test
    void 예외_테스트() {
        assertSimpleTest(() -> {
            String[] YorN = new String[]{"N","N"};
            InputView mockInputView = InputView.createByProvider(new TestInputProvider("[컵라면-12]", YorN));
            appConfig.mock.InputView.inject(mockInputView);
            runTest(Breakpoint.FIRST_PURCHASE_ATTEMPT);
            assertThat(output()).contains("[ERROR] 입력한 상품 개수 12개는 재고를 초과해 구매할 수 없습니다.");
        });
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }

    private void runTest(Breakpoint breakpoint){
        new Application().testRun(appConfig, breakpoint);
    }
}
