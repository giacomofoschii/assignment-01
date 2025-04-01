package pcd.ass01.taskexecutor;

public class UpdatePositionTask implements Runnable {

    private final CustomCountDownLatch latch;
    private final Boid boid;
    private final BoidsModel model;

    public UpdatePositionTask(CustomCountDownLatch latch, Boid boid, BoidsModel model) {
        this.latch = latch;
        this.boid = boid;
        this.model = model;
    }

    @Override
    public void run() {
        this.boid.updatePos(model);
        this.latch.countDown();

    }
}
