package pcd.ass01.taskexecutor;

import java.util.concurrent.ExecutorService;

public class BoidsWorker extends Thread {
    private BoidsModel model;
    private ExecutorService executor;

    public BoidsWorker(BoidsModel model, ExecutorService executor) {
        super();
        this.model = model;
        this.executor = executor;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            CustomCountDownLatch velocityLatch = new CustomCountDownLatchImpl(model.getBoids().size());
            for (Boid boid : model.getBoids()) {
                executor.submit(new UpdateVelocityTask(velocityLatch, boid, model));
            }
            try {
                velocityLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            CustomCountDownLatch positionLatch = new CustomCountDownLatchImpl(model.getBoids().size());
            for (Boid boid : model.getBoids()) {
                executor.submit(new UpdatePositionTask(positionLatch, boid, model));
            }
            try {
                positionLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
