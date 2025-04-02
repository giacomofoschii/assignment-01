package pcd.ass01.multithreading;

import pcd.ass01.Boid;
import pcd.ass01.BoidsController;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;

public class BoidThread extends Thread{
    private List<Boid> boids;
    private final BoidsController controller;
    private final CustomCyclicBarrier barrier;
    private final Administrator administrator;
    private volatile boolean stopped;

    public BoidThread(final List<Boid> boids, final BoidsController controller,
                      final CustomCyclicBarrier barrier, final Administrator administrator) {
        assignPool(boids);
        this.controller = controller;
        this.barrier = barrier;
        this.administrator = administrator;
    }

    public void assignPool(List<Boid> boids) {
        if(this.boids != null) {
            this.boids.clear();
        }
        this.boids = boids;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    @Override
    public void run() {
        while(!stopped) {
            synchronized (this.controller) {
                while(this.controller.isPaused()) {
                    try {
                        this.controller.wait();
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
                barrier.reset();
                Thread.currentThread().interrupt();
            }

            for (Boid boid : boids) {
                boid.updatePos(this.controller.getModel());
            }

            this.administrator.threadDone();
        }
    }
}
