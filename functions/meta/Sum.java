package functions.meta;

import functions.Function;

public class Sum implements Function {
    private Function f1;
    private Function f2;

    public Sum(Function f1, Function f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    public double getLeftDomainBorder() {
        double LeftDomainBorder = Math.max(f1.getLeftDomainBorder(), f2.getLeftDomainBorder());
        return LeftDomainBorder;
    }
    public double getRightDomainBorder() {
        double RightDomainBorder = Math.min(f1.getRightDomainBorder(), f2.getRightDomainBorder());
        return RightDomainBorder;
    }
    public double getFunctionValue(double x) {
        return f1.getFunctionValue(x) + f2.getFunctionValue(x);
    }
}
