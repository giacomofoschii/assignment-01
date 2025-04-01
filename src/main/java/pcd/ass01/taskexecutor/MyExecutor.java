package pcd.ass01.taskexecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public abstract class MyExecutor implements ExecutorService {

    @Override
    public void execute(Runnable command) {


    }


    @Override
    public Future<?> submit(Runnable task) {
        return null;
    }
}
