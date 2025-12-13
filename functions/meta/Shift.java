package functions.meta;

import functions.Function;

public class Shift implements Function {
    private Function f1;
    private double shiftX;
    private double shiftY;

    public Shift(Function f1, double shiftX, double shiftY) {
        this.f1 = f1;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
    }

    public double getLeftDomainBorder() {
        return f1.getLeftDomainBorder() + shiftX;
    }
    public double getRightDomainBorder() {
        return f1.getRightDomainBorder() + shiftX;
    }
    public double getFunctionValue(double x) {
        return shiftY + f1.getFunctionValue(x - shiftX);
    }
}
