package pcd.ass01.multithreading;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class BoidThread extends Thread{
    private final List<Boid> boids;
    private final BoidsController controller;
    private final CyclicBarrier barrier;
    private final Administrator administrator;
    private volatile boolean stopped = false;

    public BoidThread(final List<Boid> boids, final BoidsController controller,
                      final CyclicBarrier barrier, final Administrator administrator) {
        this.boids = boids;
        this.controller = controller;
        this.barrier = barrier;
        this.administrator = administrator;
    }

    public void stopThread() {
        this.stopped = true;
    }

    @Override
    public void run() {
        while(!stopped) {
            synchronized (this.controller.getSimulator()) {
                while(this.controller.getSimulator().isPaused()) {
                    try {
                        this.controller.getSimulator().wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            for (Boid boid : boids) {
                boid.updateVelocity(this.controller.getModel());
            }

            try {
                barrier.await(); //Wait all the boids to update their velocities
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
            }

            for (Boid boid : boids) {
                boid.updatePos(this.controller.getModel());
            }

            this.administrator.threadDone();
        }
    }
}
