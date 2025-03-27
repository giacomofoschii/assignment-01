package pcd.ass01;

import java.util.*;
import java.util.concurrent.CyclicBarrier;

public class BoidsSimulator {

    private BoidsModel model;
    private Optional<BoidsView> view;
    private final Administrator administrator;
    private final CyclicBarrier barrier;
    private final int numThreads;
    private final Queue<BoidThread> threads;
    private volatile boolean running = true;
    private volatile boolean paused = false;
    
    private static final int FRAMERATE = 25;
    private int framerate;
    
    public BoidsSimulator(BoidsModel model) {
        this.model = model;
        this.view = Optional.empty();
        this.numThreads = Runtime.getRuntime().availableProcessors() + 1;
        this.threads = new LinkedList<>();
        this.administrator = new Administrator(numThreads);
        this.barrier = new CyclicBarrier(numThreads);


        List<Boid> boids = model.getBoids();
        for (int i = 0; i < numThreads; i++) {
            threads.add(new BoidThread(getThreadPool(i, boids), model, barrier, administrator));
        }
    }

    public void attachView(BoidsView view) {
    	this.view = Optional.of(view);
    }
      
    public void runSimulation() {
        for (Thread thread : threads) {
            thread.start();
        }
    
        while (running) {
            
            var t0 = System.currentTimeMillis();
            administrator.waitThreads();
    
            if (view.isPresent()) {
                view.get().update(framerate);
                var t1 = System.currentTimeMillis();
                var dtElapsed = t1 - t0;
                var frameRatePeriod = 1000 / FRAMERATE;
    
                if (dtElapsed < frameRatePeriod) {
                    try {
                        Thread.sleep(frameRatePeriod - dtElapsed);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    framerate = FRAMERATE;
                } else {
                    framerate = (int) (1000 / dtElapsed);
                }
            }
    
            administrator.signalDone();
        }
    }
    

    private List<Boid> getThreadPool(int threadIndex, List<Boid> boids) {
        int poolSize = boids.size() / numThreads;
        int start = threadIndex * poolSize;
        int end = (threadIndex == numThreads - 1) ? boids.size() : start + poolSize;
        return new ArrayList<>(boids.subList(start, end));
    }

    public synchronized void pauseSimulation() {
        model.setPaused(true);
    }
    
    public synchronized void resumeSimulation() {
        model.setPaused(false);
    }
    
    

    public synchronized void stopSimulation() {
        running = false;
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }
}
