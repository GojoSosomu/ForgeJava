package presentation.outside.swing.animation.animator.easing;

public class SmoothStepEasing implements Easing {

    @Override
    public double apply(double t) {
        return t * t * (3 - 2 * t);
    }
}
