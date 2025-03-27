package pcd.ass01;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class BoidThread extends Thread{
    private final List<Boid> boids;
    private final BoidsModel model;
    private final CyclicBarrier barrier;
    private final Administrator administrator;

    public BoidThread(final List<Boid> boids, final BoidsModel model,
                      final CyclicBarrier barrier, final Administrator administrator) {
        this.boids = boids;
        this.model = model;
        this.barrier = barrier;
        this.administrator = administrator;
    }

    @Override
    public void run() {
        while(true) {
            for (Boid boid : boids) {
                boid.updateVelocity(model);
            }

            try {
                barrier.await(); //Wait all the boids to update their velocities
            } catch (InterruptedException | BrokenBarrierException e) {}

            for (Boid boid : boids) {
                boid.updatePos(model);
            }

            this.administrator.threadDone();
        }
    }

}
