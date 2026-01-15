package presentation.outside.swing.animation.animator.easing;

public class EasingInAndOut implements Easing {

    @Override
    public double apply(double t) {
        return t < 0.5f ? 2 * t * t : -1 + (4 - 2 * t) * t;
    }
}
