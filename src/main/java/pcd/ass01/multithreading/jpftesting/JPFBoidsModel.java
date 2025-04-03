package pcd.ass01.multithreading.jpftesting;

import java.util.ArrayList;
import java.util.List;

public class JPFBoidsModel {

    private final List<JPFBoid> boids;

    public JPFBoidsModel(){
        boids = new ArrayList<>();
    }

    public void setBoidsNumber(int nBoids) {
        boids.clear();
        for (int i = 0; i < nBoids; i++) {
            boids.add(new JPFBoid());
        }
    }

    public List<JPFBoid> getBoids(){
        return boids;
    }
}