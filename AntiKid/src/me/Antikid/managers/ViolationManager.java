package me.Antikid.managers;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.Antikid.module.Module;
import me.Antikid.types.BanReason;
import me.Antikid.types.PlayerData;
import me.Antikid.types.Roast;
import me.Antikid.types.Violation;
import me.Antikid.utils.BanUtils;
import me.Antikid.utils.ServerUtils;

public class ViolationManager {

    private Player player;
    private HashMap<Module, Violation> violations = new HashMap<>();

    public ViolationManager(Player player) {
	this.player = player;
    }

    public void updateViolations() {
	PlayerData pd = PlayerData.getPlayerData(player);
	Roast roast = pd.getRoast();

	getViolations().values().forEach(violation -> {

	    Module module = violation.getModule();

	    if (module.getViolationsAlert() != -1 && violation.getViolations() >= module.getViolationsBan()) {
		BanUtils.ban(Bukkit.getConsoleSender(), pd.getPlayer(), BanReason.FLY);

	    } else if (module.getViolationsRoast() != -1 && violation.getViolations() >= module.getViolationsRoast()) {
		roast.addRoast();

	    } else if (module.getViolationsBan() != -1 && violation.getViolations() == violation.getModule().getViolationsAlert()) {
		ServerUtils.alert(pd.getPlayer(), "administration", module.getName(), violation.getViolations());
	    }

	    saveViolations(module);
	    resetViolations(module);
	});
    }

    public void addViolation(Module module, int numberOfViolations) {

	Violation violation = null;

	if (!hasViolation(module)) {
	    violation = new Violation(player, module);
	}

	violation = getViolations().get(module);
	violation.setViolations(violation.getViolations() + numberOfViolations);
    }

    public int getViolationAmount(Module module) {

	if (!hasViolation(module)) { return 0; }
	return getViolations().get(module).getViolations();
    }

    public boolean hasViolation(Module module) {
	return violations.containsKey(module);
    }

    public HashMap<Module, Violation> getViolations() {
	return violations;
    }

    public void resetViolations(Module module) {
	getViolations().get(module).reset();
    }

    public void saveViolations(Module module) {
	// save violations offline
    }

    public void handleViolations() {
	if (getViolations().isEmpty()) { return; }

	getViolations().values().forEach(violation -> {
	    violation.handle();
	});
    }
}
