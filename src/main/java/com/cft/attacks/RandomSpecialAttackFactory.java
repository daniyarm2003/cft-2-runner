package com.cft.attacks;

import com.cft.utils.IntColorUtils;

import java.util.Random;

public class RandomSpecialAttackFactory implements SpecialAttackFactory {

    private final Random random;

    public RandomSpecialAttackFactory(Random random) {
        this.random = random;
    }

    @Override
    public SpecialAttack createSpecialAttack(SpecialAttack.Type attackType) {
        switch(attackType) {
            case BALL -> {
                int ballColor = IntColorUtils.COMMON_COLORS[this.random.nextInt(IntColorUtils.COMMON_COLORS.length)];
                return new BallAttack(ballColor);
            }
            case SMALL_PROJECTILE -> {
                SmallProjectileAttack.Type smallProjectileType = SmallProjectileAttack.Type.values()[this.random.nextInt(SmallProjectileAttack.Type.values().length)];
                return new SmallProjectileAttack(smallProjectileType);
            }
            default -> {
                return new LightningAttack();
            }
        }
    }
}
