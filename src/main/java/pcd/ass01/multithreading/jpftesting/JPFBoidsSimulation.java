package pcd.ass01.multithreading.jpftesting;

public class JPFBoidsSimulation {

    public static void main(String[] args) {
        var model = new JPFBoidsModel(10);
        var simulator = new JPFBoidsSimulator(model);
        simulator.runSimulation();
    }
}
