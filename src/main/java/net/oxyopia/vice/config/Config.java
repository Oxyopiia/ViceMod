package net.oxyopia.vice.config;

import gg.essential.universal.UChat;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;

import java.awt.*;
import java.io.File;

public class Config extends Vigilant {

    // Vice Config

    @Property(
            type = PropertyType.BUTTON,
            name = "Edit HUD Locations",
            description = "Edit the position of HUD Elements",
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
            name = "Snowball Cannon Corrector",
            description = "Attempts to fix the offset of Snowball projectiles launched by the Snowball Cannon.",
            category = "General",
            subcategory = "Quality of Life"
    )
    public boolean CORRECT_SNOWBALL_CANNON = false;

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
            name = "Burger Timers",
            description = "Display a graphic on the screen displaying a live timer for Burgers, or Soda in World 4.",
            category = "General",
            subcategory = "Quality of Life"
    )
    public boolean BURGER_TIMER = false;




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
    public boolean FISHING_WITH_WRONG_HOOK = false;

    
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
    public Color ARENA_DANGER_ZONE_COLOR = new Color(255,0,0);

    
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
            name = "Display Drops",
            description = "Adds drops to Live Arena Info.\nBasic Drops: Amethyst, Polar Fur, Glowing Matter, etc\nUnique Drops: Chance for Vortex Launcher, etc",
            category = "Arenas",
            subcategory = "Live Arena Info",
            options = {"None", "Basic Drops Only", "Unique Drops Only", "All"}
    )
    public int LIVE_ARENA_DROPS = 2;

    
    
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
            name = "8-Bit Katana Sound",
            description = "Coming SoonTM",
            category = "Sounds",
            max = 200,
            increment = 5
    )
    public int EIGHT_BIT_KATANA_VOLUME = 100;

    
    
    // Category /Vice Multiplayer/
    @Property(
            type = PropertyType.SWITCH,
            name = "Arenas Availability Indicator",
            description = "Shares §anon-identifiable §rinformation with other Vice users to inform you and others whether an Arena is available\n" +
                            "Connects to a secure websocket. §cMay negatively impact Network Performance.§r",
            category = "Vice Multiplayer"
    )
    public boolean ARENA_AVAILABILITY_WEBSOCKET_ENABLED = false;

    @Property(
            type = PropertyType.SWITCH,
            name = "Boss Availability Indicator",
            description = "Shares §anon-identifiable §rinformation with other Vice users to inform you and others whether a Boss Arena is available\n" +
                    "Connects to a secure websocket. §cMay negatively impact Network Performance.§r",
            category = "Vice Multiplayer"
    )
    public boolean BOSS_AVAILABILITY_WEBSOCKET_ENABLED = false;

    @Property(
            type = PropertyType.SWITCH,
            name = "Minigun Glitch Notifier",
            description = "Shares §anon-identifiable §rinformation with other Vice users to inform you and others when the Minigun glitch is occuring.\n" +
                    "Connects to a secure websocket. §cMay negatively impact Network Performance.§r",
            category = "Vice Multiplayer"
    )
    public boolean MINIGUN_GLITCH_WEBSOCKET_ENABLED = true;


    public Config() {
        super(new File("./config/main.toml"));
        initialize();
    }
}
