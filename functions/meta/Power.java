package functions.meta;

import functions.Function;

public class Power implements Function {
    private Function f1;
    private double power;

    public Power(Function f1, double power) {
        this.f1 = f1;
        this.power = power;
    }

    public double getLeftDomainBorder() {
        return f1.getLeftDomainBorder();
    }
    public double getRightDomainBorder() {
        return f1.getRightDomainBorder();
    }
    public double getFunctionValue(double x) {
        return Math.pow(f1.getFunctionValue(x), power);
    }
}
