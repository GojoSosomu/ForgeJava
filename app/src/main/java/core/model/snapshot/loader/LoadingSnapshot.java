package core.model.snapshot.loader;

import core.model.snapshot.Snapshot;

public record LoadingSnapshot(
    int total,
    int progress,
    String name
) implements Snapshot{
    
    public int percentage() {
        if(total == 0) return 100;
        return (int)((progress * 100) / (double)total);
    }

    public LoadingSnapshot withProgress(int newProgress) {
        return new LoadingSnapshot(this.total, newProgress, this.name);
    }

    public LoadingSnapshot withName(String newName) {
        return new LoadingSnapshot(this.total, this.progress, newName);
    }
}
