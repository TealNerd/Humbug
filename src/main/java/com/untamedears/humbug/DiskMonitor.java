package com.untamedears.humbug;

import java.io.File;

import org.bukkit.Bukkit;

import com.untamedears.humbug.annotations.BahHumbug;
import com.untamedears.humbug.annotations.OptType;

public class DiskMonitor implements Runnable {
	private File serverFolder;
	private Config config;

	public DiskMonitor(Humbug plugin) {
		serverFolder = plugin.getDataFolder().getParentFile().getParentFile();
		config = plugin.getHumbugConfig();
	}

	@BahHumbug(opt="disk_space_shutdown", type = OptType.Double, def = "0.02")
	public void run() {
		Double criticalValue = config.get("disk_space_shutdown").getDouble();
		if (((double)serverFolder.getFreeSpace()/(double)serverFolder.getTotalSpace()) < criticalValue) {
			Humbug.severe("Available disk space below critical value, shutting the server down to prevent damage");
			Bukkit.shutdown();
		}
	}

}
