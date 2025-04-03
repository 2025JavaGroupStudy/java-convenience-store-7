package store;

import java.util.function.Consumer;
import store.controller.StoreController;
import store.model.repository.ProductRepository;
import store.model.repository.PromotionRepository;
import store.model.repository.StockRepository;
import store.model.repository.UserRepository;
import store.service.PurchaseService;
import store.service.RepositoryService;
import store.service.StoreInitializeService;
import store.service.UserService;
import store.utility.ErrorMessage;
import store.view.InputView;
import store.view.OutputView;

public class AppConfig {
    private ProductRepository productRepository;
    private PromotionRepository promotionRepository;
    private StockRepository stockRepository;
    private UserRepository userRepository;

    private RepositoryService repositoryService;
    private UserService userService;
    private StoreInitializeService storeInitializeService;
    private PurchaseService purchaseService;

    private InputView inputView;
    private OutputView outputView;

    private StoreController storeController;

    public MockSetting mock;

    public AppConfig(){
        setRepository();
        setService();
        setView();
        setContoller();

        mock = new MockSetting(this);
    }

    public StoreController getStoreController(){
        return storeController;
    }

    public static class MockSetting {
        public SingleVariable<InputView> InputView;
        public SingleVariable<ProductRepository> ProductRepository;
        public SingleVariable<PromotionRepository> PromotionRepository;
        public SingleVariable<StockRepository> StockRepository;

        public MockSetting(AppConfig parent){
            this.InputView = new SingleVariable<>(v -> parent.inputView = v, parent::setContoller);
            this.ProductRepository = new SingleVariable<>(v -> parent.productRepository = v, ()->{parent.setService(); parent.setContoller();});
            this.StockRepository = new SingleVariable<>(v -> parent.stockRepository = v, ()->{parent.setService(); parent.setContoller();});
            this.PromotionRepository = new SingleVariable<>(v -> parent.promotionRepository = v, ()->{parent.setService(); parent.setContoller();});
        }

        static class SingleVariable<T> implements Injectable {
            private final Consumer<T> setter;
            private final Refresh func;
            public SingleVariable(Consumer<T> setter, Refresh func){
                this.setter = setter;
                this.func = func;
            }
            @Override
            public void inject(Object mockedInstance){
                try{
                    setter.accept((T) mockedInstance);
                    func.refresh();
                } catch (Exception e){
                    throw new IllegalStateException(ErrorMessage.errorHeader+
                            String.format(ErrorMessage.PARAMETER_NOT_ACCEPTABLE.getMessage(),
                                    "입력된 타입의 mockInstance",setter.getClass().getName()));
                }
            }
        }
    }

    @FunctionalInterface
    interface Injectable {
        void inject(Object mockedInstance);
    }

    @FunctionalInterface
    interface Refresh{
        void refresh();
    }

    private void setRepository(){
        productRepository = new ProductRepository();
        promotionRepository = new PromotionRepository();
        stockRepository = new StockRepository();
        userRepository = new UserRepository();
    }

    private void setService(){
        repositoryService = new RepositoryService(promotionRepository, productRepository, stockRepository);
        userService = new UserService(userRepository);
        storeInitializeService = new StoreInitializeService(repositoryService);
        purchaseService = new PurchaseService(userService, repositoryService);
    }

    private void setView(){
        inputView = new InputView();
        outputView = new OutputView();
    }

    private void setContoller(){
        storeController = new StoreController(inputView, outputView, storeInitializeService, purchaseService, repositoryService, userService);
    }

    //for debugging
//    public void myInputView(){
//        System.out.println("Injected InputView: " + inputView.myIdentity());
//    }
}
