package presentation.outside.swing.animation.animator.easing;

public class LibraryOfEasing {

    // --- 1. BASIC CURVES ---
    public final static Easing LINEAR = t -> t;
    public final static Easing SMOOTH_STEP = t -> t * t * (3 - 2 * t);

    // --- 2. POWER CURVES (Quadratic, Cubic, Quartic, Quintic) ---
    
    // Quadratic (standard easing in/out)
    public final static Easing QUAD_IN = t -> t * t;
    public final static Easing QUAD_OUT = t -> t * (2 - t);
    public final static Easing QUAD_IN_OUT = t -> t < 0.5f ? 2 * t * t : 1 - (float) Math.pow(-2 * t + 2, 2) / 2;

    // Cubic
    public final static Easing CUBIC_IN = t -> t * t * t;
    public final static Easing CUBIC_OUT = t -> 1 - (float) Math.pow(1 - t, 3);
    public final static Easing CUBIC_IN_OUT = t -> t < 0.5f ? 4 * t * t * t : 1 - (float) Math.pow(-2 * t + 2, 3) / 2;

    // Quartic
    public final static Easing QUART_IN = t -> t * t * t * t;
    public final static Easing QUART_OUT = t -> 1 - (float) Math.pow(1 - t, 4);
    public final static Easing QUART_IN_OUT = t -> t < 0.5f ? 8 * t * t * t * t : 1 - (float) Math.pow(-2 * t + 2, 4) / 2;

    // Quintic
    public final static Easing QUINT_IN = t -> t * t * t * t * t;
    public final static Easing QUINT_OUT = t -> 1 - (float) Math.pow(1 - t, 5);
    public final static Easing QUINT_IN_OUT = t -> t < 0.5f ? 16 * t * t * t * t * t : 1 - (float) Math.pow(-2 * t + 2, 5) / 2;

    // --- 3. SINE CURVES ---
    public final static Easing SINE_IN = t -> 1 - (float) Math.cos((t * Math.PI) / 2);
    public final static Easing SINE_OUT = t -> (float) Math.sin((t * Math.PI) / 2);
    public final static Easing SINE_IN_OUT = t -> (float) -(Math.cos(Math.PI * t) - 1) / 2;

    // --- 4. EXPONENTIAL & CIRCULAR ---
    
    // Expo
    public final static Easing EXPO_IN = t -> t == 0 ? 0 : (float) Math.pow(2, 10 * t - 10);
    public final static Easing EXPO_OUT = t -> t == 1 ? 1 : 1 - (float) Math.pow(2, -10 * t);
    public final static Easing EXPO_IN_OUT = t -> t == 0 ? 0 : t == 1 ? 1 : t < 0.5f 
            ? (float) Math.pow(2, 20 * t - 10) / 2 
            : (2 - (float) Math.pow(2, -20 * t + 10)) / 2;

    // Circular
    public final static Easing CIRC_IN = t -> 1 - (float) Math.sqrt(1 - Math.pow(t, 2));
    public final static Easing CIRC_OUT = t -> (float) Math.sqrt(1 - Math.pow(t - 1, 2));
    public final static Easing CIRC_IN_OUT = t -> t < 0.5f 
            ? (1 - (float) Math.sqrt(1 - Math.pow(2 * t, 2))) / 2 
            : ((float) Math.sqrt(1 - Math.pow(-2 * t + 2, 2)) + 1) / 2;

    // --- 5. BACK & ELASTIC (Overshoot/Spring) ---

    // Back
    public final static Easing BACK_IN = t -> {
        float c1 = 1.70158f;
        float c3 = c1 + 1;
        return c3 * t * t * t - c1 * t * t;
    };
    public final static Easing BACK_OUT = t -> {
        float c1 = 1.70158f;
        float c3 = c1 + 1;
        return 1 + c3 * (float) Math.pow(t - 1, 3) + c1 * (float) Math.pow(t - 1, 2);
    };
    public final static Easing BACK_IN_OUT = t -> {
        float c1 = 1.70158f;
        float c2 = c1 * 1.525f;
        return t < 0.5f 
            ? ((float) Math.pow(2 * t, 2) * ((c2 + 1) * 2 * t - c2)) / 2 
            : ((float) Math.pow(2 * t - 2, 2) * ((c2 + 1) * (t * 2 - 2) + c2) + 2) / 2;
    };

    // Elastic
    public final static Easing ELASTIC_IN = t -> {
        float c4 = (2 * (float) Math.PI) / 3;
        return t == 0 ? 0 : t == 1 ? 1 : -(float) (Math.pow(2, 10 * t - 10) * Math.sin((t * 10 - 10.75) * c4));
    };
    public final static Easing ELASTIC_OUT = t -> {
        double c4 = (2 * Math.PI) / 3;
        return t == 0 ? 0 : t == 1 ? 1 : (float)(Math.pow(2, -10 * t) * Math.sin((t * 10 - 0.75) * c4) + 1);
    };
    public final static Easing ELASTIC_IN_OUT = t -> {
        float c5 = (2 * (float) Math.PI) / 4.5f;
        return t == 0 ? 0 : t == 1 ? 1 : t < 0.5f 
            ? -(float) (Math.pow(2, 20 * t - 10) * Math.sin((20 * t - 11.125) * c5)) / 2 
            : (float) (Math.pow(2, -20 * t + 10) * Math.sin((20 * t - 11.125) * c5)) / 2 + 1;
    };

    // --- 6. BOUNCE ---
    public final static Easing BOUNCE_OUT = t -> bounceOutInternal(t);
    public final static Easing BOUNCE_IN = t -> 1 - bounceOutInternal(1 - t);
    public final static Easing BOUNCE_IN_OUT = t -> t < 0.5f 
            ? (1 - bounceOutInternal(1 - 2 * t)) / 2 
            : (1 + bounceOutInternal(2 * t - 1)) / 2;

    // Private helper for bounce calculation logic
    private static double bounceOutInternal(double t) {
        double n1 = 7.5625f;
        double d1 = 2.75f;
        if (t < 1 / d1) {
            return n1 * t * t;
        } else if (t < 2 / d1) {
            return n1 * (t -= 1.5f / d1) * t + 0.75f;
        } else if (t < 2.5 / d1) {
            return n1 * (t -= 2.25f / d1) * t + 0.9375f;
        } else {
            return n1 * (t -= 2.625f / d1) * t + 0.984375f;
        }
    }

    // --- ALIASES (Legacy Support) ---
    public final static Easing EASING_IN = QUAD_IN;
    public final static Easing EASING_OUT = QUAD_OUT;
    public final static Easing EASING_IN_AND_OUT = QUAD_IN_OUT;
}