// Copyright (C) 2020 Ryan Bester
// This code is licensed under the MIT license

package com.ryanbester.serverhider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

public class ServerHider extends Plugin implements Listener {
	Configuration config;
	
	boolean ignoreCase = true;
	String action = "exclude";
	List<String> servers = new ArrayList<String>();
	
	@Override
	public void onEnable() {
		loadConfig();
		
		getProxy().getPluginManager().registerListener(this, this);
	}
	
	void loadConfig() {
		if(!getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}
		
		File configFile = new File(getDataFolder(), "config.yml");
		if(!configFile.exists()) {
			try {
				configFile.createNewFile();
				InputStream is = getResourceAsStream("config.yml");
				OutputStream os = new FileOutputStream(configFile);
				ByteStreams.copy(is, os);
			} catch (Exception e) {
				getLogger().warning("Cannot create config file");
			}
		}
		
		try {
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
		} catch (Exception e) {
			getLogger().warning("Cannot parse config file");
		}
		
		ignoreCase = config.getBoolean("ignoreCase");
		action = config.getString("action");
				
		if(!action.contentEquals("exclude") && !action.contentEquals("include")) {
			getLogger().warning("Action has to be either include or exclude (defaults to exclude)");
			action = "exclude";
		}
		
		servers.clear();
		servers = config.getStringList("servers");
		
		// Set all servers to lower case if ignoreCase is true
		if(ignoreCase) {
			ListIterator<String> iterator = servers.listIterator();
			while (iterator.hasNext()) {
				iterator.set(iterator.next().toLowerCase());
			}
		}
	}
	
	@EventHandler
	public void onTabComplete(TabCompleteEvent event) {
		String command = event.getCursor();
		
		if(command.startsWith("/server ")) {
			List<String> suggestions = event.getSuggestions();
			
			if(action.contentEquals("include")) {
				List<String> newSuggestions = new ArrayList<String>(suggestions);
				
				suggestions.clear();
				
				for(String suggestion : newSuggestions) {
					String formattedSuggestion = suggestion;
					
					if(ignoreCase) formattedSuggestion = suggestion.toLowerCase();
					
					if(servers.contains(formattedSuggestion)) {
						suggestions.add(suggestion);
					}
				}
			} else {
				ListIterator<String> suggestionsIterator = suggestions.listIterator();
				
				while(suggestionsIterator.hasNext()) {
					String formattedSuggestion = suggestionsIterator.next();
					
					if(ignoreCase) formattedSuggestion = formattedSuggestion.toLowerCase();
					
					getLogger().info(formattedSuggestion);
					
					if(servers.contains(formattedSuggestion)) {
						suggestionsIterator.remove();
					}
				}
			}
		}
	}
}
