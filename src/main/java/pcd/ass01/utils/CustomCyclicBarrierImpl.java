package pcd.ass01.utils;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CustomCyclicBarrierImpl implements CustomCyclicBarrier {

    private final int waitingThreadNum;
    private int arrivedThreadNum;
    private final ReentrantLock lock;
    private final Condition condition;

    public CustomCyclicBarrierImpl(int waitingThreadNum) {
        this.waitingThreadNum = waitingThreadNum;
        this.arrivedThreadNum = 0;
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
    }

    @Override
    public void await() throws InterruptedException, BrokenBarrierException {
        lock.lock();
        try {
            arrivedThreadNum++;

            if (arrivedThreadNum < waitingThreadNum) {
                this.condition.await();
            } else {
                arrivedThreadNum = 0;
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
            arrivedThreadNum = 0;
            this.condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
