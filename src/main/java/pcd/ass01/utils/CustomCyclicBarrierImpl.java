package pcd.ass01.utils;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CustomCyclicBarrierImpl implements CustomCyclicBarrier {

    private final int totalThreads;
    private int arrivedThreads;
    private final ReentrantLock lock;
    private final Condition condition;

    public CustomCyclicBarrierImpl(int totalThreads) {
        this.totalThreads = totalThreads;
        this.arrivedThreads = 0;
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
    }

    @Override
    public void await() throws InterruptedException {
        lock.lock();
        try {
            arrivedThreads++;

            if (arrivedThreads < totalThreads) {
                this.condition.await();
            } else {
                arrivedThreads = 0;
                this.condition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void reset() {
        lock.lock();
        try{
            arrivedThreads = 0;
            this.condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
