package pcd.ass01.multithreading;

import pcd.ass01.Boid;
import pcd.ass01.utils.CustomCyclicBarrier;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;

public class BoidThread extends Thread{
    private List<Boid> boids;
    private final MultiThreadController controller;
    private final CustomCyclicBarrier barrier;
    private final MultiAdministrator multiAdministrator;
    private volatile boolean stopped;

    public BoidThread(final List<Boid> boids, final MultiThreadController controller,
                      final CustomCyclicBarrier barrier, final MultiAdministrator multiAdministrator) {
        assignPool(boids);
        this.controller = controller;
        this.barrier = barrier;
        this.multiAdministrator = multiAdministrator;
        this.stopped = false;
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
                    } catch (InterruptedException ignored) {
                    }
                }
            }

            boids.forEach(boid -> boid.updateVelocity(this.controller.getModel()));

            try {
                barrier.await(); //Wait all the boids to update their velocities
            } catch (InterruptedException | BrokenBarrierException ignored) {
            }

            boids.forEach(boid -> boid.updatePos(this.controller.getModel()));

            this.multiAdministrator.threadDone();
        }
    }
}