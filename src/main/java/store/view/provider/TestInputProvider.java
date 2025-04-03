package store.view.provider;

import java.text.Normalizer;
import store.view.provider.interfaces.InputProvider;

public class TestInputProvider implements InputProvider {
    String purchaseLine;
    String[] YorN;
    int index;

    public TestInputProvider(String purchaseLine, String[] YorN){
        this.purchaseLine = Normalizer.normalize(purchaseLine, Normalizer.Form.NFC);
        this.YorN = YorN;
        index = 0;
    }

    @Override
    public String readNormal(){
        return purchaseLine;
    }

    @Override
    public String readYorN(){
        if(index == YorN.length) index = 0;
        String target = YorN[index++];
        return target;
    }

    //for debugging
    @Override
    public String toString(){
        return "MockInput";
    }
}
