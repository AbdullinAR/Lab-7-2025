package functions;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayTabulatedFunction implements TabulatedFunction, Externalizable, Cloneable {
    private FunctionPoint[] array;
    private int capacity;
    private double DEFAULT_EPSILON = 1e-9;

    public ArrayTabulatedFunction() {}
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException {
        if (leftX >= rightX || pointsCount < 2) {
            throw new IllegalArgumentException("Левая граница области должна быть меньше правой, количество точек должно быть больше 2");
        }
        array = new FunctionPoint[pointsCount];
        for (int t = 0; t < pointsCount; t++) {
            array[t] = new FunctionPoint();
        }
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int t = 0; t < pointsCount - 1; t++) {
            array[t].setX(leftX + t * step);
        }
        array[pointsCount - 1].setX(rightX);
        capacity = pointsCount;
    }
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException{
        if (leftX >= rightX || values.length < 2) {
            throw new IllegalArgumentException("Левая граница области должна быть меньше правой, количество точек должно быть больше 2");
        }
        array = new FunctionPoint[values.length];
        for (int t = 0; t < values.length; t++) {
            array[t] = new FunctionPoint();
        }
        double step = (rightX - leftX) / (values.length - 1);
        for (int t = 0; t < values.length - 1; t++) {
            array[t].setX(leftX + t * step);
            array[t].setY(values[t]);
        }
        array[values.length - 1].setX(rightX);
        array[values.length - 1].setY(values[values.length - 1]);
        capacity = values.length;
    }
    public ArrayTabulatedFunction(FunctionPoint[] values) throws IllegalArgumentException {
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть больше 2");
        }
        for (int i = 1; i < values.length; i++) {
            if (values[i].getX() <= values[i - 1].getX()) {
                throw new IllegalArgumentException("Точки не упорядочены по значению абсциссы");
            }
        }
        array = new FunctionPoint[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i] = new FunctionPoint(values[i]);
        }
        capacity = values.length;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(capacity);
        for (int i = 0; i < capacity; i++) {
            out.writeDouble(this.getPointX(i));
            out.writeDouble(this.getPointY(i));
        }
    }
    public void readExternal(ObjectInput in) throws IOException {
        int size = in.readInt();
        FunctionPoint[] values = new FunctionPoint[size];
        for (int i = 0; i < size; i++) {
            values[i] = new FunctionPoint(in.readDouble(), in.readDouble());
        }
        this.capacity = size;
        this.array = values;
    }


    public double getLeftDomainBorder() { return array[0].getX();}
    public double getRightDomainBorder() { return array[capacity - 1].getX();}
    public double getFunctionValue(double x) {
        if (x < array[0].getX() || x > array[capacity - 1].getX()) {return Double.NaN;}
        for (int t = 0; t < capacity; t++) {
            if (Math.abs(x - array[t].getX()) < DEFAULT_EPSILON) {return array[t].getY();}
        }
        int t = 0;
        while (x > array[t].getX()) {
            t++;
        }
        double x0 = array[t - 1].getX();
        double x1 = array[t].getX();
        double y0 = array[t - 1].getY();
        double y1 = array[t].getY();
        return y0 + (y1 - y0) * (x - x0) / (x1 - x0);
    }


    public int getPointsCount() {return capacity;}
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index >= capacity || index < 0) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс за пределами таблицы");
        }
        return new FunctionPoint(array[index]);
    }
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index >= capacity || index < 0) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс за пределами таблицы");
        }
        if ((index == 0 && point.getX() >= array[1].getX()) || (index == capacity - 1 && point.getX() <= array[capacity - 2].getX())) {
            throw new InappropriateFunctionPointException("координата x задаваемой точки лежит вне интервала, определяемого значениями соседних точек");
        }
        if (point.getX() <= array[index - 1].getX() || point.getX() >= array[index + 1].getX()) {
            throw new InappropriateFunctionPointException("координата x задаваемой точки лежит вне интервала, определяемого значениями соседних точек");
        }
        array[index] = new FunctionPoint(point);
    }
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index >= capacity || index < 0) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс за пределами таблицы");
        }
        return array[index].getX();
    }
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index >= capacity || index < 0) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс за пределами таблицы");
        }
        if ((index == 0 && x >= array[1].getX()) || (index == capacity - 1 && x <= array[capacity - 2].getX())) {
            throw new InappropriateFunctionPointException("координата x задаваемой точки лежит вне интервала, определяемого значениями соседних точек");
        }
        if (x <= array[index - 1].getX() || x >= array[index + 1].getX()) {
            throw new InappropriateFunctionPointException("координата x задаваемой точки лежит вне интервала, определяемого значениями соседних точек");
        }
        array[index].setX(x);
    }
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index >= capacity || index < 0) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс за пределами таблицы");
        }
        return array[index].getY();
    }
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (index >= capacity || index < 0) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс за пределами таблицы");
        }
        array[index].setY(y);
    }


    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        if (capacity < 3) {
            throw new IllegalStateException("В таблице менее трех точек");
        }
        if (index >= capacity || index < 0) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс за пределами таблицы");
        }
        System.arraycopy(array, index + 1, array, index, capacity - index - 1);
        array[capacity - 1] = null;
        capacity--;
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        for (int t = 0; t < capacity; t++) {
            if (Math.abs(point.getX() - array[t].getX()) <= DEFAULT_EPSILON) {
                throw new InappropriateFunctionPointException("Точка с такой с такой же абсциссой уже имеется");
            }
        }
        FunctionPoint copy = new FunctionPoint(point);
        int pose = 0;
        while (pose < capacity && array[pose].getX() < copy.getX()) {
            pose++;
        }
        if (capacity == array.length) {
            FunctionPoint[] temp = new FunctionPoint[capacity+1];
            for (int t = 0; t < capacity+1; t++) {
                temp[t] = new FunctionPoint();
            }
            for (int t = 0; t < capacity; t++) {
                temp[t] = new FunctionPoint(array[t]);
            }
            array = temp;
            capacity++;
        }
        System.arraycopy(array, pose, array, pose + 1, capacity - pose - 1);
        array[pose] = copy;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < capacity; i++) {
            sb.append(array[i].toString());
            if (i < capacity - 1) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }
    public boolean equals(Object o) {
        if (!(o instanceof TabulatedFunction)) return false;
        TabulatedFunction other = (TabulatedFunction) o;
        if (this.getPointsCount() != other.getPointsCount()) return false;
        if (o instanceof ArrayTabulatedFunction) {
            ArrayTabulatedFunction that = (ArrayTabulatedFunction) o;
            for (int i = 0; i < capacity; i++) {
                if (!this.array[i].equals(that.array[i])) {
                    return false;
                }
            }
            return true;
        }
        for (int i = 0; i < capacity; i++) {
            if (!this.getPoint(i).equals(other.getPoint(i))) {
                return false;
            }
        }
        return true;
    }
    public int hashCode() {
        int hash = capacity;
        for (int i = 0; i < capacity; i++) {
            hash ^= array[i].hashCode();
        }
        return hash;
    }
    public Object clone() {
        try {
            ArrayTabulatedFunction cloned = (ArrayTabulatedFunction) super.clone();
            cloned.array = new FunctionPoint[this.capacity];
            for (int i = 0; i < this.capacity; i++) {
                cloned.array[i] = (FunctionPoint) this.array[i].clone();
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private int index = 0;
            public boolean hasNext() {
                return index < capacity;
            }
            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                FunctionPoint p = array[index++];
                return new FunctionPoint(p.getX(), p.getY());
            }
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory {

        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new ArrayTabulatedFunction(points);
        }

        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        }

        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new ArrayTabulatedFunction(leftX, rightX, values);
        }
    }
}
