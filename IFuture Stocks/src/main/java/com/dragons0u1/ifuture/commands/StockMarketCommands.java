package com.dragons0u1.ifuture.commands;

import com.dragons0u1.ifuture.*;
import com.dragons0u1.ifuture.helpers.*;
import com.dragons0u1.ifuture.managers.*;
import net.milkbowl.vault.economy.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class StockMarketCommands implements CommandExecutor {
	
	private IFutureStocks plugin;
	
	public StockMarketCommands(IFutureStocks plugin) {
		this.plugin = plugin;
		plugin.getCommand("stocks").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command!");
			return true;
		}
		Player p = (Player) sender;
		StockMarket sm = new StockMarket(plugin);
		PlayerStocks pl;
		Economy econ = VaultHelper.getEconomy();
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("checkprice")) {
				p.sendMessage("Current stock market and prices:");
				p.sendMessage(sm.showStocks());
				return true;
			}
			else if (args[0].equalsIgnoreCase("check")) {
				if (sm.playerStocks.get(p.getUniqueId()) != null) {
					pl = sm.playerStocks.get(p.getUniqueId());
					p.sendMessage("Your current stocks:");
					p.sendMessage(pl.toString());
				}
				else {
					p.sendMessage(ChatColor.RED + "You don't have any stocks. Please purchase some.");
				}
				return true;
			}
			else {
				p.sendMessage(ChatColor.RED + "Incorrect usage.");
				return false;
			}
		}
		else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("checkprice")){
				if (sm.companies.containsKey(args[1])) {
					p.sendMessage("Current stocks for " + ChatColor.GREEN + args[1]);
					p.sendMessage(sm.showStocks(args[1]));
				}
				else {
					p.sendMessage(ChatColor.RED + "That company doesn't seem to exist. " +
							"Please check your spelling and try again.");
				}
				return true;
			}
			else if (args[0].equalsIgnoreCase("add")) {
				if (sm.addCompany(args[1]))
					p.sendMessage("Added " + ChatColor.GREEN + args[1] + ChatColor.WHITE + " to the stock market");
				else
					p.sendMessage(ChatColor.RED + "ERROR: Company could not be added. " +
							"Either it already exists or another error occurred.");
				return true;
			}
			else if (args[0].equalsIgnoreCase("remove")) {
				if (sm.removeCompany(args[1]))
					p.sendMessage("Removed " + ChatColor.GREEN + args[1] + ChatColor.WHITE + " from the stock market");
				else
					p.sendMessage(ChatColor.RED + "ERROR: Company could not be removed. " +
							"Either it doesn't exist or another error occured.");
				return true;
			}
			else {
				p.sendMessage(ChatColor.RED + "Incorrect usage.");
				return false;
			}
		}
		else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("buy")) {
				if (plugin.isActiveHours()) {
					if (sm.companies.containsKey(args[1])) {
						if (sm.playerStocks.get(p.getUniqueId()) != null) {
							pl = sm.playerStocks.get(p.getUniqueId());
							if (econ.getBalance(p) >= sm.companies.get(args[1])) {
								p.sendMessage("Bought " + ChatColor.GOLD +
										sm.buyStocks(p.getUniqueId(), args[1], Integer.parseInt(args[2])) +
										ChatColor.WHITE + " shares of " + ChatColor.GREEN + args[1]
										+ ChatColor.WHITE + "'s stock");
								p.sendMessage("Your current stocks for " + ChatColor.GREEN + args[1]);
								p.sendMessage(ChatColor.GREEN + args[1] + ChatColor.WHITE + ": " + ChatColor.GOLD +
										pl.getStocks(args[1]));
								econ.withdrawPlayer(p, sm.companies.get(args[1]) * Integer.parseInt(args[2]));
							}
						} else {
							if (econ.getBalance(p) >= sm.companies.get(args[1])) {
								pl = new PlayerStocks(args[1], Integer.parseInt(args[2]));
								p.sendMessage("Current stocks for " + ChatColor.GREEN + args[1]);
								p.sendMessage(ChatColor.GREEN + args[1] + ChatColor.WHITE + ": " + pl.getStocks(args[1]));
							}
						}
						return true;
					}
				}
			}
			else if (args[0].equalsIgnoreCase("sell")) {
				if (plugin.isActiveHours()) {
					if (sm.playerStocks.get(p.getUniqueId()) != null) {
						pl = sm.playerStocks.get(p.getUniqueId());
						if (pl.stocks.containsKey(args[1])) {
							p.sendMessage("Sold " + ChatColor.GOLD +
									sm.sellStocks(p.getUniqueId(), args[1], Integer.parseInt(args[2])) +
									ChatColor.WHITE + " shares of " + ChatColor.GREEN + args[1]
									+ ChatColor.WHITE + "'s stock");
							p.sendMessage("Your current stocks for " + ChatColor.GREEN + args[1]);
							p.sendMessage(ChatColor.GREEN + args[1] + ChatColor.WHITE + ": " + ChatColor.GOLD +
									pl.getStocks(args[1]));
							econ.depositPlayer(p, sm.companies.get(args[1]) * Integer.parseInt(args[2]));
						} else {
							p.sendMessage(ChatColor.RED + "You don't have any of that stock. " +
									"Please check your spelling or buy some.");
						}
					} else {
						p.sendMessage(ChatColor.RED + "You don't have any stocks. Please buy some first.");
					}
					return true;
				}
			}
			else {
				p.sendMessage(ChatColor.RED + "Incorrect usage");
				return false;
			}
		}
		else{
			p.sendMessage(ChatColor.RED + "Incorrect usage.");
		}
		return false;
	}
}
