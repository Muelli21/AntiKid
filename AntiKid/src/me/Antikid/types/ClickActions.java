package me.Antikid.types;

public class ClickActions {

    private int blockhit = 1;
    private int hit = 1;
    private int blockInteract = 1;
    private int interact = 1;
    private int sneakHit = 1;

    private long lastCheck = System.currentTimeMillis();

    public int getTotalHitAmount() {
	return blockhit + blockInteract + hit + interact;
    }

    public int getBlockInteract() {
	return blockInteract;
    }

    public void setBlockInteract(int blockInteract) {
	this.blockInteract = blockInteract;
    }

    public void addBlockInteract() {
	blockInteract++;
    }

    public int getInteract() {
	return interact;
    }

    public void setInteract(int interact) {
	this.interact = interact;
    }

    public void addInteract() {
	interact++;
    }

    public int getHit() {
	return hit;
    }

    public void setHit(int hit) {
	this.hit = hit;
    }

    public void addHit() {
	hit++;
    }

    public int getBlockhit() {
	return blockhit;
    }

    public void setBlockhit(int blockhit) {
	this.blockhit = blockhit;
    }

    public void addBlockhit() {
	blockhit++;
    }

    public int getSneakHit() {
	return sneakHit;
    }

    public void setSneakHit(int sneakHit) {
	this.sneakHit = sneakHit;
    }

    public void addSneakHit() {
	sneakHit++;
    }

    public long getLastCheck() {
	return lastCheck;
    }

    public void setLastCheck(long lastCheck) {
	this.lastCheck = lastCheck;
    }
}
