package me.Antikid.types;

public enum LadderMovementType {

    UPWARDS(
	    0.13,
	    1),
    DOWNWARDS(
	    0.16,
	    2);

    private Double maxMetersPerSecond;
    private Integer multiplier;

    private LadderMovementType(Double maxMetersPerSecond, Integer multiplier) {
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
