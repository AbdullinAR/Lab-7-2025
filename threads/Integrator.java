package threads;

import functions.Functions;

public class Integrator extends Thread {
    private Task task;
    private Semaphore semaphore;

    public Integrator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }
    public void run() {
        try {
            for (int i = 0; i < task.getTasksCount(); i++) {
                semaphore.acquireRead();
                double left = task.getLeft();
                double right = task.getRight();
                double step = task.getStep();
                double result = Functions.integrate(task.getFunction(), left, right, step);
                System.out.printf("Result %.4f %.4f %.4f %.6f%n", left, right, step, result);
                semaphore.releaseRead();
            }
        } catch (InterruptedException e) {}
    }
}
