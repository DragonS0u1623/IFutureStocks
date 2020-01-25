package com.dragons0u1.ifuture.tasks;

import com.dragons0u1.ifuture.*;
import com.dragons0u1.ifuture.managers.*;
import org.bukkit.scheduler.*;

public class StocksUpdate extends BukkitRunnable {
	
	private IFutureStocks plugin;
	private StockMarket sm;
	
	public StocksUpdate(IFutureStocks plugin, StockMarket sm) {
		this.plugin = plugin;
		this.sm = sm;
	}
	
	@Override
	public void run() {
		sm.updateStocks();
		plugin.getLogger().info("Stocks have been updated");
		System.out.println("Stocks have been updated");
	}
	
}
