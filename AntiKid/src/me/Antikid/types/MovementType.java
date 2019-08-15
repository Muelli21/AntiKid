package me.Antikid.types;

public enum MovementType {

    SNEAKING(
	    0.2,
	    2),
    SNEAKING_BACKWARDS(
	    0.1,
	    2),
    COBWEB(
	    0.1,
	    5),
    NORMAL(
	    0.29,
	    3),

    JUMPING(
	    0.62,
	    1),
    JUMPING_BACKWARDS(
	    0.3,
	    1),
    JUMPING_ON_ICE(
	    0.58,
	    1),
    JUMPING_ON_ICE_UNDER_BLOCK(
	    0.92,
	    1),
    JUMPING_UNDER_BLOCK(
	    0.89,
	    1),

    ON_ICE(
	    0.39,
	    1),
    ON_ICE_UNDER_BLOCK(
	    0.92,
	    1),
    UNDER_BLOCK(
	    0.68,
	    1);

    private Double maxMetersPerSecond;
    private Integer multiplier;

    private MovementType(Double maxMetersPerSecond, Integer multiplier) {
	this.maxMetersPerSecond = maxMetersPerSecond;
	this.multiplier = multiplier;
    }

    public Double getMaxMetersPerSecond() {
	return maxMetersPerSecond;
    }

    public Integer getMultiplier() {
	return multiplier;
    }
}
