package pcd.ass01.utils;

/**
 * CustomCountDownLatch is a simple implementation of a countdown latch.
 */
public interface CustomCountDownLatch {

    /**
     * Waits until the latch has counted down to zero.
     *
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    void await() throws InterruptedException;

    /**
     * Decrements the count of the latch, releasing all waiting threads if
     * the count reaches zero.
     */
    void countDown();
}
