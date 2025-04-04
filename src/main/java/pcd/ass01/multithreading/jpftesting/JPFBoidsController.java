package pcd.ass01.multithreading.jpftesting;

import pcd.ass01.multithreading.MultiAdministrator;
import pcd.ass01.utils.CustomCyclicBarrier;
import pcd.ass01.utils.CustomCyclicBarrierImpl;
import static pcd.ass01.Constants.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class JPFBoidsController {
    private final JPFBoidsModel model;
    private final MultiAdministrator multiAdministrator;
    private final CustomCyclicBarrier barrier;
    private final int numThreads;
    private final LinkedList<JPFBoidThread> threads;

    public JPFBoidsController() {
        numThreads = Runtime.getRuntime().availableProcessors() + 1;
        this.model = new JPFBoidsModel(SEPARATION_WEIGHT, ALIGNMENT_WEIGHT,
                COHESION_WEIGHT, ENVIRONMENT_WIDTH, ENVIRONMENT_HEIGHT,
                MAX_SPEED, PERCEPTION_RADIUS, AVOID_RADIUS, new MockGenerator());
        this.threads = new LinkedList<>();
        this.multiAdministrator = new MultiAdministrator(numThreads);
        this.barrier = new CustomCyclicBarrierImpl(numThreads);
    }

    public JPFBoidsModel getModel() {
        return this.model;
    }

    private void divideBoids() {
        for (int i = 0; i < numThreads; i++) {
            threads.add(new JPFBoidThread(getThreadPool(i), model, barrier, multiAdministrator));
        }
    }

    private void startThreads() {
        for (JPFBoidThread thread : threads) {
            thread.start();
        }
    }

    public void runSimulation() {
        divideBoids();
        startThreads();
        while(true) {
            multiAdministrator.waitThreads();
            multiAdministrator.signalDone();
            break;
        }
    }

    public synchronized void stopSimulation() {
        for (JPFBoidThread thread : threads) {
            thread.interrupt();
        }
    }

    private List<JPFBoid> getThreadPool(int threadIndex) {
        List<JPFBoid> boids = model.getBoids();
        int poolSize = boids.size() / numThreads;
        int start = threadIndex * poolSize;
        int end = (threadIndex == numThreads - 1) ? boids.size() : start + poolSize;
        return new ArrayList<>(boids.subList(start, end));
    }

}
