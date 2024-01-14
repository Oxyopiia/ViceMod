package net.oxyopia.vice.config;

import gg.essential.api.EssentialAPI;
import gg.essential.universal.UDesktop;
import gg.essential.universal.UScreen;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.*;
import net.oxyopia.vice.Vice;
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

    @Property(
        type = PropertyType.BUTTON,
        name = "Edit HUD Locations",
        description = "Edit the position of HUD Elements\n" +
            " - Drag elements around in the menu to reposition,\n" +
            " - Use the Scroll Wheel on an element to resize.",
        category = "General",
        subcategory = "Vice",
        placeholder = "Edit HUD Locations"
    )
    public void EDIT_HUD_LOCATIONS() {
        EssentialAPI.getNotifications().push("Vice", "HUD Manager coming soon\n§eUse Developer Mode as an alternative for now!", 3f);
    }

    @Property(
        type = PropertyType.SWITCH,
        name = "HUD Text Shadow",
        description = "Toggle text shadow on Vice HUD elements.",
        category = "General",
        subcategory = "Vice"
    )
    public boolean HUD_TEXT_SHADOW = true;

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
        name = "Snowball Cannon Projection",
        description = "Displays a line of projection of where the Snowball Cannon can reach.\n§cComing soon!",
        category = "General",
        subcategory = "Quality of Life",
        hidden = true
    )
    public boolean SNOWBALL_CANNON_PROJECTION = false;

    @Property(
        type = PropertyType.COLOR,
        name = "Snowball Projection Colour",
        description = "Color the thing",
        category = "General",
        subcategory = "Quality of Life",
        allowAlpha = false
    )
    public Color SNOWBALL_CANNON_PROJECTION_COLOR = new Color(0, 200, 255);

    @Property(
        type = PropertyType.SWITCH,
        name = "Prevent Placing Player Heads",
        description = "Blocks place block packets when using player heads to prevent consuming some player heads, such as Ancient Cells.",
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

    @Property(
        type = PropertyType.SWITCH,
        name = "Hide Server Tips",
        description = "Hides messages such as the periodic Discord tip in chat.",
        category = "General",
        subcategory = "Quality of Life",
        hidden = true
    )
    public boolean HIDE_SERVER_TIPS = true;

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
    	description = "Shows cooking displays in a more simplified manner",
    	category = "General",
    	subcategory = "World 4"
    )
    public boolean SIMPLIFY_COOKING_DISPLAYS = false;

    @Property(
    	type = PropertyType.SWITCH,
    	name = "Block Wrong Cooking Clicks",
    	description = "Blocks clicks when clicking on a Cooking plate with the wrong item for your current order.\n§eUse /viceclickoverride to override!",
    	category = "General",
    	subcategory = "World 4"
    )
    public boolean BLOCK_WRONG_COOKING_CLICKS = true;

    @Property(
    	type = PropertyType.SWITCH,
    	name = "Hide Handled Chat Messages",
    	description = "Hides chat messages handled by the Cooking UI (held item, stock info, etc)",
    	category = "General",
    	subcategory = "World 4"
    )
    public boolean HIDE_HANDLED_COOKING_MESSAGES = true;

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

    // General/Fishing

    @Property(
        type = PropertyType.SWITCH,
        name = "Fishing Bite Ding",
        description = "Plays a ding sound when your bobber is bitten.\n" +
            "§eRequires semi-close proximity if Don't Detect with Sound Packets is disabled.",
        category = "General",
        subcategory = "Fishing"
    )
    public boolean FISHING_DING = true;

    @Property(
        type = PropertyType.SWITCH,
        name = "Don't Detect Bite with Sound Packets",
        description = "Allows for infinite range of Fishing Bite Ding by not using Sound packets, only relying on Velocity packets.\n"+
            "§eNot extensively tested, may provide false positives.\n" +
            "§aWhen within normal setting range, works as usual.",
        category = "General",
        subcategory = "Fishing"
    )
    public boolean FISHING_DING_DONT_DETECT_SOUND_PACKET = true;

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
        description = "Displays a notification when an Arena cooldown has expired.",
        category = "Arenas",
        subcategory = "Quality of Life",
        hidden = true
    )
    public boolean ARENAS_COOLDOWN_NOTIFIER = true;


    // Arenas/Live Arena Info
    @Property(
        type = PropertyType.SWITCH,
        name = "Live Arena Information",
        description = "Display useful statistics during an Arena session. At base, displays current Wave.",
        category = "Arenas",
        subcategory = "Live Arena Info",
        hidden = true
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
        name = "Display Time Elapsed",
        description = "Displays the time elapsed during your Session.",
        category = "Arenas",
        subcategory = "Live Arena Info"
    )
    public boolean LIVE_ARENA_TOTAL_TIMER = true;

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
        name = "Silence Boss Sounds",
        description = "Silences boss sound effects, may be separated into a per-boss basis the future.\n§cComing in a future release!",
        category = "Bosses",
        subcategory = "Quality of Life"
    )
    public boolean DISABLE_BOSS_SOUNDS = false;

    // Sounds

    @Property(
        type = PropertyType.DECIMAL_SLIDER,
        name = "8-Bit Katana Volume",
        category = "Sounds",
        maxF = 2f
    )
    public float EIGHT_BIT_KATANA_VOLUME = 1f;

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
        addDependency("SNOWBALL_CANNON_PROJECTION_COLOR", "SNOWBALL_CANNON_PROJECTION");
        addDependency("TRAIN_TIMER_OUTSIDE", "TRAIN_TIMER");
        addDependency("FISHING_DING_DONT_DETECT_SOUND_PACKET", "FISHING_DING");

        addDependency("ITEMCD_DISPLAY_TYPE", "ITEM_COOLDOWN_DISPLAY");
        addDependency("SHOW_ITEMCD_TEXT", "ITEM_COOLDOWN_DISPLAY");
        addDependency("SHOW_ITEMCD_TEXT_CROSSHAIR", "ITEM_COOLDOWN_DISPLAY");
        addDependency("HIDE_ITEMCD_WHEN_READY", "ITEM_COOLDOWN_DISPLAY");
        addDependency("ITEMCD_BACKGROUND_OPACITY", "ITEM_COOLDOWN_DISPLAY");

        addDependency("LIVE_ARENA_MOBS", "LIVE_ARENA_TOGGLE");
        addDependency("LIVE_ARENA_TOTAL_TIMER", "LIVE_ARENA_TOGGLE");
        addDependency("LIVE_ARENA_ROUND_TIMER", "LIVE_ARENA_TOGGLE");
        addDependency("LIVE_ARENA_DROPS", "LIVE_ARENA_TOGGLE");
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
