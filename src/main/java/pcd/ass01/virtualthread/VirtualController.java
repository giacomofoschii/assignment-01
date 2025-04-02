package pcd.ass01.virtualthread;

import pcd.ass01.BoidsController;

public class VirtualController extends BoidsController {

    private final CustomCyclicBarrier barrier;

    public VirtualController() {
        super();
        this.barrier = new CustomCyclicBarrierImpl(numThreads);
    }

    @Override
    public void runSimulation() {
        while (running) {

            updateView();

        }
    }

    @Override
    public synchronized void resumeSimulation() {
        this.paused = false;
        notifyAll();
    }

    @Override
    public synchronized void newSimulation() {
        running = true;

        resumeSimulation();
        new Thread(this::runSimulation).start();
    }

    @Override
    public synchronized void stopSimulation() {
        paused = false;
        running = false;

    }

    public boolean isPaused() {
        return paused;
    };
}
