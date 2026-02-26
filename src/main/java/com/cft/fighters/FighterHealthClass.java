package com.cft.fighters;

import java.util.Random;

public enum FighterHealthClass {
    BLUE_HEART("BLH", 1.0, 50.0, 0.8),
    PURPLE_HEART("PH", 51.0, 100.0, 0.5),
    GREEN_HEART("GH", 101.0, 200.0, 0.5),
    RED_HEART("RH", 201.0, 500.0, 0.5),
    BROWN_HEART("BRH", 501.0, 1000.0, 0.2);

    private final String shortName;
    private final double minHp;
    private final double maxHp;
    private final double skew;

    FighterHealthClass(String shortName, double minHp, double maxHp, double skew) {
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

    private double getSkew() {
        return this.skew;
    }

    public double generateHealthValue(Random random) {
        double range = this.getMaxHp() - this.getMinHp();
        double center = (1.0 - this.getSkew()) * this.getMinHp() + this.getSkew() * this.getMaxHp();
        double stdDev = Math.sqrt(range);

        double stdDevLeft = stdDev * this.getSkew();
        double stdDevRight = stdDev * (1.0 - this.getSkew());

        double mag = Math.abs(random.nextGaussian());
        boolean isLeft = random.nextDouble() < this.getSkew();

        double hp = center + mag * (isLeft ? -stdDevLeft : stdDevRight);

        hp = Math.min(hp, this.getMaxHp());
        hp = Math.max(hp, this.getMinHp());

        return hp;
    }

    public static FighterHealthClass getHealthClassByHealthValue(double healthValue) {
        for (FighterHealthClass healthClass : values()) {
            if(healthValue >= healthClass.getMinHp() && healthValue <= healthClass.getMaxHp()) {
                return healthClass;
            }
        }

        return BLUE_HEART;
    }

    public static FighterHealthClass findHealthClassByName(String name) {
        for (FighterHealthClass healthClass : values()) {
            if(healthClass.getShortName().equals(name) || healthClass.getHeartClassName().equals(name) || healthClass.name().equals(name)) {
                return healthClass;
            }
        }

        return null;
    }
}
