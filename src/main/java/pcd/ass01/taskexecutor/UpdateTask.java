package pcd.ass01.taskexecutor;

/**
 * Interface representing a task that can be executed to update the state of boids.
 */
public interface UpdateTask extends Runnable {

    /**
     * Executes the task to update the state of boids.
     * This method should contain the logic for updating the boids' positions or velocities.
     */
    @Override
    void run();
}
