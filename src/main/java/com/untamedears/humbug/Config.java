package com.untamedears.humbug;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import com.google.common.base.Splitter;
import com.untamedears.humbug.Humbug;
import com.untamedears.humbug.annotations.BahHumbug;
import com.untamedears.humbug.annotations.BahHumbugs;
import com.untamedears.humbug.annotations.ConfigOption;

public class Config {
  private static Config global_instance_ = null;

  // ================================================
  // Configuration defaults
  private static final boolean debug_log_ = false;
  private static final int quartz_gravel_percentage_ = 0;
  private static final int cobble_from_lava_scan_radius_ = 0;
  private static final String find_end_portals_ = null;
  private static final int projectile_slow_ticks_ = 100;
  private static final int loot_multiplier_ = 1;
  private static final String book_name_ = "Civcraft Beginner's Guide";
  private static final String book_author_ = "Civcraft Admins";
  private static final String book_text_ =
        "    {|lWhat is CivCraft?{|r\n"
        + "\"{|oCivcraft is an experiment for communities, political ideologies, debate and discussion.... where players can work together to create and shape civilization or to watch it crumble.{|r\"\n"
        + "-ttk2, founder\n}|"

        + "    {|lWhere am I?{|r\n"
        + "Anyone who hasn't slept in a bed yet spawns at a random point in the world. You're most likely in the middle of nowhere.\n}|"

        + "    {|lWhy are so few people in chat right now?{|r\n"
        + "Your chat range is limited by distance. This range varies depending which shard you're in but the default is 750 blocks.\n}|"

        + "    {|lHow do I find people?{|r\n"
        + "Your best bet is to respawn.\n"
        + "You can also poke around on reddit.com/r/Civcraft for directions to a city. There are usually towns on there who are looking for new members.\n}|"

        + "Or, why not grab a few friends and start a town of your own? No one's stopping you.\n"
        + "Although if you're patient, maybe you want to find a road and, well, just see where it takes you.\n}|"

        + "    {|lAre you an admin?{|r\n\n"
        + "No.\n\n"
        + "Editor's Note: Editor is an admin.\n}|"

        + "    {|lHow do I play?{|r\n"
        + "Civcraft is a Minecraft survival server in which players are more-or-less free to do whatever they want. Building, stealing, fighting, killing, and griefing are all fair game.\n}|"

        + "Moderators only get involved when someone has cheated, hacked, exploited a glitch, or harassed another player in real life.\n}|"

        + "In Civcraft, power lies with the players, not the moderators.\n}|"

        + "To give players this power and to expand some on Minecraft's base feature set, the following modifications on the original Minecraft are run on Civcraft.\n}|"

        + "    {|lPrison Pearl{|r\n"
        + "This mod lets players imprison (and effectively ban) each other. To imprison someone with Prison Pearl simply kill them while an ender pearl is in your hotbar.\n}|"

        + "    {|lCitadel:{|r\n"
        + "This mod allows players to place 'reinforced' blocks that are more difficult to break than non-reinforced blocks. To make a basic reinforcement, type '/ctr' into chat and then punch the target block with stone.\n}|"

        + "    {|lBastion:{|r\n"
        + "'Bastion' blocks prohibit nearby players from placing blocks, emptying buckets and throwing ender pearls. In practice they are much less common than reinforcements for their expense and specialized nature.\n}|"

        + "    {|lJuke Alert:{|r\n"
        + "Jukeboxes and noteblocks are Civcraft's security cameras. By simply reinforcing a jukebox or noteblock you turn it into a 'snitch' that keeps you updated on what happens around it.\n}|"

        + "    {|lPylon:{|r\n"
        + "The Pylon mod disables standard Minecraft experience-gathering and enchantment. All enchanted materials on Civcraft come from 'Pylons' which are rare and difficult to build.\n}|"

        + "For a full list of mods and what they do, visit our wiki at wiki.civcraft.co. To see all chat commands, type '/help' into chat.\n}|"

        + "    {|lSo...what now?{|r\n\n"
        + "What now? You tell me.\n}|"

        + "    {|lBe a builder.{|r\n"
        + "Build a house, build a village, build a store, build a castle, build an island, build a mountain. You can build an island in the sky and then build a city on top of it, but that's already been done though.\n}|"

        + "    {|lBe a griefer.{|r\n"
        + "Grief a house, loot a village, bomb a city, start a gang, kill the mayor.\n}|"

        + "Mine out a bunch of diamonds and a bunch of obsidian and throw it all over a chest at the bottom of the world and then put as many people in the chest as possible. Why not?\n}|"

        + "Destroy the world. That's already been done twice. Once on purpose.\n}|"

        + "What happens now is up to you.\n\n"
        + "Author: {|ocommando_wins{|r\n\n"
        + "Editor: {|oIntellectualHobo{|r";

  private static final Iterable<String> compiled_book_text_ =
      Splitter.on("}|").split(book_text_.replaceAll("\\{\\|", "\u00A7"));

  private static final String holiday_book_name_ = "Happy Holidays";
  private static final String holiday_book_author_ = "the CivCraft Admins";
  private static final String holiday_book_text_ =
      "    {|2H{|4a{|2p{|4p{|2y {|4H{|2o{|4l{|2i{|4d{|2a{|4y{|2s{|4!{|0\n"
      + "Thank each and every one of you for making the server what it is. "
      + "Our best wishes go out to you and yours. May your New Year "
      + "be full of griefing, drama, and mayhem.\n\n"
      + "-Santa Ttk2 and the Admin Elves";
  private static final Iterable<String> compiled_holiday_book_text_ =
      Splitter.on("}|").split(holiday_book_text_.replaceAll("\\{\\|", "\u00A7"));

  private static FileConfiguration config_ = null;

  public static Config initialize(Humbug plugin) {
    if (global_instance_ == null) {
      plugin.reloadConfig();
      config_ = plugin.getConfig();
      config_.options().copyDefaults(true);
      global_instance_ = new Config(plugin);
      global_instance_.load();
    }
    return global_instance_;
  }

  public static ConfigurationSection getStorage() {
    return config_;
  }

  private Humbug plugin_ = null;
  private Set<Integer> remove_item_drops_ = null;

  public Config(Humbug plugin) {
    plugin_ = plugin;
    scanAnnotations();
  }

  private Map<String, ConfigOption> dynamicOptions_ = new TreeMap<String, ConfigOption>();

  private void addToConfig(BahHumbug bug) {
    if (dynamicOptions_.containsKey(bug.opt())) {
      Humbug.info("Duplicate configuration option detected: " + bug.opt());
      return;
    }
    dynamicOptions_.put(bug.opt(), new ConfigOption(bug));
  }

  private void scanAnnotations() {
    try {
      for (Method method : Humbug.class.getMethods()) {
        BahHumbug bug = method.getAnnotation(BahHumbug.class);
        if (bug != null) {
          addToConfig(bug);
          continue;
        }
        BahHumbugs bugs = method.getAnnotation(BahHumbugs.class);
        if (bugs != null) {
          for (BahHumbug drone : bugs.value()) {
            addToConfig(drone);
          }
          continue;
        }
      }
    } catch(Exception ex) {
      Humbug.info(ex.toString());
    }
  }

  public void load() {
    // Setting specific initialization
    loadRemoveItemDrops();
  }

  public void reload() {
    plugin_.reloadConfig();
  }

  public void save() {
    plugin_.saveConfig();
  }

  public ConfigOption get(String optionName) {
    return dynamicOptions_.get(optionName);
  }

  public boolean set(String optionName, String value) {
    ConfigOption opt = dynamicOptions_.get(optionName);
    if (opt != null) {
      opt.setString(value);
      return true;
    }
    return false;
  }

  public boolean getDebug() {
    return config_.getBoolean("debug", debug_log_);
  }

  public void setDebug(boolean value) {
    config_.set("debug", value);
  }

  public String getTitle(){
    return config_.getString("noobbook.name", book_name_);
  }

  public String getAuthor(){
    return config_.getString("noobbook.author", book_author_);
  }

  public List<String> getPages(){
    List<String> book_pages = new LinkedList<String>();
    for(final String text: compiled_book_text_){
      book_pages.add(text);
    }
    return book_pages;
  }

  public String getHolidayTitle(){
    return holiday_book_name_;
  }

  public String getHolidayAuthor(){
    return holiday_book_author_;
  }

  public List<String> getHolidayPages(){
    List<String> book_pages = new LinkedList<String>();
    for(final String text: compiled_holiday_book_text_){
      book_pages.add(text);
    }
    return book_pages;
  }

  public int getLootMultiplier(String entity_type){
    return config_.getInt("loot_multiplier." + entity_type.toLowerCase(), loot_multiplier_);
  }

  public void setLootMultiplier(String entity_type, int value){
    config_.set("loot_multiplier." + entity_type.toLowerCase(), value);
  }


  public int getQuartzGravelPercentage() {
    return config_.getInt("quartz_gravel_percentage", quartz_gravel_percentage_);
  }

  public void setQuartzGravelPercentage(int value) {
    if (value < 0) {
      value = 0;
      Humbug.warning("quartz_gravel_percentage adjusted to 0");
    } else if (value > 100) {
      value = 100;
      Humbug.warning("quartz_gravel_percentage adjusted to 100");
    }
    config_.set("quartz_gravel_percentage", value);
  }


  public int getCobbleFromLavaScanRadius() {
    return config_.getInt("cobble_from_lava_scan_radius", cobble_from_lava_scan_radius_);
  }

  public void setCobbleFromLavaScanRadius(int value) {
    if (value < 0) {
      value = 0;
      Humbug.warning("cobble_from_lava_scan_radius adjusted to 0");
    } else if (value > 20) {  // 8000 blocks to scan at 20
      value = 20;
      Humbug.warning("cobble_from_lava_scan_radius adjusted to 20");
    }
    config_.set("cobble_from_lava_scan_radius", value);
  }


  public int getProjectileSlowTicks() {
    int ticks = config_.getInt("projectile_slow_ticks", projectile_slow_ticks_);
    if (ticks <= 0 || ticks > 600) {
      ticks = 100;
    }
    return ticks;
  }
  

  private void loadRemoveItemDrops() {
    if (!config_.isSet("remove_mob_drops")) {
      remove_item_drops_ = new HashSet<Integer>(4);
      return;
    }
    remove_item_drops_ = new HashSet<Integer>();
    if (!config_.isList("remove_mob_drops")) {
      Integer val = config_.getInt("remove_mob_drops");
      if (val == null) {
        config_.set("remove_mob_drops", new LinkedList<Integer>());
        Humbug.info("remove_mob_drops was invalid, reset");
        return;
      }
      remove_item_drops_.add(val);
      List<Integer> list = new LinkedList<Integer>();
      list.add(val);
      config_.set("remove_mob_drops", val);
      Humbug.info("remove_mob_drops was not an Integer list, converted");
      return;
    }
    remove_item_drops_.addAll(config_.getIntegerList("remove_mob_drops"));
  }

  public boolean doRemoveItemDrops() {
    return !remove_item_drops_.isEmpty();
  }

  public Set<Integer> getRemoveItemDrops() {
    return Collections.unmodifiableSet(remove_item_drops_);
  }

  public void addRemoveItemDrop(int item_id) {
    if (item_id < 0) {
      return;
    }
    remove_item_drops_.add(item_id);
    List<Integer> list;
    if (!config_.isSet("remove_mob_drops")) {
      list = new LinkedList<Integer>();
    } else {
      list = config_.getIntegerList("remove_mob_drops");
    }
    list.add(item_id);
    config_.set("remove_mob_drops", list);
  }

  public void removeRemoveItemDrop(int item_id) {
    if (item_id < 0) {
      return;
    }
    if (!remove_item_drops_.remove(item_id)) {
      return;
    }
    List<Integer> list = config_.getIntegerList("remove_mob_drops");
    list.remove((Object)item_id);
    config_.set("remove_mob_drops", list);
  }

  public void setRemoveItemDrops(Set<Integer> item_ids) {
    remove_item_drops_ = new HashSet<Integer>();
    remove_item_drops_.addAll(item_ids);
    List<Integer> list = new LinkedList<Integer>();
    list.addAll(item_ids);
    config_.set("remove_mob_drops", list);
  }

  public String toDisplayRemoveItemDrops() {
    StringBuilder sb = new StringBuilder();
    for (Integer item_id : remove_item_drops_) {
      Material mat = Material.getMaterial(item_id);
      if (mat == null) {
        sb.append(item_id);
      } else {
        sb.append(mat.toString());
      }
      sb.append(",");
    }
    return sb.toString();
  }
  
  public void tag_on_join(boolean value){
	  config_.set("tag_on_join", value);
  }

  public List<ItemStack> getStartingKit() {
    List<?> bsk = config_.getList("newbie_kit");
	if (bsk != null) {
      return (List<ItemStack>) bsk;
	}
	return null;
  }

  public void setDefaultStartingKit() {
    List<ItemStack> kit = new LinkedList<ItemStack>();
	ItemStack def = new ItemStack(Material.getMaterial("COOKIE"), 32);
	ItemMeta meta = def.getItemMeta();
	meta.setDisplayName("Chocolate-chip Cookie");
	List<String> lore = new LinkedList<String>();
	lore.add("Gift from TTK2 as you");
	lore.add("begin your journey on Civcraft");
	meta.setLore(lore);
	def.setItemMeta(meta);
	kit.add(def);
	setStartingKit(kit);
  }

  public void setStartingKit(List<ItemStack> kit) {
	config_.set("newbie_kit", kit);
  }
}
