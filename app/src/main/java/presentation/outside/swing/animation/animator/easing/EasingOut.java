package presentation.outside.swing.animation.animator.easing;

public class EasingOut implements Easing {

    @Override
    public double apply(double t) {
        return t * (2 - t);
    }
}
