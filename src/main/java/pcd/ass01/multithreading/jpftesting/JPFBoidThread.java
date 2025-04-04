package pcd.ass01.multithreading.jpftesting;

import pcd.ass01.multithreading.MultiAdministrator;
import pcd.ass01.utils.CustomCyclicBarrier;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;

public class JPFBoidThread extends Thread{
    private final List<JPFBoid> boids;
    private final CustomCyclicBarrier barrier;
    private final MultiAdministrator multiAdministrator;
    private final JPFBoidsModel model;

    public JPFBoidThread(final List<JPFBoid> boids, final JPFBoidsModel model,
                         final CustomCyclicBarrier barrier, final MultiAdministrator multiAdministrator) {
        this.boids = boids;
        this.multiAdministrator = multiAdministrator;
        this.barrier = barrier;
        this.model = model;
    }

    @Override
    public void run() {
        int counter = 0;
        while(counter < JPFBoidsController.NUM_ITERATIONS)  {
            for (JPFBoid boid : boids) {
                boid.updateVelocity(model);
            }


            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException ignored) {
            }

            for (JPFBoid boid : boids) {
                boid.updatePos(model);
            }

            this.multiAdministrator.threadDone();
            counter++;
        }
    }

}
