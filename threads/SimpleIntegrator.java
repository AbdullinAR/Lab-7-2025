package threads;

import functions.Functions;

public class SimpleIntegrator implements Runnable {
    private Task task;
    public SimpleIntegrator(Task task) {
        this.task = task;
    }
    public void run() {
        for (int i = 0; i < task.getTasksCount(); i++) {
            double left, right, step, result;
            synchronized (task) {
                left = task.getLeft();
                right = task.getRight();
                step = task.getStep();
                result = Functions.integrate(task.getFunction(), left, right, step);
            }
            System.out.printf("Result %.4f %.4f %.4f %.6f%n", left, right, step, result);
        }
    }
}
