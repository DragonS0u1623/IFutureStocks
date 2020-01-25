package com.dragons0u1.ifuture.helpers;

import net.milkbowl.vault.economy.*;
import org.bukkit.*;
import org.bukkit.plugin.*;

public class VaultHelper {
	private static final String VAULT = "Vault";
	
	private static Economy ecoVault = null;
	private static boolean vaultLoaded = false;
	
	public static Economy getEconomy(){
		if(!vaultLoaded){
			vaultLoaded = true;
			Server theServer = Bukkit.getServer();
			if (theServer.getPluginManager().getPlugin(VAULT) != null){
				RegisteredServiceProvider<Economy> rsp = theServer.getServicesManager().getRegistration(Economy.class);
				if(rsp!=null){
					ecoVault = rsp.getProvider();
				}
			}
		}
		return ecoVault;
	}
}
