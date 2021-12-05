package ru.bmstu.iu9;

import java.util.Random;

public class RandomServer {
    private Random random;

    public Random getRandom() {
        return random;
    }

    public RandomServer(Random random){
        this.random = random;
    }

}
