package me.Antikid.types;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class YamlFileManager {

    private YamlConfiguration yamlFile;
    private File file;
    private String path;
    private String name;

    public YamlConfiguration loadFile(String path, String name) {

	File file = new File(path, name + ".yml");

	if (file.getParent() == null)
	    file.getParentFile().mkdirs();

	if (!file.exists() || file == null) {
	    try {
		file.createNewFile();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

	YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(file);
	this.file = file;
	this.yamlFile = yamlFile;
	this.path = path;
	this.name = name;
	return yamlFile;
    }

    public void reload() {
	try {
	    yamlFile.load(yamlFile.getCurrentPath());

	} catch (
		IOException
		| InvalidConfigurationException e) {
	    e.printStackTrace();
	}
    }

    public void save() {
	try {
	    yamlFile.save(path + "/" + name + ".yml");
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public YamlConfiguration getYamlFile() {
	return yamlFile;
    }

    public void setYamlFile(YamlConfiguration yamlFile) {
	this.yamlFile = yamlFile;
    }

    public File getFile() {
	return file;
    }

    public void setFile(File file) {
	this.file = file;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }
}
