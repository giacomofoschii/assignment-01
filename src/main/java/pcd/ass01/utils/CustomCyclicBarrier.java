package pcd.ass01.utils;

import java.util.concurrent.BrokenBarrierException;

/**
 * CustomCyclicBarrier is a simple implementation of a cyclic barrier.
 */
public interface CustomCyclicBarrier {

    /**
     * Waits until all threads have called this method, or until
     * the barrier is broken, then it signals all waiting threads to
     * proceed.
     *
     * @throws InterruptedException
     * @throws BrokenBarrierException
     */
    void await() throws InterruptedException, BrokenBarrierException;

    /**
     * Resets the barrier to its initial state, signaling all
     * waiting threads to proceed.
     */
    void reset();
}
