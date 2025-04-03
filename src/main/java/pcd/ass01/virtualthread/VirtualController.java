package pcd.ass01.virtualthread;

import pcd.ass01.Boid;
import pcd.ass01.BoidsController;
import pcd.ass01.utils.CustomCyclicBarrier;
import pcd.ass01.utils.CustomCyclicBarrierImpl;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class VirtualController extends BoidsController {

    private final CustomCyclicBarrier barrier;
    private final Queue<BoidVirtualThread> threads;
    private final VirtualAdministrator virtualAdministrator;
    private final ReentrantLock lock;
    private final Condition condition;

    public VirtualController() {
        super();
        this.barrier = new CustomCyclicBarrierImpl(numThreads);
        this.threads = new LinkedList<>();
        this.virtualAdministrator = new VirtualAdministrator();
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
    }

    public void startThreads() {
        virtualAdministrator.setThreadNumber(model.getBoids().size());
        for(Boid boid : model.getBoids()) {
            BoidVirtualThread boidThread = new BoidVirtualThread(boid, barrier, virtualAdministrator, this, lock, condition);
            threads.add(boidThread);
            Thread.ofVirtual().start(boidThread);
        }
    }

    @Override
    public void runSimulation() {
        startThreads();
        while (running) {
            virtualAdministrator.waitThreads();

            updateView();

            virtualAdministrator.signalDone();
        }
    }

    @Override
    public void resumeSimulation() {
        lock.lock();
        try {
            this.paused = false;
            this.condition.signalAll();
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
    }
}
