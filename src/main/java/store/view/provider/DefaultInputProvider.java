package store.view.provider;

import static store.utility.SystemConstantVariable.INPUT_EXAMPLES;

import camp.nextstep.edu.missionutils.Console;
import store.utility.InputFormatter;
import store.utility.SystemMessage;
import store.view.provider.interfaces.InputProvider;

public class DefaultInputProvider implements InputProvider {
    @Override
    public String readNormal(){
        return Console.readLine();
    }

    @Override
    public String readYorN(){
        String input = Console.readLine();
        return InputFormatter.isConfirmRejectFormat(input);
    }

    //for debugging
    @Override
    public String toString(){
        return "DefaultInput";
    }
}
