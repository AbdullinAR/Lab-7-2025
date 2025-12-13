package threads;

import functions.Function;
import functions.basic.Log;

import java.util.Random;

public class Generator extends Thread {
    private Task task;
    private Semaphore semaphore;
    private final Random random = new Random();

    public Generator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }
    public void run() {
        try {
            for (int i = 0; i < task.getTasksCount(); i++) {
                semaphore.acquireWrite();
                double base = 1 + 9 * random.nextDouble();
                Function function = new Log(base);
                double left = random.nextDouble() * 100;
                double right = 100 + random.nextDouble() * 100;
                double step = random.nextDouble();
                if (step == 0) step = 0.0001;
                task.setFunction(function);
                task.setLeft(left);
                task.setRight(right);
                task.setStep(step);
                System.out.printf("Source %.4f %.4f %.4f%n", left, right, step);
                semaphore.releaseWrite();
            }
        } catch (InterruptedException e) {}
    }

}
