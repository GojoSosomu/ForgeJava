package infrastructure.event.receiver;

import core.model.snapshot.loader.LoadingSnapshot;
import core.model.view.loader.LoadingView;
import infrastructure.event.pulse.Pulse;
import presentation.outside.channel.OutsideChannel;
import presentation.service.assembler.ViewAssembler;

public class LoadingReceiver implements Pulse<LoadingSnapshot> {
    private OutsideChannel<LoadingView> channel;
    private ViewAssembler<LoadingSnapshot, LoadingView> viewAssembler;

    public LoadingReceiver(
        OutsideChannel<LoadingView> channel,
        ViewAssembler<LoadingSnapshot, LoadingView> viewAssembler
    ) {
        this.channel = channel;
        this.viewAssembler = viewAssembler;
    }

    @Override
    public void onPulse(LoadingSnapshot Snapshot) {
        LoadingView view = viewAssembler.from(Snapshot);
        channel.render(view);
    }

}
