package pcd.ass01.taskexecutor;

import pcd.ass01.Boid;
import pcd.ass01.BoidsModel;

import java.util.List;

public class UpdatePositionTask implements UpdateTask {

    private final List<Boid> boids;
    private final BoidsModel model;

    public UpdatePositionTask(final List<Boid> boids, final BoidsModel model) {
        this.boids = boids;
        this.model = model;
    }

    @Override
    public Void call() {
        this.boids.forEach(boid -> boid.updatePos(this.model));
        return null;
    }
}
