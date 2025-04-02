package pcd.ass01.taskexecutor;

import pcd.ass01.BoidsController;
import pcd.ass01.BoidsModel;
import pcd.ass01.BoidsSimulator;
import pcd.ass01.BoidsView;

public class TaskController extends BoidsController {
    private BoidsSimulator simulator;

    public void initialize() {
        this.model = new BoidsModel(
                SEPARATION_WEIGHT, ALIGNMENT_WEIGHT, COHESION_WEIGHT,
                ENVIRONMENT_WIDTH, ENVIRONMENT_HEIGHT,
                MAX_SPEED,
                PERCEPTION_RADIUS,
                AVOID_RADIUS);
        this.view = new BoidsView(this, SCREEN_WIDTH, SCREEN_HEIGHT);
        this.simulator = new TaskSimulator(this);
        this.simulator.runSimulation();
    }

    public BoidsSimulator getSimulator() {
        return simulator;
    }
}
