package pcd.ass01.taskexecutor;

import java.util.List;

public class UpdateVelocityTask implements Runnable {
    private final CustomCountDownLatch latch;
    private final List<Boid> boids;
    private final BoidsModel model;

    public UpdateVelocityTask(CustomCountDownLatch latch, List<Boid> boids, BoidsModel model) {
        this.latch = latch;
        this.boids = boids;
        this.model = model;
    }

    @Override
    public void run() {
        for(Boid boid : this.boids) {
            boid.updateVelocity(model);
        }
        this.latch.countDown();

    }

}
