package pcd.ass01.utils;

public interface CustomCountDownLatch {
    void await() throws InterruptedException;

    void countDown();
}
