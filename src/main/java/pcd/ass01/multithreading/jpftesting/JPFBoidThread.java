package pcd.ass01.multithreading.jpftesting;

import pcd.ass01.multithreading.Administrator;
import pcd.ass01.multithreading.Boid;
import pcd.ass01.multithreading.BoidsController;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class JPFBoidThread extends Thread{
    private List<JPFBoid> boids;
    private final CyclicBarrier barrier;
    private final Administrator administrator;
    private volatile boolean stopped;

    public JPFBoidThread(final List<JPFBoid> boids,
                      final CyclicBarrier barrier, final Administrator administrator) {
        assignPool(boids);
        this.barrier = barrier;
        this.administrator = administrator;
    }


    public void assignPool(List<JPFBoid> boids) {
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

            for (JPFBoid boid : boids) {
                boid.updateVelocity();
            }

            try {
                barrier.await(); //Wait all the boids to update their velocities
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
            }

            for (JPFBoid boid : boids) {
                boid.updatePos();
            }

            this.administrator.threadDone();
        }
    }

}
