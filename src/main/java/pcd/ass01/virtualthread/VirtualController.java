package pcd.ass01.virtualthread;

import pcd.ass01.Boid;
import pcd.ass01.BoidsController;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class VirtualController extends BoidsController {

    private final CustomCyclicBarrier barrier;
    private final Queue<BoidVirtualThread> threads;
    private final Administrator administrator;
    private final ReentrantLock lock;

    public VirtualController() {
        super();
        this.barrier = new CustomCyclicBarrierImpl(numThreads);
        this.threads = new LinkedList<>();
        this.administrator = new Administrator();
        this.lock = new ReentrantLock();
    }

    public void startThreads() {
        administrator.setThreadNumber(model.getBoids().size());
        for(Boid boid : model.getBoids()) {
            BoidVirtualThread boidThread = new BoidVirtualThread(boid, barrier, administrator, this);
            threads.add(boidThread);
            Thread.ofVirtual().start(boidThread);
        }
    }

    @Override
    public void runSimulation() {
        startThreads();
        while (running) {
            administrator.waitThreads();

            updateView();

            administrator.signalDone();
        }
    }

    @Override
    public void resumeSimulation() {
        lock.lock();
        try {
            this.paused = false;
            notifyAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void newSimulation() {
        lock.lock();
        try {
            running = true;
            this.threads.clear();
            resumeSimulation();
            new Thread(this::runSimulation).start();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void stopSimulation() {
        lock.lock();
        try {
            paused = false;
            running = false;
            for (BoidVirtualThread thread : threads) {
                thread.setStopped(true);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void pauseSimulation() {
        lock.lock();
        try {
            paused = true;
        } finally {
            lock.unlock();
        }
    }

    public boolean isPaused() {
        return paused;
    };
}
