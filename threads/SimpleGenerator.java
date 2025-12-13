package threads;

import functions.Function;
import functions.basic.Log;

import java.util.Random;

public class SimpleGenerator implements Runnable {
    private Task task;
    private Random random = new Random();

    public SimpleGenerator(Task task) {
        this.task = task;
    }
    public void run() {
        for (int i = 0; i < task.getTasksCount(); i++) {
            double base = 1 + 9 * random.nextDouble();
            Function function = new Log(base);
            double left = random.nextDouble() * 100;
            double right = 100 + random.nextDouble() * 100;
            double step = random.nextDouble();
            if (step == 0) step = 0.1;
            synchronized (task) {
                task.setFunction(function);
                task.setLeft(left);
                task.setRight(right);
                task.setStep(step);
            }
            System.out.printf("Source %.4f %.4f %.4f%n", left, right, step);
        }
    }
}
