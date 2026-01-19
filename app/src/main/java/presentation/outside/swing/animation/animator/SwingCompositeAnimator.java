package presentation.outside.swing.animation.animator;

import java.util.List;

public final class SwingCompositeAnimator extends SwingAnimator {

    private List<SwingAnimator> animators;

    public SwingCompositeAnimator(List<SwingAnimator> animators) {
        this.animators = animators;
    }

    @Override
    public void start() {
        for(SwingAnimator animator : animators) animator.start();
        finished = true;
    }

    @Override
    public void stop() {
        for(SwingAnimator animator : animators) animator.stop();
    }

    @Override
    public boolean isFinished() {
        if(!finished) return false;
        for(SwingAnimator animator : animators) 
            if(!animator.isFinished())return false;

        return true;
    }

    @Override
    public void animate() {
        if(!finished) return;
        for(SwingAnimator animator : animators) animator.animate();
    }

}
