package me.Antikid.types;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class MoveTrail {

    public static HashMap<Player, MoveTrail> trails = new HashMap<>();
    private Player p;
    private LinkedList<Move> moves = new LinkedList<>();

    public MoveTrail(Player p) {
	trails.put(p, this);
    }

    public void addLoc(Location loc, Player p) {

	Entity entity = p;

	if (moves == null) {
	    p.sendMessage("moves null");
	}

	if (moves.size() > 50) {
	    moves.remove(moves.get(0));
	}
	moves.add(new Move(loc, System.currentTimeMillis(), p.isSneaking(), entity.isOnGround()));
    }

    public void clear() {
	moves.clear();
    }

    public void remove() {
	trails.remove(getP(), this);
	this.moves = null;
	this.p = null;
    }

    public Player getP() {
	return p;
    }

    public void setP(Player p) {
	this.p = p;
    }

    public LinkedList<Move> getMoves() {
	return moves;
    }

    public void setMoves(LinkedList<Move> moves) {
	this.moves = moves;
    }

    public Iterator<Move> getLastEntries(long oldest) {

	long time = System.currentTimeMillis() - oldest;
	ListIterator<Move> itr = moves.listIterator();

	while (itr.hasNext()) {
	    if (itr.next().time >= time)
		return moves.listIterator(itr.previousIndex());
	}
	return moves.listIterator(0);
    }

    public class Move {

	private Location loc;
	private long time;
	private boolean sneak = false;
	private boolean onGround = false;

	public Move(Location loc, long time, boolean sneak, boolean onGround) {
	    this.setLoc(loc);
	    this.setTime(time);
	    this.setSneak(sneak);
	    this.setOnGround(onGround);
	}

	public long getTime() {
	    return time;
	}

	public void setTime(long time) {
	    this.time = time;
	}

	public Location getLoc() {
	    return loc;
	}

	public void setLoc(Location loc) {
	    this.loc = loc;
	}

	public boolean isSneak() {
	    return sneak;
	}

	public void setSneak(boolean sneak) {
	    this.sneak = sneak;
	}

	public boolean isOnGround() {
	    return onGround;
	}

	public void setOnGround(boolean onGround) {
	    this.onGround = onGround;
	}
    }
}
