package core.engine.lifecycle.saver;

import java.util.Map;

import core.manager.saver.SaveType;
import core.manager.saver.SavingRequest;
import core.manager.saver.order.SavingOrder;

public class SaverExecutor {
    private Map<SaveType, Saver<?>> savers;
    private SavingOrder order;

    public SaverExecutor(
        Map<SaveType, Saver<?>> savers,
        SavingOrder order
    ) {
        this.savers = savers;
        this.order = order;
    }

    public void execute() {
        for(SavingRequest request : order.orders()) {
            Saver<?> saver = savers.get(request.type());
            
            saver.save(request);
        }
    }
}
