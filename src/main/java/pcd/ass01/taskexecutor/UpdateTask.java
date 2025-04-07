package pcd.ass01.taskexecutor;

import java.util.concurrent.Callable;

/**
 * Interface representing a task that can be executed to update the state of boids.
 */
public interface UpdateTask extends Callable<Void> {

    /**
     * Executes the task to update the state of boids.
     * This method should contain the logic for updating the boids' positions or velocities.
     *
     * @return null
     */
    @Override
    Void call();
}
