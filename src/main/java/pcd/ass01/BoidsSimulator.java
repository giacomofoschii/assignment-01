package pcd.ass01;

import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class BoidsSimulator {

    private BoidsModel model;
    private Optional<BoidsView> view;
    private final int numThreads;
    private final Thread[] threads;
    private volatile boolean running = true;
    private volatile boolean paused = false;
    
    private static final int FRAMERATE = 25;
    private int framerate;
    
    public BoidsSimulator(BoidsModel model) {
        this.model = model;
        view = Optional.empty();
        this.numThreads = Runtime.getRuntime().availableProcessors() + 1;
        this.threads = new Thread[numThreads];
    }

    public void attachView(BoidsView view) {
    	this.view = Optional.of(view);
    }
      
    public void runSimulation() {
        for (int i = 0 ; i < numThreads; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                while (running) {
                    if (paused) {
                        synchronized (this) {
                            try {
                                this.wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
                    var boids = new CopyOnWriteArrayList<>(model.getBoids());
                    int poolSize = boids.size() / numThreads;
                    int start = threadIndex * poolSize;
                    int end = (threadIndex == numThreads - 1) ? boids.size() : start + poolSize;

                    for (int j = start; j < end; j++) {
                        boids.get(j).updateVelocity(model);
                    }
                    for (int j = start; j < end; j++) {
                        boids.get(j).updatePos(model);
                    }
                }
            });
            threads[i].start();
        }

        while (running) {
            var t0 = System.currentTimeMillis();
            if (view.isPresent()) {
                view.get().update(framerate);
                var t1 = System.currentTimeMillis();
                var dtElapsed = t1 - t0;
                var framratePeriod = 1000/FRAMERATE;

                if (dtElapsed < framratePeriod) {
                    try {
                        Thread.sleep(framratePeriod - dtElapsed);
                    } catch (Exception ex) {}
                    framerate = FRAMERATE;
                } else {
                    framerate = (int) (1000/dtElapsed);
                }
            }

        }
    }

    public void pauseSimulation() {
        paused=true;
    }

    public void resumeSimulation() {
        paused = false;
        synchronized (this) {
            notifyAll();
        }
    }

    public void stopSimulation() {
        running = false;
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }
}
