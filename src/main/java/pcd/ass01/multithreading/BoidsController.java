package pcd.ass01.multithreading;

public class BoidsController {

    BoidsSimulator boidsSimulator;

    public void setBoidsSimulator(final BoidsSimulator boidsSimulator) {
        this.boidsSimulator = boidsSimulator;
    }

    public void initializeBoidsSimulator(final BoidsModel model) {
        this.boidsSimulator = new BoidsSimulator(model);
    }

    public BoidsSimulator getSimulator() {
        return this.boidsSimulator;
    }

}
