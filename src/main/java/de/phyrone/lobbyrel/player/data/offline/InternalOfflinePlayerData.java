package de.phyrone.lobbyrel.player.data.offline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.phyrone.lobbyrel.config.Config;

public class InternalOfflinePlayerData {
	public static InternalOfflinePlayerData fromJson(String json) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(json, InternalOfflinePlayerData.class);
	}public String toJsonString() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(this);
	}public String toPrettyJsonString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}public static InternalOfflinePlayerData fromFile(UUID uuid,File file) throws FileNotFoundException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
        BufferedReader reader = new BufferedReader(new InputStreamReader( 
        		new FileInputStream(file))); 
        return gson.fromJson(reader, InternalOfflinePlayerData.class); 

}public InternalOfflinePlayerData toFile(File file) throws IOException {
	if(!file.getParentFile().exists()) {
		file.getParentFile().mkdirs();
	}
    Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
    String jsonConfig = gson.toJson(this); 
    FileWriter writer; 
        writer = new FileWriter(file); 
        writer.write(jsonConfig); 
        writer.flush(); 
        writer.close(); 

	return this;
}
	//Data
    public int Navigator = Config.getInt("PlayerSettings.Default.Navigator", 0);
    public int PlayerHider = Config.getInt("PlayerSettings.Default.PlayerHider", 0);
    public Boolean Sound = true;
    public Boolean Scoreboard = true; 
    public boolean JumpPad = true;
    public boolean DoubleJump = true;
    public HashMap<String, Object> CustomData = new HashMap<>();
    public ArrayList<String> Tags = new ArrayList<>();
	public int getNavigator() {
		return Navigator;
	}
	public InternalOfflinePlayerData setNavigator(int navigator) {
		Navigator = navigator;
		return this;
	}
	public int getPlayerHider() {
		return PlayerHider;
	}
	public InternalOfflinePlayerData setPlayerHider(int playerHider) {
		PlayerHider = playerHider;
		return this;
	}
	public Boolean getSound() {
		return Sound;
	}
	public InternalOfflinePlayerData setSound(Boolean sound) {
		Sound = sound;
		return this;
	}
	public Boolean getScoreboard() {
		return Scoreboard;
	}
	public InternalOfflinePlayerData setScoreboard(Boolean scoreboard) {
		Scoreboard = scoreboard;
		return this;
	}
	public boolean isJumpPad() {
		return JumpPad;
	}
	public InternalOfflinePlayerData setJumpPad(boolean jumpPad) {
		JumpPad = jumpPad;
		return this;
	}
	public boolean isDoubleJump() {
		return DoubleJump;
	}
	public InternalOfflinePlayerData setDoubleJump(boolean doubleJump) {
		DoubleJump = doubleJump;
		return this;
	}
	public ArrayList<String> getTags() {
		return Tags;
	}
	public InternalOfflinePlayerData setTags(ArrayList<String> tags) {
		Tags = tags;
		return this;
	}
	public HashMap<String, Object> getCustomData() {
		return CustomData;
	}
	public InternalOfflinePlayerData setCustomData(HashMap<String, Object> customData) {
		CustomData = customData;
		
		return this;
	}
    
}
