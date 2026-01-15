package core.manager.loader.order;

import java.util.List;

import core.manager.loader.LoadingRequest;

public interface LoadingOrder {
    List<LoadingRequest> orders();
}
