package pcd.ass01.multithreading.jpftesting;

public class JPFBoidsController {
    private JPFBoidsSimulator simulator;
    private JPFBoidsModel model;

    public JPFBoidsModel getModel() {
        return this.model;
    }

    public void initialize() {
        this.model = new JPFBoidsModel();
        this.model.setBoidsNumber(10);
        this.simulator = new JPFBoidsSimulator(this);
        this.simulator.startThreads();
        this.simulator.runSimulation();
        this.model.setBoidsNumber(50);
        this.simulator.newSimulation();
    }

}
