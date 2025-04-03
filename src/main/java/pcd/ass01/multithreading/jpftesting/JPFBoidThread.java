package pcd.ass01.multithreading.jpftesting;

import pcd.ass01.multithreading.MultiAdministrator;
import pcd.ass01.utils.CustomCyclicBarrier;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;

public class JPFBoidThread extends Thread{
    private final List<JPFBoid> boids;
    private final CustomCyclicBarrier barrier;
    private final MultiAdministrator multiAdministrator;

    public JPFBoidThread(final List<JPFBoid> boids,
                      final CustomCyclicBarrier barrier, final MultiAdministrator multiAdministrator) {
        this.boids = boids;
        this.barrier = barrier;
        this.multiAdministrator = multiAdministrator;
    }

    @Override
    public void run() {
        while(true) {

            for (JPFBoid boid : boids) {
                boid.updateVelocity();
            }

            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException ignored) {
            }

            for (JPFBoid boid : boids) {
                boid.updatePos();
            }

            this.multiAdministrator.threadDone();
            break;
        }
    }

}
