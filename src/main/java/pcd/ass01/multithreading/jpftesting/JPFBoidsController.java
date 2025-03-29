package pcd.ass01.multithreading.jpftesting;

public class JPFBoidsController {
    private JPFBoidsModel model;

    public JPFBoidsModel getModel() {
        return this.model;
    }

    public void initialize() {
        this.model = new JPFBoidsModel();
        this.model.setBoidsNumber(10);
        JPFBoidsSimulator simulator = new JPFBoidsSimulator(this);
        simulator.startThreads();
        simulator.runSimulation();
        simulator.stopSimulation();
        this.model.setBoidsNumber(50);
        simulator.newSimulation();
        simulator.stopSimulation();
    }

}
