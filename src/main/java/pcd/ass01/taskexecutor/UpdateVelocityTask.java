package pcd.ass01.taskexecutor;

public class UpdateVelocityTask implements Runnable {
    private final CustomCountDownLatch latch;
    private final Boid boid;
    private final BoidsModel model;

    public UpdateVelocityTask(CustomCountDownLatch latch, Boid boid, BoidsModel model) {
        this.latch = latch;
        this.boid = boid;
        this.model = model;
    }

    @Override
    public void run() {
        this.boid.updateVelocity(model);
        this.latch.countDown();

    }

}
