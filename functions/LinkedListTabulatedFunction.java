package functions;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListTabulatedFunction implements TabulatedFunction, Externalizable, Cloneable {

    private class FunctionNode {
        public FunctionPoint value;
        public FunctionNode next;
        public FunctionNode prev;
    }

    private FunctionNode head;
    private int capacity;
    private FunctionNode lastAccessedNode;
    private int lastAccessedIndex;
    private double DEFAULT_EPSILON = 1e-9;

    private FunctionNode getNodeByIndex(int index) {
        FunctionNode answer;
        int curIndex;
        if (index < lastAccessedIndex) {
            if (index < lastAccessedIndex - index) {
                curIndex = 0;
                answer = head.next;
                while (curIndex != index) {
                    answer = answer.next;
                    curIndex++;
                }
            } else {
                curIndex = lastAccessedIndex;
                answer = lastAccessedNode;
                while (curIndex != index) {
                    answer = answer.prev;
                    curIndex--;
                }
            }
        } else {
            if (index - lastAccessedIndex < capacity - lastAccessedIndex - 1) {
                curIndex = lastAccessedIndex;
                answer = lastAccessedNode;
                while (curIndex != index) {
                    answer = answer.next;
                    curIndex++;
                }
            } else {
                curIndex = capacity - 1;
                answer = head.prev;
                while (curIndex != index) {
                    answer = answer.prev;
                    curIndex--;
                }
            }

        }
        lastAccessedNode = answer;
        lastAccessedIndex = index;
        return answer;
    }
    private FunctionNode addNodeToTail() {
        FunctionNode newTail = new FunctionNode();
        FunctionNode oldTail = head.prev;
        oldTail.next = newTail;
        newTail.prev = oldTail;
        newTail.next = head;
        head.prev = newTail;
        capacity++;
        lastAccessedNode = newTail;
        lastAccessedIndex = capacity - 1;
        return newTail;
    }
    private FunctionNode addNodeByIndex(int index) {
        FunctionNode newNode = new FunctionNode();
        FunctionNode rightNode = getNodeByIndex(index);
        FunctionNode leftNode = rightNode.prev;
        leftNode.next = newNode;
        newNode.prev = leftNode;
        newNode.next = rightNode;
        rightNode.prev = newNode;
        capacity++;
        lastAccessedNode = newNode;
        lastAccessedIndex = index;
        return newNode;
    }
    private FunctionNode deleteNodeByIndex(int index) {
        FunctionNode delNote = getNodeByIndex(index);
        delNote.prev.next = delNote.next;
        delNote.next.prev = delNote.prev;
        capacity--;
        lastAccessedNode = head.next;
        lastAccessedIndex = 0;
        return delNote;
    }

    public LinkedListTabulatedFunction() {}
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) throws IllegalArgumentException {
        if (leftX >= rightX || pointsCount < 2) {
            throw new IllegalArgumentException("Левая граница области должна быть меньше правой, количество точек должно быть больше 2");
        }
        head = new FunctionNode();
        head.next = head;
        head.prev = head;
        capacity = 0;
        FunctionNode curNode;
        double step = (rightX - leftX) / (pointsCount - 1);
        while (capacity != pointsCount) {
            curNode = this.addNodeToTail();
            curNode.value = new FunctionPoint(leftX + (capacity - 1) * step, 0);
        }
    }
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) throws IllegalArgumentException {
        if (leftX >= rightX || values.length < 2) {
            throw new IllegalArgumentException("Левая граница области должна быть меньше правой, количество точек должно быть больше 2");
        }
        head = new FunctionNode();
        head.next = head;
        head.prev = head;
        capacity = 0;
        FunctionNode curNode;
        double step = (rightX - leftX) / (values.length - 1);
        while (capacity != values.length) {
            curNode = this.addNodeToTail();
            curNode.value = new FunctionPoint(leftX + (capacity - 1) * step, values[capacity - 1]);
        }
    }
    public LinkedListTabulatedFunction(FunctionPoint[] values) throws IllegalArgumentException{
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть больше 2");
        }
        for (int i = 1; i < values.length; i++) {
            if (values[i].getX() <= values[i - 1].getX()) {
                throw new IllegalArgumentException("Точки не упорядочены по значению абсциссы");
            }
        }
        head = new FunctionNode();
        head.next = head;
        head.prev = head;
        capacity = 0;
        FunctionNode curNode;
        for (int i = 0; i < values.length; i++) {
            curNode = this.addNodeToTail();
            curNode.value = new FunctionPoint(values[i]);
        }
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
        LinkedListTabulatedFunction source = new LinkedListTabulatedFunction(values);
        this.head = source.head;
        this.capacity = source.capacity;
        this.lastAccessedNode = source.lastAccessedNode;
        this.lastAccessedIndex = source.lastAccessedIndex;
    }

    public double getLeftDomainBorder() { return this.getNodeByIndex(0).value.getX();}
    public double getRightDomainBorder() { return this.getNodeByIndex(capacity - 1).value.getX();}
    public double getFunctionValue(double x) {
        if (x < this.head.next.value.getX() || x > this.head.prev.value.getX()) {return Double.NaN;}
        FunctionNode curNode = head.next;
        for (int t = 0; t < capacity; t++) {
            if (Math.abs(x - curNode.value.getX()) < DEFAULT_EPSILON) {return curNode.value.getY();}
            curNode = curNode.next;
        }
        int t = 0;
        FunctionNode Node1 = head.next;
        while (x > Node1.value.getX()) {
            Node1 = Node1.next;
        }
        FunctionNode Node0 = Node1.prev;
        double x0 = Node0.value.getX();
        double x1 = Node1.value.getX();
        double y0 = Node0.value.getY();
        double y1 = Node1.value.getY();
        return y0 + (y1 - y0) * (x - x0) / (x1 - x0);
    }

    public int getPointsCount() {return capacity;}
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index >= capacity || index < 0) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс за пределами таблицы");
        }
        return new FunctionPoint(this.getNodeByIndex(index).value);
    }
    public void setPoint(int index, FunctionPoint point) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index >= capacity || index < 0) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс за пределами таблицы");
        }
        if ((index == 0 && point.getX() >= this.getNodeByIndex(1).value.getX()) || (index == capacity - 1 && point.getX() <= this.getNodeByIndex(capacity - 2).value.getX())) {
            throw new InappropriateFunctionPointException("координата x задаваемой точки лежит вне интервала, определяемого значениями соседних точек");
        }
        if (point.getX() <= this.getNodeByIndex(index - 1).value.getX() || point.getX() >= this.getNodeByIndex(index + 1).value.getX()) {
            throw new InappropriateFunctionPointException("координата x задаваемой точки лежит вне интервала, определяемого значениями соседних точек");
        }
        this.getNodeByIndex(index).value = new FunctionPoint(point);
    }
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index >= capacity || index < 0) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс за пределами таблицы");
        }
        return this.getNodeByIndex(index).value.getX();
    }
    public void setPointX(int index, double x) throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        if (index >= capacity || index < 0) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс за пределами таблицы");
        }
        if ((index == 0 && x >= this.getNodeByIndex(1).value.getX()) || (index == capacity - 1 && x <= this.getNodeByIndex(capacity - 2).value.getX())) {
            throw new InappropriateFunctionPointException("координата x задаваемой точки лежит вне интервала, определяемого значениями соседних точек");
        }
        if (x <= this.getNodeByIndex(index - 1).value.getX() || x >= this.getNodeByIndex(index + 1).value.getX()) {
            throw new InappropriateFunctionPointException("координата x задаваемой точки лежит вне интервала, определяемого значениями соседних точек");
        }
        this.getNodeByIndex(index).value.setX(x);
    }
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index >= capacity || index < 0) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс за пределами таблицы");
        }
        return this.getNodeByIndex(index).value.getY();
    }
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        if (index >= capacity || index < 0) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс за пределами таблицы");
        }
        this.getNodeByIndex(index).value.setY(y);
    }

    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        if (capacity < 3) {
            throw new IllegalStateException("В таблице менее трех точек");
        }
        if (index >= capacity || index < 0) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс за пределами таблицы");
        }
        this.deleteNodeByIndex(index);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        for (int t = 0; t < capacity; t++) {
            if (Math.abs(point.getX() - this.getNodeByIndex(t).value.getX()) <= DEFAULT_EPSILON) {
                throw new InappropriateFunctionPointException("Точка с такой с такой же абсциссой уже имеется");
            }
        }
        FunctionPoint copy = new FunctionPoint(point);
        int pose = 0;
        while (pose < capacity && this.getNodeByIndex(pose).value.getX() < copy.getX()) {
            pose++;
        }
        if (pose == capacity) {
            this.addNodeToTail().value = copy;
        } else {
            this.addNodeByIndex(pose).value = copy;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        FunctionNode current = head.next;
        for (int i = 0; i < capacity; i++) {
            sb.append(current.value.toString());
            if (i < capacity - 1) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("}");
        return sb.toString();
    }
    public boolean equals(Object o) {
        if (!(o instanceof TabulatedFunction)) return false;
        TabulatedFunction other = (TabulatedFunction) o;
        if (this.getPointsCount() != other.getPointsCount()) return false;
        if (o instanceof LinkedListTabulatedFunction) {
            LinkedListTabulatedFunction that = (LinkedListTabulatedFunction) o;
            FunctionNode n1 = this.head.next;
            FunctionNode n2 = that.head.next;
            for (int i = 0; i < capacity; i++) {
                if (!n1.value.equals(n2.value)) {
                    return false;
                }
                n1 = n1.next;
                n2 = n2.next;
            }
            return true;
        }
        FunctionNode current = head.next;
        for (int i = 0; i < capacity; i++) {
            if (!current.value.equals(other.getPoint(i))) {
                return false;
            }
            current = current.next;
        }
        return true;
    }
    public int hashCode() {
        int hash = capacity;
        FunctionNode current = head.next;
        for (int i = 0; i < capacity; i++) {
            hash ^= current.value.hashCode();
            current = current.next;
        }
        return hash;
    }
    public Object clone() {
        try {
            LinkedListTabulatedFunction cloned = (LinkedListTabulatedFunction) super.clone();
            cloned.head = new FunctionNode();
            cloned.head.next = cloned.head;
            cloned.head.prev = cloned.head;
            cloned.capacity = this.capacity;
            FunctionNode originalCurrent = this.head.next;
            FunctionNode clonedCurrent = cloned.head;
            for (int i = 0; i < capacity; i++) {
                FunctionNode newNode = new FunctionNode();
                newNode.value = (FunctionPoint) originalCurrent.value.clone();
                newNode.prev = clonedCurrent;
                newNode.next = cloned.head;
                clonedCurrent.next = newNode;
                cloned.head.prev = newNode;
                clonedCurrent = newNode;
                originalCurrent = originalCurrent.next;
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {

            private FunctionNode current = head.next;
            public boolean hasNext() {
                return current != head;
            }
            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                FunctionPoint p = current.value;
                current = current.next;
                return new FunctionPoint(p.getX(), p.getY());
            }
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory {

        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new LinkedListTabulatedFunction(points);
        }

        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new LinkedListTabulatedFunction(leftX, rightX, pointsCount);
        }

        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new LinkedListTabulatedFunction(leftX, rightX, values);
        }
    }
}
