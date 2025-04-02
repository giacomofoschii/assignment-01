package pcd.ass01.virtualthread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CustomCyclicBarrierImpl implements CustomCyclicBarrier {

    private final int waitingThreadNum;
    private int arrivedThreadNum;
    private boolean broken = false;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public CustomCyclicBarrierImpl(int waitingThreadNum) {
        this.waitingThreadNum = waitingThreadNum;
        this.arrivedThreadNum = 0;
    }

    @Override
    public void await() throws InterruptedException, BrokenBarrierException {
        lock.lock();
        try {
            if (broken) {
                throw new BrokenBarrierException();
            }

            arrivedThreadNum++;

            if (arrivedThreadNum < waitingThreadNum) {
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    if (!broken) {
                        broken = true;
                        condition.signalAll();
                    }
                    throw e;
                }
                if (broken) {
                    throw new BrokenBarrierException();
                }
            } else {
                arrivedThreadNum = 0;
                condition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void reset() {
        lock.lock();
        try {
            arrivedThreadNum = 0;
            broken = false;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
