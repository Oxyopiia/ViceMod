package net.oxyopia.vice.config;

import gg.essential.api.EssentialAPI;
import gg.essential.universal.UDesktop;
import gg.essential.universal.UScreen;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.*;
import net.oxyopia.vice.Vice;
import net.oxyopia.vice.utils.Utils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.net.URI;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.oxyopia.vice.Vice.devConfig;

public class Config extends Vigilant {

    @SuppressWarnings("unused")
	@Property(
        type = PropertyType.BUTTON,
        name = "Edit HUD Locations",
        description = """
			Edit the positions of HUD Elements:
			 - Drag to reposition. §8(WASD/Arrow Keys)§7
			 - Scroll to scale. §8(+/-)§7
			 - Middle Click to toggle centering. §8(TAB)§7
			 - Press H or V to center horizontally/vertically.""",
        category = "General",
        subcategory = "Vice",
        placeholder = "Edit HUD Locations"
    )
    public void EDIT_HUD_LOCATIONS() {
		if (!Utils.INSTANCE.getInDoomTowers()) {
			EssentialAPI.getNotifications().push("HUD Manager", "Please open while in DoomTowers!", 3f);
			return;
		}

		UScreen.displayScreen(HudEditor.INSTANCE);
    }

    @Property(
        type = PropertyType.SWITCH,
        name = "HUD Text Shadow",
        description = "Toggle text shadow on Vice HUD elements.",
        category = "General",
        subcategory = "Vice"
    )
    public boolean HUD_TEXT_SHADOW = true;


	@SuppressWarnings("unused")
	@Property(
        type = PropertyType.BUTTON,
        name = "Vice Discord",
        description = "Join the vice Discord to recieve updates, post suggestions, and more!",
        category = "General",
        subcategory = "Vice",
        placeholder = "Join"
    )
    public void JOIN_VICE_DISCORD() {
        UDesktop.browse(URI.create("https://discord.gg/7nb9KcZHug"));
        EssentialAPI.getNotifications().push("Vice", "Hopefully opened Discord/Web Browser!", 3f);
    }

    // General/Developer

    @Property(
        type = PropertyType.SWITCH,
        name = "Developer Mode",
        description = "dev mode for beta versions/debugging\n§cOnly enable if you know what you're doing!",
        category = "General",
        subcategory = "Developer"
    )
    public boolean DEVMODE = false;

    @SuppressWarnings("unused")
	@Property(
        type = PropertyType.BUTTON,
        name = "Dev Menu",
        description = "Open the developer menu",
        category = "General",
        subcategory = "Developer",
        placeholder = "Open"
    )
    public void OPEN_DEV_CONFIG() {
        UScreen.displayScreen(devConfig.gui());
    }

    @Property(
        type = PropertyType.SELECTOR,
        name = "Gaming Mode",
        description = "Choose your epic gaming mode for optimal gaming performance",
        category = "General",
        subcategory = "Developer",
        options = {"None", "Vice", "DoomTowers smashing", "msmdude", "digmonireland", "Trump", "clive", "law Abiding Citizen"}
    )
    public int DEV_GAMING_MODE = 0;

    // General/Quality of Life

    @Property(
        type = PropertyType.SWITCH,
        name = "Prevent Consuming Items",
        description = "Blocks block place packets when using consumable items, like Player Heads or Train Keys.\nItem Abilities still activate when enabled.",
        category = "General",
        subcategory = "Quality of Life"
    )
    public boolean PREVENT_PLACING_PLAYER_HEADS = true;

    @Property(
            type = PropertyType.SWITCH,
            name = "Train Timer",
            description = "Displays a HUD graphic showing information about the Train in World 11.",
            category = "General",
            subcategory = "Quality of Life"
    )
    public boolean TRAIN_TIMER = false;

    @Property(
    	type = PropertyType.SWITCH,
    	name = "Show Train Timer outside World 11",
    	description = "Show the train timer while outside World 11, yeah",
    	category = "General",
    	subcategory = "Quality of Life"
    )
    public boolean TRAIN_TIMER_OUTSIDE = false;

    @Property(
        type = PropertyType.SWITCH,
        name = "Hide Revolver Blindness",
        description = "Hides the blindness effect when aiming the revolver.",
        category = "General",
        subcategory = "Quality of Life"
    )
    public boolean HIDE_REVOLVER_BLINDNESS = true;

    @Property(
        type = PropertyType.SWITCH,
        name = "Better Tower Beacon UI",
        description = "Display the floor number as a stack size, and add the featured worlds in the lore.",
        category = "General",
        subcategory = "Quality of Life"
    )
    public boolean BETTER_TOWER_BEACON_UI = true;
    
    // General/Spam Hider

    @Property(
        type = PropertyType.SWITCH,
        name = "Hide Server Tips",
        description = "Hides messages such as the periodic Discord tip in chat.",
        category = "General",
        subcategory = "Spam Hider",
        hidden = true
    )
    public boolean HIDE_SERVER_TIPS = false;
    
    @Property(
    	type = PropertyType.SWITCH,
    	name = "Hide Worldguard Messages",
    	description = "Hides the 'Hey! Sorry' Worldguard Messages",
    	category = "General",
    	subcategory = "Spam Hider"
    )
    public boolean HIDE_WORLDGUARD_MESSAGES = false;
    
    @Property(
    	type = PropertyType.SWITCH,
    	name = "Hide Set Requirement Messages",
    	description = "Hides the 'You must be wearing [x] number of [Set] to use this item' messages",
    	category = "General",
    	subcategory = "Spam Hider"
    )
    public boolean HIDE_SET_REQUIREMENT_MESSAGES = false;

    // General/World 4

    @Property(
    	type = PropertyType.SWITCH,
    	name = "Show Next Cooking Item",
    	description = "Displays a HUD graphic showing the next required item for your order in World 4.",
    	category = "General",
    	subcategory = "World 4"
    )
    public boolean SHOW_NEXT_COOKING_ITEM = true;

    @Property(
    	type = PropertyType.SWITCH,
    	name = "Show Stock Information",
    	description = "Displays a HUD graphic showing your stock in World 4.",
    	category = "General",
    	subcategory = "World 4"
    )
    public boolean SHOW_COOKING_STOCK_INFO = true;

    @Property(
    	type = PropertyType.SWITCH,
    	name = "Simplify Cooking Displays",
    	description = "Shows Cooking displays in a more simplified form.",
    	category = "General",
    	subcategory = "World 4"
    )
    public boolean SIMPLIFY_COOKING_DISPLAYS = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Order Tracker",
		description = "Displays a HUD graphic showing the total number of requests, completions and completion rate of each order in World 4.",
		category = "General",
		subcategory = "World 4"
	)
	public boolean COOKING_ORDER_TRACKER = false;

    @Property(
    	type = PropertyType.SWITCH,
    	name = "Hide Handled Chat Messages",
    	description = "Hides chat messages displayed by Cooking features",
    	category = "General",
    	subcategory = "World 4"
    )
    public boolean HIDE_HANDLED_COOKING_MESSAGES = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Auto Apply Bread",
		description = "Normally, Bread is not removed when completing an order, but can sometimes bug out and lock the order. As a result, Vice automatically deselects Bread for each new order, but this functionality can be bypassed with this feature.\n§eCan bug out from time to time!§8 (no clue why)",
		category = "General",
		subcategory = "World 4"
	)
	public boolean AUTO_APPLY_BREAD = false;

    // General/Item Cooldowns

    @Property(
        type = PropertyType.SWITCH,
        name = "Item Cooldown Display",
        description = "Highlights a hotbar slot when on cooldown, and can display a HUD graphic. Customizable when enabled!",
        category = "General",
        subcategory = "Item Cooldowns"
    )
    public boolean ITEM_COOLDOWN_DISPLAY = false;

    @Property(
        type = PropertyType.SELECTOR,
        name = "Cooldown Display Type",
        description = "Changes how the Item Cooldown Display is shown.",
        category = "General",
        subcategory = "Item Cooldowns",
        options = {"Vanilla", "Static Background", "Color Fade", "Percentage Based", "Text Only"}
    )
    public int ITEMCD_DISPLAY_TYPE = 1;

    @Property(
        type = PropertyType.SWITCH,
        name = "Show Timer/Ready Text in Hotbar",
        description = "Displays a text stating the cooldown remaining or when ready.",
        category = "General",
        subcategory = "Item Cooldowns"
    )
    public boolean SHOW_ITEMCD_TEXT = true;

    @Property(
        type = PropertyType.SWITCH,
        name = "Show Timer/Ready Text near Crosshair",
        description = "Displays a text stating the cooldown remaining or when ready near your crosshair.\n§eCan be customized in Developer options (search 'dev'), will be migrated to HUD Manager soon!",
        category = "General",
        subcategory = "Item Cooldowns"
    )
    public boolean SHOW_ITEMCD_TEXT_CROSSHAIR = true;

    @Property(
        type = PropertyType.SWITCH,
        name = "Hide when Ready",
        description = "Instead of highlighting as Green, hides the Overlay.",
        category = "General",
        subcategory = "Item Cooldowns"
    )
    public boolean HIDE_ITEMCD_WHEN_READY = false;

    @Property(
        type = PropertyType.PERCENT_SLIDER,
        name = "Background Opacity",
        description = "Opacity for any item backgrounds rendered in the Item Cooldown Display.",
        category = "General",
        subcategory = "Item Cooldowns",
        minF = 0.01f,
        maxF = 1f
    )
    public float ITEMCD_BACKGROUND_OPACITY = 0.2f;

    @Property(
        type = PropertyType.SWITCH,
        name = "Hide Cooldown Titles",
        description = "Hides the Cooldown titles when trying to use an item while on Cooldown.",
        category = "General",
        subcategory = "Item Cooldowns"
    )
    public boolean HIDE_ITEM_COOLDOWN_TITLES = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Wrong Set Indicator",
		description = "Display a red overlay on an item if you do not have the required Set equipped.",
		category = "General",
		subcategory = "Item Cooldowns"
	)
	public boolean WRONG_SET_INDICATOR = false;

    // General/Fishing

    @Property(
        type = PropertyType.SWITCH,
        name = "Fishing Bite Ding",
        description = "Plays a unique ding sound and title when your bobber is bitten.",
        category = "General",
        subcategory = "Fishing"
    )
    public boolean FISHING_DING = true;

    // Arenas/Quality of Life

    @Property(
        type = PropertyType.SWITCH,
        name = "Draw Danger Zones",
        description = "Draws a Bounding Box around the zone where Mobs spawn in Arenas",
        category = "Arenas",
        subcategory = "Quality of Life",
        hidden = true
    )
    public boolean DRAW_ARENA_DANGER_ZONES = false;

    @Property(
        type = PropertyType.COLOR,
        name = "Danger Zone Colour",
        description = "Color the thing",
        category = "Arenas",
        subcategory = "Quality of Life",
        allowAlpha = false
    )
    public Color ARENA_DANGER_ZONE_COLOR = new Color(255, 0, 0);

    @Property(
        type = PropertyType.SWITCH,
        name = "Arenas Cooldown Notification",
        description = "Sends a chat notification when an Arena cooldown has expired.",
        category = "Arenas",
        subcategory = "Quality of Life"
    )
    public boolean ARENAS_COOLDOWN_NOTIFIER = true;


    // Arenas/Live Arena Info
    @Property(
        type = PropertyType.SWITCH,
        name = "Live Arena Information",
        description = "Display useful statistics during an Arena session. At base, displays current Wave.",
        category = "Arenas",
        subcategory = "Live Arena Info"
    )
    public boolean LIVE_ARENA_TOGGLE = false;

    @Property(
        type = PropertyType.SWITCH,
        name = "Display Mobs Remaining",
        description = "Adds a Mobs remaining stat to the Live Arena Info.",
        category = "Arenas",
        subcategory = "Live Arena Info"
    )
    public boolean LIVE_ARENA_MOBS = false;

    @Property(
        type = PropertyType.SWITCH,
        name = "Display Wave Timer",
        description = "Displays the estimated time left for the current Wave.",
        category = "Arenas",
        subcategory = "Live Arena Info"
    )
    public boolean LIVE_ARENA_ROUND_TIMER = true;

    @Property(
        type = PropertyType.SELECTOR,
        name = "Display Projected Drops",
        description = "Adds drops to Live Arena Info.\nBasic Drops: Amethyst, Polar Fur, Glowing Matter, etc\nUnique Drops: Chance for Galactic Hand Cannon, Arctic Scroll, etc",
        category = "Arenas",
        subcategory = "Live Arena Info",
        options = {"None", "Basic Drops Only", "Unique Drops Only", "All"}
    )
    public int LIVE_ARENA_DROPS = 2;

    @Property(
        type = PropertyType.SWITCH,
        name = "Mob Effects Notification",
        description = "Sends a chat message when mobs gain certain potion effects during an Arena.",
        category = "Arenas",
        subcategory = "Live Arena Info"
    )
    public boolean ARENAS_MOB_EFFECT_NOTIFICATION = true;

    // Bosses/Quality of Life

    @Property(
        type = PropertyType.SWITCH,
        name = "Boss Despawn Timers",
        description = "Adjusts the Bossbar to display the despawn timer of the boss.",
        category = "Bosses",
        subcategory = "Quality of Life"
    )
    public boolean BOSS_DESPAWN_TIMERS = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Low Time Warning",
		description = "Displays a warning when the Boss is relatively close to despawning.",
		category = "Bosses",
		subcategory = "Quality of Life"
	)
	public boolean BOSS_DESPAWN_WARNING = true;
    
    @Property(
    	type = PropertyType.SWITCH,
    	name = "Abyssal Vice Laser Warning",
    	description = "Shows a warning title when Abyssal Vice is about to shoot its laser.",
    	category = "Bosses",
    	subcategory = "Abyssal Vice"
    )
    public boolean ABYSSAL_VICE_LASER_WARNING = true;

    // Sounds

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "8-Bit Katana Volume",
        category = "Sounds",
        maxF = 2f
    )
    public float EIGHT_BIT_KATANA_VOLUME = 1f;
	
	@Property(
		type = PropertyType.SWITCH,
		name = "Glitch Mallet Volume",
		category = "Sounds",
		maxF = 2f
	)
	public float GLITCH_MALLET_VOLUME = 1f;

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "Arctic Core Volume",
        category = "Sounds",
        maxF = 2f
    )
    public float ARCTIC_CORE_VOLUME = 1f;

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "Barbed Shotgun Volume",
        category = "Sounds",
        maxF = 2f
    )
    public float BARBED_SHOTGUN_VOLUME = 1f;

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "Bedrock Breaker Volume",
        description = "§eMay have inaccuracies with the 'regenerating' (beacon) sounds.",
        category = "Sounds",
        maxF = 2f
    )
    public float BEDROCK_BREAKER_VOLUME = 1f;

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "Laser Point Minigun Volume",
        category = "Sounds",
        maxF = 2f
    )
    public float LASER_POINT_MINIGUN_VOLUME = 1f;

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "Snowball Cannon Volume",
        category = "Sounds",
        maxF = 2f
    )
    public float SNOWBALL_CANNON_VOLUME = 1f;

    public Config() {
        super(new File("./config/vice/config.toml"), "Vice §b" + Vice.Companion.getVersion(), new JVMAnnotationPropertyCollector(), new ConfigSorting());
    }

    public void init() {
        initialize();
        markDirty();

        addDependency("ARENA_DANGER_ZONE_COLOR", "DRAW_ARENA_DANGER_ZONES");
        addDependency("TRAIN_TIMER_OUTSIDE", "TRAIN_TIMER");

        addDependency("ITEMCD_DISPLAY_TYPE", "ITEM_COOLDOWN_DISPLAY");
        addDependency("SHOW_ITEMCD_TEXT", "ITEM_COOLDOWN_DISPLAY");
        addDependency("SHOW_ITEMCD_TEXT_CROSSHAIR", "ITEM_COOLDOWN_DISPLAY");
        addDependency("HIDE_ITEMCD_WHEN_READY", "ITEM_COOLDOWN_DISPLAY");
        addDependency("ITEMCD_BACKGROUND_OPACITY", "ITEM_COOLDOWN_DISPLAY");

        addDependency("LIVE_ARENA_MOBS", "LIVE_ARENA_TOGGLE");
        addDependency("LIVE_ARENA_ROUND_TIMER", "LIVE_ARENA_TOGGLE");
        addDependency("LIVE_ARENA_DROPS", "LIVE_ARENA_TOGGLE");

//		TODO("Implement Migration logic")
    }

	public static class ConfigSorting extends SortingBehavior {
        @NotNull
        @Override
        public Comparator<? super Category> getCategoryComparator() {
            return (o1, o2) -> {
                if ("General".equals(o1.getName())) {
                    return -1;
                }
                if ("General".equals(o2.getName())) {
                    return 1;
                } else {
                    return Comparator.comparing(Category::getName).compare(o1, o2);
                }
            };
        }

        @NotNull
        @Override
        public Comparator<? super Map.Entry<String, ? extends List<PropertyData>>> getSubcategoryComparator() {
            return (entry1, entry2) -> {
                String key1 = entry1.getKey();
                String key2 = entry2.getKey();

                // This places Vice & Quality of Life at the top, and Developer at the bottom.
                Map<String, Integer> keyOrderMap = new HashMap<>();
                keyOrderMap.put("Vice", -2);
                keyOrderMap.put("Quality of Life", -1);
                keyOrderMap.put("Developer", 1);

                int order1 = keyOrderMap.getOrDefault(key1, 0);
                int order2 = keyOrderMap.getOrDefault(key2, 0);

                if (order1 != order2) {
                    return Integer.compare(order1, order2);
                } else {
                    return key1.compareTo(key2);
                }
            };
        }
    }
}
