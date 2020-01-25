package com.dragons0u1.ifuture.managers;

import com.dragons0u1.ifuture.*;
import org.bukkit.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;

public class StockMarket {
	private IFutureStocks plugin;
	private Random r = new Random();
	public HashMap<UUID, PlayerStocks> playerStocks = new HashMap<UUID, PlayerStocks>();
	public HashMap<String, Integer> companies = new HashMap<String, Integer>();
	
	public StockMarket(IFutureStocks plugin) {
		this.plugin = plugin;
		companies.put("Apple", r.nextInt(20));
		companies.put("Microsoft", r.nextInt(20));
		companies.put("Amazon", r.nextInt(20));
		companies.put("Google", r.nextInt(20));
		companies.put("Facebook", r.nextInt(20));
	}
	
	public void savePlayerStocks() throws FileNotFoundException, IOException {
		for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
			File playerStocksFile = new File("IFuture/playerStocks.dat");
			ObjectOutputStream output = new ObjectOutputStream(
					new GZIPOutputStream(new FileOutputStream(playerStocksFile)));
			if (playerStocks.get(p.getUniqueId()) != null) {
				playerStocks.put(p.getUniqueId(), playerStocks.get(p.getUniqueId()));
			}
			
			try {
				output.writeObject(playerStocks);
				output.flush();
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void saveCompanies() throws FileNotFoundException, IOException {
		for (String company : companies.keySet()) {
			File companiesFile = new File("IFuture/companies.dat");
			ObjectOutputStream output = new ObjectOutputStream(
					new GZIPOutputStream(new FileOutputStream(companiesFile)));
			if (companies.get(company) != null) {
				companies.put(company, companies.get(company));
			}
			
			try {
				output.writeObject(companies);
				output.flush();
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void loadPlayerStocks() throws FileNotFoundException, IOException, ClassNotFoundException {
		File playerStocksFile = new File("IFuture/playerStocks.dat");
		if (playerStocksFile != null) {
			ObjectInputStream input = new ObjectInputStream(new GZIPInputStream(new FileInputStream(playerStocksFile)));
			Object readObject = input.readObject();
			input.close();
			if (!(readObject instanceof HashMap))
				throw new IOException("ERROR: Data is not a HashMap");
			playerStocks = (HashMap<UUID, PlayerStocks>) readObject;
			for (UUID key : playerStocks.keySet()) {
				playerStocks.put(key, playerStocks.get(key));
			}
		}
		else {
			System.out.println("No Player Stocks file found");
		}
	}
	
	public void loadCompanies() throws FileNotFoundException, IOException, ClassNotFoundException {
		File companiesFile = new File("IFuture/companies.dat");
		if (companiesFile != null) {
			ObjectInputStream input = new ObjectInputStream(new GZIPInputStream(new FileInputStream(companiesFile)));
			Object readObject = input.readObject();
			input.close();
			if (!(readObject instanceof HashMap))
				throw new IOException("ERROR: Data is not a HashMap");
			companies = (HashMap<String, Integer>) readObject;
			for (String key : companies.keySet()) {
				companies.put(key, companies.get(key));
			}
		}
		else {
			System.out.println("No Companies file found");
		}
	}
	
	public boolean addCompany(String company) {
		if (!companies.containsKey(company)) {
			companies.put(company, r.nextInt(20));
			return true;
		}
		return false;
	}
	
	public boolean removeCompany(String company) {
		if (companies.containsKey(company)) {
			companies.remove(company);
			return true;
		}
		return false;
	}
	
	public int buyStocks(UUID player, String company, int amount) {
		PlayerStocks user = playerStocks.get(player);
		int current = user.getStocks(company);
		user.buyStocks(company, amount);
		updateStocks();
		int delta = user.getStocks(company) - current;
		return delta;
	}
	
	public int sellStocks(UUID player, String company, int amount) {
		PlayerStocks user = playerStocks.get(player);
		int current = user.getStocks(company);
		user.sellStocks(company, amount);
		updateStocks();
		int delta = current - user.getStocks(company);
		return delta;
	}
	
	public void updateStocks() {
		int delta = r.nextInt(5), pos = r.nextInt(1);
		for (String company : companies.keySet()) {
			if(pos == 1) {
				companies.put(company, companies.get(company) + delta);
			}
			else {
				companies.put(company, companies.get(company) - delta);
			}
		}
	}
	
	public String showStocks() {
		String returnable = "";
		for (String key : companies.keySet()) {
			returnable += ChatColor.GREEN + key + ChatColor.WHITE + ": $" + ChatColor.GOLD + companies.get(key) + "\n";
		}
		return returnable;
	}
	
	public String showStocks(String company) {
		String returnable = ChatColor.GREEN + company + ChatColor.WHITE + ": $" + ChatColor.GOLD + companies.get(company) + "\n";
		return returnable;
	}
}
