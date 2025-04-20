package net.oxyopia.vice.features.itemabilities

import net.minecraft.util.ClickType
import net.oxyopia.vice.data.Debugger
import net.oxyopia.vice.data.Set
import net.oxyopia.vice.utils.ItemUtils.getEquippedSets
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import net.oxyopia.vice.utils.Utils
import kotlin.time.Duration.Companion.milliseconds

/**
 * Inspired from SkyHanni's similar feature, partially adapted
 * under the GNU Lesser General Public License v2.1.
 *
 * @link https://github.com/hannibal002/SkyHanni/blob/beta/src/main/java/at/hannibal2/skyhanni/features/itemabilities/abilitycooldown/ItemAbility.kt
 * @link https://github.com/hannibal002/SkyHanni/blob/beta/LICENSE
 * @author hannibal002
 */
enum class ItemAbility(
    val itemName: String,
    val cooldown: Float,
    val clickType: ClickType = ClickType.RIGHT,
    val set: Set? = null,
    val setAmount: Int = 0,
    val sharedCooldownId : String? = null,
    val soundOnUse: Boolean = true,
    private var lastActivated: Long = 0,
    var lastClicked: Long = 0
) {
    VORTEX_LAUNCHER("Vortex Launcher", 5f, soundOnUse = false),
    CRYSTALINE_BLADE("Crystaline Blade", 10f),
    BURGER_BLADE("Burger Blade", 15f, set = Set.CHEF, setAmount = 1, soundOnUse = false),
    WASTED_SHOTGUN("Wasted Shotgun", 5f),
    BARBED_SHOTGUN("Barbed Shotgun", 5f),
    WASTED_BOOMSTICK("Wasted Boomstick", 7f),
    ARCTIC_CORE("Arctic's Core", 2f, set = Set.DEMOLITIONIST, setAmount = 2),
    ARCTIC_SCROLL("Arctic Scroll", 15f),
    EIGHT_BIT_KATANA("8-Bit Katana", 10f, set = Set.DIGITAL, setAmount = 1),
    ADVENTURER_HOOK("Adventurer's Hook", 3f, sharedCooldownId = "GRAPPLING_HOOK"),
    SLIME_HOOK("Slime Hook", 3f, sharedCooldownId = "GRAPPLING_HOOK"),
    LUMINESCENT_HOOK("Luminescent Hook", 3f, sharedCooldownId = "GRAPPLING_HOOK"),
    GENHOOK("GenHook", 2f, sharedCooldownId = "GRAPPLING_HOOK"),
    BEDROCK_BREAKER("Bedrock Breaker", 10f, set = Set.HEAVY, setAmount = 2),
    DOGE_HAMMER("Doge Hammer", 3f, set = Set.DEMOLITIONIST, setAmount = 2),
    REVOLVER("Revolver", 10f, set = Set.HEAVY, setAmount = 2),
    BARTENDER_GLOVE("Bartender's Glove", 15f, set = Set.CHEF, setAmount = 1),
    DYNAMITE_BARREL("Dynamite Barrel", 10f, set = Set.DEMOLITIONIST, setAmount = 1),
    FLESH_HATCHET("Flesh Hatchet", 10f, set = Set.FLESHCRAWLER, setAmount = 2),
    VIRTUASWORD("VIRTUASWORD", 17.5f, set = Set.DIGITAL, setAmount = 2),
    GLITCH_MALLET("Glitch Mallet", 15f, set = Set.HEAVY, setAmount = 3),
    WARPED_GRENADE("Warped Grenade", 3f),
    POSEIDONS_FURY("Poseidon's Fury", 15f),
    ZIP_BOMB("Zip Bomb", 9f, set = Set.DEMOLITIONIST, setAmount = 2),
    THE_SYNTHFLESH("The Synthflesh", 3f, set = Set.FLESHCRAWLER, setAmount = 2), // 2 for 1st ability, 3 for 2nd ability
    THE_EXPERIMENT("The Experiment", 6f, set = Set.FLESHCRAWLER, setAmount = 2),
    GORE_GAUNTLET("Gore Gauntlet", 10f, set = Set.FLESHCRAWLER, setAmount = 3),
    WAVE_PULSER("Wave Pulser", 10f, set = Set.DIGITAL, setAmount = 2),
    THE_PHANTASM("The Phantasm", 10f, set = Set.HEAVY, setAmount = 1),
    STAR_BOMB("Star Bomb", 8f, set = Set.DEMOLITIONIST, setAmount = 2),
    STARBLADE("Starblade", 2f, set = Set.HEAVY, setAmount = 2),
    BEWITCHED_BLOWPIPE("Bewitched Blowpipe", 5f),
    BROKEN_FLASHLIGHT("Broken Flashlight", 10f),
    GALVANISER("Galvaniser", 20f, set = Set.DIGITAL, setAmount = 3),
    UNSTABLE_DYNAMITE_BARREL("Unstable Dynamite Barrel", 15f, set = Set.DEMOLITIONIST, setAmount = 2),
    EGG_LAUNCHER("Egg Launcher", 4f, set = Set.DEMOLITIONIST, setAmount = 1),

    // No Display
    LASER_POINT_MINIGUN("Laser Point Minigun", 0.5f, set = Set.HEAVY, setAmount = 2),
    BUBBLE_GUN("Bubble Gun", 0.5f),
    CARNAGE_RIFLE("Carnage Rifle", 0.5f, set = Set.FLESHCRAWLER, setAmount = 2),
    SNOWBALL_CANNON("Snowball Cannon", 0.5f),
    SHADOW_GELATO_DRUM_GUN("Shadow Gelato's Drum Gun", 0.1f),
    JYNX_CHAIN_GUN("Jynx's Chain Gun", 0.5f, set = Set.DIGITAL, setAmount = 2),

    // Expeditions
    ASSASSIN_BLADE("Assassin's Blade", 10f),
    C4("C4", 10f),
    CURSED_SKULL("Cursed Skull", 3f),
    GRENADE_BUNDLE("Grenade Bundle", 5f, soundOnUse = false),
    GLOW_BERRIES("Glow Berries", 5f),
    ALL_SEEING_EYE("All Seeing Eye", 30f),

    // Pillars
    SHOTGUN("Shotgun", 6f),
    DOOM_KNOCK_PISTOL("Doom-Knock Pistol", 6f),
    SNIPER("Sniper", 20f),
    THE_PUNISHER("The Punisher", 8f),
    RPG("RPG", 10f),
    MEDKIT("Medkit", 12f);

    val displayCooldown = cooldown >= 1f

    // Detecting whether the ability has ACTUALLY happened is performed in /features/itemabilities/ItemAbilityCooldown.kt
    // This will reset the cooldown regardless of whether it is still on cooldown or not.
    // Functionality to check for cooldowns MAY be added in a feature commit
    fun activate() {
        Debugger.ITEMABILITY.debug("§aActivated as &&b$itemName")
        val now = System.currentTimeMillis()

        sharedCooldownId?.let { sharedCooldowns[it] = now }
        lastActivated = now
    }

    fun isOnCooldown() : Boolean {
        return remainingCooldown() > 0f
    }

    fun remainingCooldown() : Float {
        val lastActivation = sharedCooldownId?.let { sharedCooldowns[it] } ?: lastActivated
        var diff = lastActivation.timeDelta()

        if (this == THE_EXPERIMENT && (Utils.getPlayer()?.health ?: 0f) <= 10f) {
            diff /= 2
        }

        return maxOf(cooldown - (diff.inWholeMilliseconds / 1000.0f), 0f)
    }

    fun onSound() {
        if (this.isOnCooldown()) return
        
        val debounceTime = lastClicked.timeDelta()
        Debugger.ITEMABILITY.debug("§eReceived onSound as &&b$itemName")

        if (debounceTime < 500.milliseconds) {
            activate()
        }
    }

    fun hasSetEquipped(): Boolean = (Utils.getPlayer()?.getEquippedSets()?.get(set) ?: 0) >= setAmount

    companion object {
        var sharedCooldowns = HashMap<String, Long>()

        fun getByName(name: String, clickType: ClickType? = null): ItemAbility? {
            return entries.firstOrNull {
                it.itemName.replace(" ", "") == name.replace(" ", "") &&
                        (clickType == null || it.clickType == clickType)
            }
        }
    }
}
