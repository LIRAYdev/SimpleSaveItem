package liray.itemsaver

import de.tr7zw.changeme.nbtapi.NBTItem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.*
import java.util.function.Predicate
import kotlin.collections.HashMap

object EventListener : Listener {

    private val undroppableItems = HashMap<UUID, Array<ItemStack?>>()
    private val playerInventories = HashMap<UUID, HashMap<Int,ItemStack?>>()

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        create(event)
    }

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        if(!player.hasUndroppableItems()) return

        player.addUndroppableItems()
        player.sendMessage("§l§aВаши предметы были успешно сохранены!")
        undroppableItems.remove(player.uniqueId)
        playerInventories.remove(player.uniqueId)

    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        val player = event.entity
        if (player !is Player) return

        playerInventories[player.uniqueId] = getInventoryContent(player.inventory)
    }

    private fun create(event: PlayerDeathEvent) {
        val player = event.entity
        if(!player.hasPlayerInventories()) return

        val undroppableItems = arrayOfNulls<ItemStack?>(41)

        val inventory = playerInventories[player.uniqueId]!!

        for ((index,item) in inventory) {
            val nbtItem = NBTItem(item ?: continue)
            if (!nbtItem.hasKey("undroppable")) continue
            if (!nbtItem.getBoolean("undroppable")) continue
            undroppableItems[index] = item
        }
        undroppableItems.forEach {
            event.drops.remove(it)
        }
        this.undroppableItems[player.uniqueId] = undroppableItems

    }

    private fun Player.addUndroppableItems() {
        player.inventory.contents = undroppableItems[uniqueId]
    }

    private fun Player.hasUndroppableItems(): Boolean {
        return undroppableItems.containsKey(uniqueId)
    }

    private fun Player.hasPlayerInventories(): Boolean {
        return playerInventories.containsKey(uniqueId)
    }

    private fun getInventoryContent(inventory: Inventory): HashMap<Int, ItemStack?> {
        val hashMap = HashMap<Int, ItemStack?>()
        for (i in 0..2 union 36..41) {
            hashMap[i] = inventory.getItem(i)
        }
        return hashMap
    }

}