package functions;

import functions.meta.*;

public class Functions {
    private Functions() {}

    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }
    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }
    public static Function power(Function f, double power) {
        return new Power(f, power);
    }
    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }
    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }
    public static Function composition(Function f1, Function f2) {
        return new Composition(f2, f1);
    }

    public static double integrate(Function f, double left, double right, double step) {
        if (left < f.getLeftDomainBorder() || right > f.getRightDomainBorder()) {
            throw new IllegalArgumentException("Интервал интегрирования выходит за область определения функции");
        }
        double result = 0.0;
        double x = left;
        while (x < right) {
            double xNext = Math.min(x + step, right);
            result += (f.getFunctionValue(x) + f.getFunctionValue(xNext)) * (xNext - x) / 2.0;
            x = xNext;
        }
        return result;
    }
}
