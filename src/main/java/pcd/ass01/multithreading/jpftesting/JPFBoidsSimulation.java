package pcd.ass01.multithreading.jpftesting;

public class JPFBoidsSimulation {

    public static void main(String[] args) {
        var boidsController = new JPFBoidsController();
        boidsController.getModel().setBoidsNumber(10);
        boidsController.runSimulation();
    }
}
