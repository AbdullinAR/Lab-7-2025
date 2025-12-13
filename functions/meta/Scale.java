package functions.meta;

import functions.Function;

public class Scale implements Function {
    private Function f1;
    private double scaleX;
    private double scaleY;

    public Scale(Function f1, double scaleX, double scaleY) {
        this.f1 = f1;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public double getLeftDomainBorder() {
        if (scaleX < 0) {
            return f1.getRightDomainBorder() * scaleX;
        }
        return f1.getLeftDomainBorder() * scaleX;
    }
    public double getRightDomainBorder() {
        if (scaleX < 0) {
            return f1.getLeftDomainBorder() * scaleX;
        }
        return f1.getRightDomainBorder() * scaleX;
    }
    public double getFunctionValue(double x) {
        return scaleY * f1.getFunctionValue(x / scaleX);
    }
}
