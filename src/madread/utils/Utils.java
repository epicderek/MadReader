package madread.utils;

import static java.lang.Math.*;

/**
 * Helpers for computations.
 */
public abstract class Utils
{
    public static double lfactor(double v)
    {
        return 1 / sqrt(1-pow(v, 2));
    }


}
