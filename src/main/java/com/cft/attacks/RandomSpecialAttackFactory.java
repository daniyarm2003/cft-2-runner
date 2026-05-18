package com.cft.attacks;

import com.cft.utils.IntColorUtils;

import java.util.Random;

public class RandomSpecialAttackFactory implements SpecialAttackFactory {

    private final Random random;

    public RandomSpecialAttackFactory(Random random) {
        this.random = random;
    }

    @Override
    public SpecialAttack createSpecialAttack() {
        int ballColor = IntColorUtils.COMMON_COLORS[this.random.nextInt(IntColorUtils.COMMON_COLORS.length)];
        BallAttack ballAttack = new BallAttack(ballColor);

        SmallProjectileAttack.Type smallProjectileType = SmallProjectileAttack.Type.values()[this.random.nextInt(SmallProjectileAttack.Type.values().length)];
        SmallProjectileAttack smallProjectileAttack = new SmallProjectileAttack(smallProjectileType);

        LightningAttack lightningAttack = new LightningAttack();

        SpecialAttack[] attacks = { ballAttack, smallProjectileAttack, lightningAttack };

        return attacks[this.random.nextInt(attacks.length)];
    }
}
