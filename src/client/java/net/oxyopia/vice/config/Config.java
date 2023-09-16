package net.oxyopia.vice.config;

import gg.essential.universal.UChat;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
    import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Config extends Vigilant {

    // Vice Config

    @Property(
        type = PropertyType.BUTTON,
        name = "Edit HUD Locations",
        description = "Edit the position of HUD Elements\n" +
            "Drag elements in the menu to reposition\n" +
            "Use the scroll wheel on an element to resize.",
        category = "General",
        subcategory = "Vice"
    )
    public void EDIT_HUD_LOCATIONS() {
        UChat.chat("Soon");
    }

    // Quality of Life

    @Property(
        type = PropertyType.SWITCH,
        name = "Copy Chat Messages",
        description = "Allows for Right Clicking chat messages to Copy them to Clipboard.",
        category = "General",
        subcategory = "Vice"
    )
    public boolean COPY_CHAT_TO_CLIPBOARD = true;

    @Property(
            type = PropertyType.SWITCH,
            name = "Fullbright",
            description = "Maximizes gamma",
            category = "General",
            subcategory = "Vice"
    )
    public boolean VICE_FULLBRIGHT = false;

    @Property(
        type = PropertyType.SWITCH,
        name = "Snowball Cannon Corrector",
        description = "Attempts to fix the offset of Snowball projectiles launched by the Snowball Cannon.",
        category = "General",
        subcategory = "Quality of Life"
    )
    public boolean CORRECT_SNOWBALL_CANNON = false;

    @Property(
            type = PropertyType.SWITCH,
            name = "Snowball Cannon Projection",
            description = "Displays a line of projection of where the Snowball Cannon can reach.",
            category = "General",
            subcategory = "Quality of Life"
    )
    public boolean SNOWBALL_CANNON_PROJECTION = false;

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
            name = "Hide Server Tips",
            description = "Hides messages such as the periodic Discord tip in chat.",
            category = "General",
            subcategory = "Quality of Life"
    )
    public boolean HIDE_SERVER_TIPS = true;

    @Property(
            type = PropertyType.SWITCH,
            name = "World 4 Held Item Display",
            description = "Displays what item you currently have held in Arena 4, such as Cheese, or Soda",
            category = "General",
            subcategory = "Quality of Life"
    )
    public boolean W4_HELD_ITEM_DISPLAY = true;

    @Property(
        type = PropertyType.SWITCH,
        name = "Burger Timers",
        description = "Displays a graphic on the screen displaying a live timer for Burgers, or Soda in World 4.",
        category = "General",
        subcategory = "Quality of Life"
    )
    public boolean W4_BURGER_TIMER = false;


    @Property(
        type = PropertyType.SWITCH,
        name = "Silence Spawn Timer",
        description = "Displays a graphic on the screen after Silence has spawned §eat §eleast §eonce§r displaying a timer when he will next spawn.",
        category = "General",
        subcategory = "Quality of Life"
    )
    public boolean SILENCE_SPAWN_TIMER = false;


    // Quality of Life/Fishing

    @Property(
        type = PropertyType.SWITCH,
        name = "Fishing Bite Ding",
        description = "Plays a ding sound when your bobber is bitten.",
        category = "General",
        subcategory = "Fishing"
    )
    public boolean FISHING_DING = true;

    @Property(
        type = PropertyType.SWITCH,
        name = "Disable Other's Bites",
        description = "Disable the Splash sound effect from other people's bobbers. Useful if Semi-AFKing.",
        category = "General",
        subcategory = "Fishing"
    )
    public boolean DISABLE_OTHERS_FISHING_BITES = false;

    @Property(
        type = PropertyType.SWITCH,
        name = "Fishing with Wrong Hook Warning",
        description = "Displays a warning when fishing with a Slime, Luminous, or GenHook.",
        category = "General",
        subcategory = "Fishing"
    )
    public boolean FISHING_WITH_WRONG_HOOK = true;


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
        subcategory = "Quality of Life"
    )
    public Color ARENA_DANGER_ZONE_COLOR = new Color(255, 0, 0);


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
            description = "Adjusts the Bossbar to display the Bosse's despawn timer.",
            category = "Bosses",
            subcategory = "Quality of Life"
    )
    public boolean BOSS_DESPAWN_TIMERS = true;
    
    @Property(
            type = PropertyType.SWITCH,
            name = "Disable Boss Sounds",
            description = "Removes boss sounds, for now only works with PPP, may be separated in the future.",
            category = "Bosses",
            subcategory = "Quality of Life"
    )
    public boolean DISABLE_BOSS_SOUNDS = false;

    // Category /Bosses/PPP
    @Property(
        type = PropertyType.SWITCH,
        name = "PPP Burp Warning",
        description = "Displays a warning during PPP's Burp Ability, which spawns dropping Eyes.",
        category = "Bosses",
        subcategory = "PPP"
    )
    public boolean PPP_BURP_WARNING = false;


    // Category /Bosses/Minehut Boss
    @Property(
        type = PropertyType.SWITCH,
        name = "Minehut (Gen) Furnace Warning",
        description = "Displays a warning while Gen is charging its Furnace ability.",
        category = "Bosses",
        subcategory = "Minehut Boss"
    )
    public boolean MH_GEN_WARNING = false;

    // Category /Bosses/Arenas
    @Property(
        type = PropertyType.SWITCH,
        name = "Santa Present Warning",
        description = "Displays a warning when Santa uses his present throw ability.",
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


    // Category /Vice Multiplayer/
    @Property(
        type = PropertyType.SWITCH,
        name = "Arenas Availability Indicator",
        description = "Shares §anon-identifiable §rinformation with other Vice users to inform you and others whether an Arena is available\n" +
            "Connects to a secure websocket.",
        category = "Vice Multiplayer"
    )
    public boolean ARENA_AVAILABILITY_WEBSOCKET_ENABLED = false;

    @Property(
        type = PropertyType.SWITCH,
        name = "Boss Availability Indicator",
        description = "Shares §anon-identifiable §rinformation with other Vice users to inform you and others whether a Boss Arena is available\n" +
            "Connects to a secure websocket.",
        category = "Vice Multiplayer"
    )
    public boolean BOSS_AVAILABILITY_WEBSOCKET_ENABLED = false;

    @Property(
        type = PropertyType.SWITCH,
        name = "Shared Silence Spawn Timer",
        description = "Acts as an extension to the pre existing Silence Spawn timer. Shares §anon-identifiable §rinformation with other Vice users.\n" +
            "Connects to a secure websocket.\n" +
            "§cRequires §cSilence §cSpawn §cTimer §cto §cbe §cenabled.",
        category = "Vice Multiplayer"
    )
    public boolean SILENCE_TIMER_WEBSOCKET_ENABLED = false;



    public Config() {
        super(new File("./config/vice.toml"), "Vice", new JVMAnnotationPropertyCollector(), new ConfigSorting());
        initialize();
        markDirty();
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
