package pcd.ass01.utils;

/**
 * This interface defines the methods for an administrator that manages the synchronization
 * of multiple threads. It provides methods to signal when a thread has completed its task,
 * wait for all threads to finish, and signal that all threads are done.
 */
public interface Administrator {

    /**
     * Signals that a thread has completed its task.
     */
    void threadDone();

    /**
     * Waits for all threads to finish their tasks.
     */
    void waitThreads();

    /**
     * Signals that all threads are done.
     */
    void signalDone();
}
