package me.Antikid.types;

import me.Antikid.managers.ViolationManager;
import me.Antikid.module.modules.combat.Reach;

public class ViolationManagerRunnable implements Runnable {

    @Override
    public void run() {

	PlayerData.getPlayerDatas().values().forEach(pd -> {
	    pd.setCps(pd.getClicks() / 5);

	    ViolationManager violationManager = pd.getViolationManager();
	    Roast roast = pd.getRoast();

	    violationManager.updateViolations();
	    roast.handleRoasts();
	    violationManager.handleViolations();
	    Reach.handleReach(pd);
	});
    }
}
