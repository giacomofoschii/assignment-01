package pcd.ass01.taskexecutor;

import pcd.ass01.Boid;
import pcd.ass01.BoidsModel;

import java.util.List;

public class UpdatePositionTask implements Runnable {

    private final CustomCountDownLatch latch;
    private final List<Boid> boids;
    private final BoidsModel model;

    public UpdatePositionTask(CustomCountDownLatch latch, List<Boid> boids, BoidsModel model) {
        this.latch = latch;
        this.boids = boids;
        this.model = model;
    }

    @Override
    public void run() {
        for(Boid boid : this.boids) {
            boid.updatePos(model);
        }
        this.latch.countDown();
    }
}
