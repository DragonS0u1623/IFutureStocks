package com.dragons0u1.ifuture;

import com.dragons0u1.ifuture.commands.*;
import com.dragons0u1.ifuture.managers.*;
import com.dragons0u1.ifuture.tasks.*;
import org.bukkit.plugin.java.*;
import org.bukkit.scheduler.*;

import java.io.*;
import java.time.*;

public final class IFutureStocks extends JavaPlugin {
	
	public boolean isActiveHours;
	public int statusMessages = 0;
	
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
			this.getServer().getPluginManager().disablePlugin(this);
			this.getLogger().info("Plugin disabled due to Vault not being found!");
		}
		if (this.getConfig().getString("on/off").equalsIgnoreCase("off"))
			this.getLogger().info("Plugin disabled due to config being off. Enable it if you want to use these features");
		
		StockMarket sm = new StockMarket(this);
		
		try {
			sm.loadPlayerStocks();
			sm.loadCompanies();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		registerCommands();
		@SuppressWarnings("unused")
		BukkitTask check = new CheckActiveHours(this).runTaskTimer(this, 0L, 20*60*5L);
		if (isActiveHours) {
			@SuppressWarnings("unused")
			BukkitTask su = new StocksUpdate(this, sm).runTaskTimer(this, 20L, 20*60*5L);
		}
	}
	
	@Override
	public void onDisable() {
		StockMarket sm = new StockMarket(this);
		try {
			sm.savePlayerStocks();
			sm.saveCompanies();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void registerCommands() {
		new StockMarketCommands(this);
	}
	
	public boolean isActiveHours() {
		LocalTime timeNow = LocalTime.now(ZoneId.of(this.getConfig().getString("timezone")));
		if ((timeNow.getHour() < this.getConfig().getInt("buy/sell.start"))
				|| (timeNow.getHour() > this.getConfig().getInt("buy/sell.end")))
			return false;
		else
			return true;
	}
	
}
