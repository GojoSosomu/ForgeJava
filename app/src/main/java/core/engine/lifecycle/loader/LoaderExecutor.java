package core.engine.lifecycle.loader;

import java.util.Map;

import core.manager.loader.LoadType;
import core.manager.loader.LoadingRequest;
import core.manager.loader.order.LoadingOrder;
import core.model.snapshot.loader.LoadingSnapshot;
import infrastructure.event.pulse.Pulse;
import infrastructure.importer.DataSizer;

public class LoaderExecutor {
    private Map<LoadType, Loader<?>> loaders;
    private LoadingOrder order;
    private Pulse<LoadingSnapshot> loadingReceiver;
    private DataSizer sizer;

    public LoaderExecutor(
        Map<LoadType, Loader<?>> loaders,
        LoadingOrder order, 
        Pulse<LoadingSnapshot> loadingReceiver,
        DataSizer sizer
    ) {
        this.loaders = loaders;
        this.order = order;
        this.loadingReceiver = loadingReceiver;
        this.sizer = sizer;
    }

    public void execute() {
        int total = order.orders().stream()
            .mapToInt(request -> sizer.size(request.path()))
            .sum();
        
        LoadingSnapshot snapshot = new LoadingSnapshot(
            total,
            0,
            "Booted..."
        );
        loadingReceiver.onPulse(snapshot);
        for(LoadingRequest request : order.orders()) {
            Loader<?> loader = loaders.get(request.type());
            
            snapshot = loader.load(request, loadingReceiver, snapshot);
        }
        snapshot = snapshot.withName("Ended");
        loadingReceiver.onPulse(snapshot);
    }
}
