package com.dragons0u1.ifuture.tasks;

import com.dragons0u1.ifuture.*;
import org.bukkit.scheduler.*;


public class CheckActiveHours extends BukkitRunnable {
	
	private IFutureStocks plugin;
	
	public CheckActiveHours(IFutureStocks plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		if (!plugin.isActiveHours()) {
			plugin.getServer().broadcastMessage("[IFutureStocks] Buying and selling disabled due to outside active hours. Plugin was NOT disabled.");
			plugin.isActiveHours = false;
		}
		else {
			plugin.getServer().broadcastMessage("[IFutureStocks] Buying and selling has been re-enabled due to active hours.");
			plugin.isActiveHours = true;
		}
	}
}
