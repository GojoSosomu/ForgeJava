package presentation.outside.swing.animation;

import javax.swing.*;

import presentation.outside.swing.animation.animator.SwingAnimator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SwingAnimationRunner {

    private Timer timer;
    private List<SwingAnimator> animators = new ArrayList<>();

    public SwingAnimationRunner(int fps) {
        int delay = 1000 / fps;
        this.timer = new Timer(delay, e -> tick());
    }

    public void add(SwingAnimator animator) {
        animators.add(animator);
        animator.start();
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    private void tick() {
        for (Iterator<SwingAnimator> it = animators.iterator(); it.hasNext();) {
            SwingAnimator animator = it.next();
            animator.animate();

            if (animator.isFinished()) {
                animator.stop();
                it.remove();
            }
        }
    }
}
