package com.dragons0u1.ifuture.managers;

import org.bukkit.*;
import java.util.*;

public class PlayerStocks {
	public HashMap<String, Integer> stocks = new HashMap<String, Integer>();
	
	public PlayerStocks(String company, int amount) {
		stocks.put(company, amount);
	}
	
	public void buyStocks(String company, int amount) {
		stocks.put(company, stocks.get(company) + amount);
	}
	
	public void sellStocks(String company, int amount) {
		if (stocks.get(company) >= amount)
			stocks.put(company, stocks.get(company) - amount);
		else
			stocks.put(company, 0);
	}
	
	public int getStocks(String company) {
		return stocks.get(company);
	}
	
	public String toString() {
		String returnable = "";
		for (String key : stocks.keySet()) {
			returnable += ChatColor.GREEN + key + ChatColor.WHITE + ": " + ChatColor.GOLD + stocks.get(key) + "\n";
		}
		return returnable;
	}
}
