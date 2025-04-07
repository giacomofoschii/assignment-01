package pcd.ass01.taskexecutor;

import pcd.ass01.Boid;
import pcd.ass01.BoidsModel;

import java.util.List;

public class UpdateVelocityTask implements UpdateTask {
    private final List<Boid> boids;
    private final BoidsModel model;

    public UpdateVelocityTask(final List<Boid> boids, final BoidsModel model) {
        this.boids = boids;
        this.model = model;
    }

    @Override
    public Void call() {
        this.boids.forEach(boid -> boid.updateVelocity(this.model));
        return null;
    }

}
