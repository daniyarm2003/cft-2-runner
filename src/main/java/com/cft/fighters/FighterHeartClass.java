package com.cft.fighters;

import java.util.Random;

public enum FighterHeartClass {
    BLUE_HEART("BLH", 1.0, 50.0, 0.8),
    PURPLE_HEART("PH", 51.0, 100.0, 0.5),
    GREEN_HEART("GH", 101.0, 200.0, 0.5),
    RED_HEART("RH", 201.0, 500.0, 0.5),
    BROWN_HEART("BRH", 501.0, 1000.0, 0.2);

    private final String shortName;
    private final double minHp;
    private final double maxHp;
    private final double skew;

    FighterHeartClass(String shortName, double minHp, double maxHp, double skew) {
        this.shortName = shortName;
        this.minHp = minHp;
        this.maxHp = maxHp;
        this.skew = skew;
    }

    public String getHeartClassName() {
        char firstChar = Character.toUpperCase(this.name().charAt(0));
        String nameSuffix = this.name().substring(1).toLowerCase().replace("_", "");

        return firstChar + nameSuffix;
    }

    public String getShortName() {
        return this.shortName;
    }

    public double getMinHp() {
        return this.minHp;
    }

    public double getMaxHp() {
        return this.maxHp;
    }

    public double getSkew() {
        return this.skew;
    }

    public double generateHealthValue(Random random) {
        return this.generateHealthValue(random, this.getSkew());
    }

    public double generateHealthValue(Random random, double skew) {
        double range = this.getMaxHp() - this.getMinHp();
        double center = (1.0 - skew) * this.getMinHp() + skew * this.getMaxHp();
        double stdDev = Math.sqrt(range);

        double stdDevLeft = stdDev * skew;
        double stdDevRight = stdDev * (1.0 - skew);

        double mag = Math.abs(random.nextGaussian());
        boolean isLeft = random.nextDouble() < skew;

        double hp = center + mag * (isLeft ? -stdDevLeft : stdDevRight);

        hp = Math.min(hp, this.getMaxHp());
        hp = Math.max(hp, this.getMinHp());

        return hp;
    }

    public static FighterHeartClass getHealthClassByHealthValue(double healthValue) {
        for (FighterHeartClass healthClass : values()) {
            if(healthValue <= healthClass.getMaxHp()) {
                return healthClass;
            }
        }

        return BLUE_HEART;
    }

    public static FighterHeartClass findHealthClassByName(String name) {
        for (FighterHeartClass healthClass : values()) {
            if(healthClass.getShortName().equals(name) || healthClass.getHeartClassName().equals(name) || healthClass.name().equals(name)) {
                return healthClass;
            }
        }

        return null;
    }
}
