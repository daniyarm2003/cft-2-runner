package com.cft.fighters;

import java.util.Random;

public enum FighterHealthClass {
    BLUE_HEART("BLH", 1.0, 49.0),
    PURPLE_HEART("PH", 51.0, 99.0),
    GREEN_HEART("GH", 101.0, 199.0),
    RED_HEART("RH", 201.0, 499.0),
    BROWN_HEART("BRH", 501.0, 999.0);

    private final String shortName;
    private final double minHp;
    private final double maxHp;

    FighterHealthClass(String shortName, double minHp, double maxHp) {
        this.shortName = shortName;
        this.minHp = minHp;
        this.maxHp = maxHp;
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

    public double generateHealthValue(Random random) {
        double range = this.getMaxHp() - this.getMinHp();

        double mean = this.getMinHp() + range / 2.0;
        double stdDev = Math.sqrt(range / 2.0);

        double hp = random.nextGaussian(mean, stdDev);

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

        return BROWN_HEART;
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
