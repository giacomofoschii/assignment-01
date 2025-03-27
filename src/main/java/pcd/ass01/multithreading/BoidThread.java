package pcd.ass01.multithreading;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class BoidThread extends Thread{
    private final List<Boid> boids;
    private final BoidsSimulator simulator;
    private final CyclicBarrier barrier;
    private final Administrator administrator;

    public BoidThread(final List<Boid> boids, final BoidsSimulator simulator,
                      final CyclicBarrier barrier, final Administrator administrator) {
        this.boids = boids;
        this.simulator = simulator;
        this.barrier = barrier;
        this.administrator = administrator;
    }

    @Override
    public void run() {
        while(true) {
            synchronized (this.simulator) {
                while(this.simulator.isPaused()) {
                    try {
                        this.simulator.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            for (Boid boid : boids) {
                boid.updateVelocity(this.simulator.getModel());
            }

            try {
                barrier.await(); //Wait all the boids to update their velocities
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
            }

            for (Boid boid : boids) {
                boid.updatePos(this.simulator.getModel());
            }

            this.administrator.threadDone();
        }
    }
}
