package presentation.outside.swing.animation.animator;

import javax.swing.*;

import presentation.outside.swing.animation.animator.easing.Easing;

import java.awt.*;

public final class SwingSlideAnimator extends SwingAnimator {

    public interface SlideTarget {
        void setOffset(int x, int y);
        boolean isStillDisplayable();
    }

    private final SlideTarget target;
    private final Point from;
    private final Point to;

    public SwingSlideAnimator(
            JComponent parent,
            SlideTarget target,
            Point from,
            Point to,
            long durationMs,
            Easing easing
    ) {
        super(parent, durationMs, easing);
        this.target = target;
        this.from = from;
        this.to = to;
    }

    @Override
    public void start() {
        startTime = System.currentTimeMillis();
        target.setOffset(from.x, from.y);
        finished = false;
    }

    @Override
    public void animate() {
        if (!target.isStillDisplayable()) {
            finished = true;
            return;
        }

        double t = progress();
        double easingT = easing.apply(t);

        target.setOffset(
                lerp(from.x, to.x, easingT),
                lerp(from.y, to.y, easingT)
        );
        component.repaint();
        Toolkit.getDefaultToolkit().sync(); 
        
        if (t >= 1f) finished = true;
    }

    @Override
    public void stop() {
        target.setOffset(to.x, to.y);
        component.repaint();
        Toolkit.getDefaultToolkit().sync(); 
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}