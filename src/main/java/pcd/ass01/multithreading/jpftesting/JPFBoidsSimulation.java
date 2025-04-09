package pcd.ass01.multithreading.jpftesting;

/*
    Author:
    - Giacomo Foschi (giacomo.foschi2@studio.unibo.it)
    - Giovanni Pisoni (giovanni.pisoni@studio.unibo.it)
    - Giovanni Rinchiuso (giovanni.rinchiuso@studio.unibo.it)
    - Gioele Santi (gioele.santi2@studio.unibo.it)
 */
public class JPFBoidsSimulation {

    public static void main(String[] args) {
        var boidsController = new JPFBoidsController();
        boidsController.runSimulation();
    }
}
