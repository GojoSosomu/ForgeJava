package presentation.outside.swing.animation.animator;

import javax.swing.JComponent;

import presentation.outside.swing.animation.animator.easing.Easing;
import presentation.outside.swing.animation.animator.easing.LibraryOfEasing;

public abstract class SwingAnimator {
    protected JComponent component;
    protected long durationMs;
    protected long startTime;
    protected boolean finished;
    protected Easing easing = LibraryOfEasing.LINEAR;

    public SwingAnimator(
        JComponent component,
        long durationMs,
        Easing easing
    ) {
        this.component = component;
        this.durationMs = durationMs;
        this.easing = easing;
    }

    public abstract void start();
    public abstract void stop();
    public abstract boolean isFinished();
    public abstract void animate();

    protected int lerp(int a, int b, double t) {
        return Math.round(a + (b - a) * (float)t);
    }

    protected double progress() {
        long now = System.currentTimeMillis();
        return Math.min((now - startTime) / (float) durationMs, 1f);
    }
}
