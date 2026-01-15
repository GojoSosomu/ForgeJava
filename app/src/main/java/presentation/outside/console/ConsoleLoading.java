package presentation.outside.console;

import core.model.view.loader.LoadingView;
import presentation.outside.channel.OutsideChannel;

public class ConsoleLoading implements OutsideChannel<LoadingView> {
    private int lastLines = 0;

    @Override
    public void render(LoadingView view) {
        clearLines();

        String output = build(view);
        System.out.print(output);

        lastLines = output.split("\n").length;
    }

    private String build(LoadingView view) {
        return
            progressBar(view)
            + "\nLoading: " + view.currentTask() + "\n \n";
    }

    private String progressBar(LoadingView view) {
        int total = 40;
        int progress = view.percentage() * total / 100;
        int antiProgress = total - progress;

        return
            "[\u001B[32m" + "=".repeat(progress) + "-".repeat(antiProgress) + "\u001B[0m] " + view.percentage() + "%\n";
    }

    private void clearLines() {
        for(int i = 0; i < lastLines; i++) {
            System.out.print("\033[1A");
            System.out.print("\033[2K");
        }
    }
}
