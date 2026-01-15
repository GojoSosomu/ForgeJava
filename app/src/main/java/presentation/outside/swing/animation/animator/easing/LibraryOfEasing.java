package presentation.outside.swing.animation.animator.easing;

public class LibraryOfEasing {
    public final static Easing LINEAR = t -> t;
    public final static Easing EASING_IN = t -> t * (2 - t);
    public final static Easing EASING_OUT = t -> t * t;
    public final static Easing EASING_IN_AND_OUT = t -> t < 0.5f ? 2 * t * t : -1 + (4 - 2 * t) * t;
    public final static Easing SMOOTH_STEP = t -> t * t * (3 - 2 * t);
}
