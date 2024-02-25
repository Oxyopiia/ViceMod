package net.oxyopia.vice.features.itemabilities

import net.minecraft.util.ClickType
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.Utils.timeDelta
import net.oxyopia.vice.data.Set

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
    val displayCooldown: Boolean = true,
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
    DOGE_HAMMER("Doge Hammer", 3f),
    REVOLVER("Revolver", 20f, set = Set.HEAVY, setAmount = 2),
    BARTENDER_GLOVE("Bartender's Glove", 15f, set = Set.CHEF, setAmount = 1),
    DYNAMITE_BARREL("Dynamite Barrel", 10f, set = Set.DEMOLITIONIST, setAmount = 1),
    FLESH_HATCHET("Flesh Hatchet", 10f, set = Set.FLESHCRAWLER, setAmount = 2),
    VIRTUASWORD("VIRTUASWORD", 10f, set = Set.DIGITAL, setAmount = 2),
    GLITCH_MALLET("Glitch Mallet", 15f, set = Set.HEAVY, setAmount = 3),
    WARPED_GRENADE("Warped Grenade", 3f),
    POSEIDONS_FURY("Poseidon's Fury", 15f),
    ZIP_BOMB("Zip Bomb", 5f, set = Set.DEMOLITIONIST, setAmount = 2),
    THE_SYNTHFLESH("The Synthflesh", 3f, set = Set.FLESHCRAWLER, setAmount = 2), // 2 for 1st ability, 3 for 2nd ability

    LASER_POINT_MINIGUN("Laser Point Minigun", 0.5f, displayCooldown = false, set = Set.HEAVY, setAmount = 2),
    CARNAGE_RIFLE("Carnage Rifle", 0.5f, displayCooldown = false, set = Set.FLESHCRAWLER, setAmount = 2),
    SNOWBALL_CANNON("Snowball Cannon", 0.5f, displayCooldown = false),
    JYNX_CHAIN_GUN("Jynx's Chain Gun", 0.5f, displayCooldown = false, set = Set.DIGITAL, setAmount = 2);

    // Detecting whether the ability has ACTUALLY happened is performed in /features/itemabilities/ItemAbilityCooldown.kt
    // This will reset the cooldown regardless of whether it is still on cooldown or not.
    // Functionality to check for cooldowns MAY be added in a feature commit
    fun activate() {
        DevUtils.sendDebugChat("&&bITEMABILITY &&aActivated as &&b$itemName", "ITEM_ABILITY_DEBUGGER")
        val now = System.currentTimeMillis()

        sharedCooldownId?.let { sharedCooldowns[it] = now }
        lastActivated = now
    }

    fun isOnCooldown() : Boolean {
        return remainingCooldown() > 0f
    }

    fun remainingCooldown() : Float {
        val lastActivation = sharedCooldownId?.let { sharedCooldowns[it] } ?: lastActivated
        val diff = lastActivation.timeDelta()

        return maxOf(cooldown - (diff / 1000.0f), 0f)
    }

    fun onSound() {
        val debounceTime = lastClicked.timeDelta()
        DevUtils.sendDebugChat("&&bITEMABILITY &&eReceived onSound as &&b$itemName", "ITEM_ABILITY_DEBUGGER")

        if (debounceTime < 500) {
            activate()
        }
    }

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
