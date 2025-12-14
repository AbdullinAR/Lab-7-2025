package functions;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class TabulatedFunctions {
    private TabulatedFunctions() {}
    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory factory) {
        TabulatedFunctions.factory = factory;
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.createTabulatedFunction(points);
    }
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) throws IllegalArgumentException {
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть больше 2");
        }
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Выход за границы определения функции");
        }
        FunctionPoint[] array = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            array[i] = new FunctionPoint(leftX + step * i, function.getFunctionValue(leftX + step * i));
        }
        return createTabulatedFunction(array);
    }
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);
        dataOut.writeInt(function.getPointsCount());
        for (int i = 0; i < function.getPointsCount(); i++) {
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }
        dataOut.flush();
    }
    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);
        int pointCount = dataIn.readInt();
        FunctionPoint[] array = new FunctionPoint[pointCount];
        for (int i = 0; i < pointCount; i++) {
            array[i] = new FunctionPoint(dataIn.readDouble(), dataIn.readDouble());
        }
        return createTabulatedFunction(array);
    }
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        out.write(String.valueOf(function.getPointsCount()));
        out.write(" ");
        for (int i = 0; i < function.getPointsCount(); i++) {
            out.write(String.valueOf(function.getPointX(i)));
            out.write(" ");
            out.write(String.valueOf(function.getPointY(i)));
            if (i < function.getPointsCount() - 1) {
                out.write(" ");
            }
        }
    }
    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        tokenizer.parseNumbers();
        tokenizer.nextToken();
        int pointCount = (int)tokenizer.nval;
        FunctionPoint[] array = new FunctionPoint[pointCount];
        double tempX;
        double tempY;
        for (int i = 0; i < pointCount; i++) {
            tokenizer.nextToken();
            tempX = tokenizer.nval;
            tokenizer.nextToken();
            tempY = tokenizer.nval;
            array[i] = new FunctionPoint(tempX, tempY);
        }
        return createTabulatedFunction(array);
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> cls, double leftX, double rightX, int pointsCount) {
        try {
            Constructor<? extends TabulatedFunction> ctor = cls.getConstructor(double.class, double.class, int.class);
            return ctor.newInstance(leftX, rightX, pointsCount);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }
    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> cls, double leftX, double rightX, double[] values) {
        try {
            Constructor<? extends TabulatedFunction> ctor = cls.getConstructor(double.class, double.class, double[].class);
            return ctor.newInstance(leftX, rightX, values);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }
    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> cls, FunctionPoint[] points) {
        try {
            Constructor<? extends TabulatedFunction> ctor = cls.getConstructor(FunctionPoint[].class);
            return ctor.newInstance((Object) points);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static TabulatedFunction tabulate(Class<? extends TabulatedFunction> cls, Function function, double leftX, double rightX, int pointsCount) throws IllegalArgumentException {
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть больше 2");
        }
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Выход за границы определения функции");
        }
        FunctionPoint[] array = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            array[i] = new FunctionPoint(leftX + step * i, function.getFunctionValue(leftX + step * i));
        }
        return createTabulatedFunction(cls, array);
    }
    public static TabulatedFunction inputTabulatedFunction(Class<? extends TabulatedFunction> cls, InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);
        int pointCount = dataIn.readInt();
        FunctionPoint[] array = new FunctionPoint[pointCount];
        for (int i = 0; i < pointCount; i++) {
            array[i] = new FunctionPoint(dataIn.readDouble(), dataIn.readDouble());
        }
        return createTabulatedFunction(cls, array);
    }
    public static TabulatedFunction readTabulatedFunction(Class<? extends TabulatedFunction> cls, Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);
        tokenizer.parseNumbers();
        tokenizer.nextToken();
        int pointCount = (int)tokenizer.nval;
        FunctionPoint[] array = new FunctionPoint[pointCount];
        double tempX;
        double tempY;
        for (int i = 0; i < pointCount; i++) {
            tokenizer.nextToken();
            tempX = tokenizer.nval;
            tokenizer.nextToken();
            tempY = tokenizer.nval;
            array[i] = new FunctionPoint(tempX, tempY);
        }
        return createTabulatedFunction(cls, array);
    }
}
