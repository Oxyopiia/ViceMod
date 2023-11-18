package net.oxyopia.vice.config;

import gg.essential.api.EssentialAPI;
import gg.essential.universal.UDesktop;
import gg.essential.universal.UScreen;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.net.URI;
import java.util.Comparator;
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
        EssentialAPI.getNotifications().push("Vice", "HUD Manager coming soon", 3f);
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

    // Quality of Life


//    public boolean COPY_CHAT_TO_CLIPBOARD = true;

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
        type = PropertyType.SWITCH,
        name = "Snowball Cannon Projection",
        description = "Displays a line of projection of where the Snowball Cannon can reach.",
        category = "General",
        subcategory = "Quality of Life"
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
        name = "Display Defence Number",
        description = "Displays your exact defence number of all pieces of your armor combined.",
        category = "General",
        subcategory = "Quality of Life",
        searchTags = {"armour"}
    )
    public boolean DISPLAY_DEFENCE_HUD = false;

// Add back soon
//    @Property(
//        type = PropertyType.SWITCH,
//        name = "Hide Armor Bar",
//        description = "Stops the armor bar from rendering.",
//        category = "General",
//        subcategory = "Quality of Life",
//        searchTags = {"armour", "defence"}
//    )
//    public boolean HIDE_ARMOR_BAR = false;
//
//    @Property(
//        type = PropertyType.SWITCH,
//        name = "Hide Hunger Bar",
//        description = "Stops the hunger bar from rendering.",
//        category = "General",
//        subcategory = "Quality of Life",
//        searchTags = {"food", "meter"}
//    )
//    public boolean HIDE_HUNGER_BAR = true;

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
            subcategory = "Quality of Life"
    )
    public boolean HIDE_SERVER_TIPS = true;

    // General/Item Cooldown Display
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
    	description = "Displays a text stating the cooldown remaining or when ready near your crosshair.",
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
    	description = "Hides the Cooldown titles when trying to use an item while on Cooldown\n",
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

    @Property(
        type = PropertyType.SWITCH,
        name = "Oyster Queen Display",
        description = "Displays your current quest progress for Oyster Queen while in World 6.",
        category = "General",
        subcategory = "Fishing"
    )
    public boolean OYSTER_QUEEN_DISPLAY = false;



    // Category /General/Bug Fixes
    @Property(
            type = PropertyType.SWITCH,
            name = "Fix Player Count in Scoreboard",
            description = "When not killing a mob in a while, the scoreboard becomes desynced with the player count, so this attempts to fix it.",
            category = "General",
            subcategory = "Bug Fixes"
    )
    public boolean FIX_SCOREBOARD_PLAYER_COUNT = true;


    // Category /Arenas/Quality of Life

    @Property(
        type = PropertyType.SWITCH,
        name = "Draw Danger Zones",
        description = "Draws a Bounding Box around the zone where Mobs spawn in Arenas",
        category = "Arenas",
        subcategory = "Quality of Life"
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
        subcategory = "Quality of Life"
    )
    public boolean ARENAS_COOLDOWN_NOTIFIER = true;


    // Category /Arenas/Live Arena Info
    @Property(
        type = PropertyType.SWITCH,
        name = "Live Arena Information",
        description = "Main Toggle for Live Arena Information, useful for preserving settings. At base, displays current Wave.",
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
    public boolean LIVE_ARENA_TIMER = true;

    @Property(
        type = PropertyType.SELECTOR,
        name = "Display Projected Drops",
        description = "Adds drops to Live Arena Info.\nBasic Drops: Amethyst, Polar Fur, Glowing Matter, etc\nUnique Drops: Chance for Galactic Hand Cannon, Arctic Scroll, etc",
        category = "Arenas",
        subcategory = "Live Arena Info",
        options = {"None", "Basic Drops Only", "Unique Drops Only", "All"}
    )
    public int LIVE_ARENA_DROPS = 2;

    // Category /Bosses/Quality of Life
    @Property(
        type = PropertyType.SWITCH,
        name = "Boss Despawn Timers",
        description = "Adjusts the Bossbar to display the Bosse's despawn timer.\n§cComing in a future release!",
        category = "Bosses",
        subcategory = "Quality of Life"
    )
    public boolean BOSS_DESPAWN_TIMERS = true;
    
    @Property(
        type = PropertyType.SWITCH,
        name = "Disable Boss Sounds",
        description = "Removes boss sounds, for now only works with PPP, may be separated in the future.\n§cComing in a future release!",
        category = "Bosses",
        subcategory = "Quality of Life"
    )
    public boolean DISABLE_BOSS_SOUNDS = false;
    
    @Property(
        type = PropertyType.SWITCH,
        name = "Nearby Amethyst Warning",
        description = "Plays a warning chime when the player is within the damage radius of a falling Amethyst",
        category = "Bosses",
        subcategory = "Shadow Gelato"
    )
    public boolean SHADOWGELATO_AMETHYST_WARNING = false;
    
    @Property(
        type = PropertyType.SWITCH,
        name = "Miniboss Health Info",
        description = "Displays health of Shadow Gelato's Minibosses in Phase 2",
        category = "Bosses",
        subcategory = "Shadow Gelato"
    )
    public boolean SHADOWGELATO_MINIBOSS_HEALTH = false;

    // Category /Bosses/Arenas
    @Property(
        type = PropertyType.SWITCH,
        name = "Santa Present Warning",
        description = "Displays a warning when Santa uses his present throw ability.\n§cComing in a future release!",
        category = "Bosses",
        subcategory = "Arenas"
    )
    public boolean ARENAS_SANTA_PRESENT_WARNING = false;


    // Category /Sounds/
    @Property(
        type = PropertyType.SLIDER,
        name = "8-Bit Katana Volume",
        description = "Coming SoonTM",
        category = "Sounds",
        max = 200,
        increment = 10
    )
    public int EIGHT_BIT_KATANA_VOLUME = 100;

//
//    // Category /Vice Multiplayer/
//    @Property(
//        type = PropertyType.SWITCH,
//        name = "Arenas Availability Indicator",
//        description = "Shares §anon-identifiable §rinformation with other Vice users to inform you and others whether an Arena is available\n" +
//            "Connects to a secure websocket.",
//        category = "Vice Multiplayer"
//    )
//    public boolean ARENA_AVAILABILITY_WEBSOCKET_ENABLED = false;
//
//    @Property(
//        type = PropertyType.SWITCH,
//        name = "Boss Availability Indicator",
//        description = "Shares §anon-identifiable §rinformation with other Vice users to inform you and others whether a Boss Arena is available\n" +
//            "Connects to a secure websocket.",
//        category = "Vice Multiplayer"
//    )
//    public boolean BOSS_AVAILABILITY_WEBSOCKET_ENABLED = false;
//
//    @Property(
//        type = PropertyType.SWITCH,
//        name = "Shared Silence Spawn Timer",
//        description = "Acts as an extension to the pre existing Silence Spawn timer. Shares §anon-identifiable §rinformation with other Vice users.\n" +
//            "Connects to a secure websocket.\n" +
//            "§cRequires §cSilence §cSpawn §cTimer §cto §cbe §cenabled.",
//        category = "Vice Multiplayer"
//    )
//    public boolean SILENCE_TIMER_WEBSOCKET_ENABLED = false;


    public Config() {
        super(new File("./config/vice/config.toml"), "Vice", new JVMAnnotationPropertyCollector(), new ConfigSorting());
    }

    public void init() {
        initialize();
        markDirty();

        addDependency("ARENA_DANGER_ZONE_COLOR", "DRAW_ARENA_DANGER_ZONES");
        addDependency("SNOWBALL_CANNON_PROJECTION_COLOR", "SNOWBALL_CANNON_PROJECTION");
        addDependency("FISHING_DING_DONT_DETECT_SOUND_PACKET", "FISHING_DING");

        addDependency("ITEMCD_DISPLAY_TYPE", "ITEM_COOLDOWN_DISPLAY");
        addDependency("SHOW_ITEMCD_TEXT", "ITEM_COOLDOWN_DISPLAY");
        addDependency("SHOW_ITEMCD_TEXT_CROSSHAIR", "ITEM_COOLDOWN_DISPLAY");
        addDependency("HIDE_ITEMCD_WHEN_READY", "ITEM_COOLDOWN_DISPLAY");
        addDependency("ITEMCD_BACKGROUND_OPACITY", "ITEM_COOLDOWN_DISPLAY");

        setCategoryDescription("Arenas", "§cFEATURES COMING SOON" + "\n§7i cannot be bothered to remove the section just to re add it when i make it, nothing works rn ok?");
        setCategoryDescription("Sounds", "§cFEATURES COMING SOON" + "\n§7i cannot be bothered to remove the section just to re add it when i make it, nothing works rn ok?");
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

                if ("Vice".equals(key1)) {
                    return -1;
                } else if ("Vice".equals(key2)) {
                    return 1;
                } else if ("Quality of Life".equals(key1)) {
                    return -1;
                } else if ("Quality of Life".equals(key2)) {
                    return 1;
                } else {
                    // If neither "Vice" nor "Quality of Life", compare alphabetically
                    return key1.compareTo(key2);
                }
            };
        }
    }
}
