package liray.itemsaver

import de.tr7zw.changeme.nbtapi.NBTItem
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.collections.HashMap

object EventListener : Listener {

    private val playerHashMap = HashMap<UUID, MutableList<ItemStack>>()

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        createListUndroppableItems(event)
    }

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        if(!player.hasUndroppableItems()) return
        if(playerHashMap[player.uniqueId]!!.size == 0) {
            playerHashMap.remove(player.uniqueId)
            return
        }

        player.addUndroppableItems()
        val count = playerHashMap[player.uniqueId]!!.size
        player.sendMessage("§l§aВаши предметы были успешно сохранены! Сохранено слотов: $count")
        playerHashMap.remove(player.uniqueId)

    }

    private fun createListUndroppableItems(event: PlayerDeathEvent) {

        val undroppableItems = mutableListOf<ItemStack>()

        val drops = event.drops
        val player = event.entity

        for (item in drops) {
            val nbtItem = NBTItem(item)
            if (!nbtItem.hasKey("undroppable")) continue
            if (!nbtItem.getBoolean("undroppable")) continue
            undroppableItems.add(item)
        }
        playerHashMap[player.uniqueId] = undroppableItems
        event.drops.removeAll(undroppableItems)
    }

    private fun Player.addUndroppableItems() {
        val list = playerHashMap[uniqueId]!!
        inventory.addItem(*list.toTypedArray())
    }

    private fun Player.hasUndroppableItems(): Boolean {
        return playerHashMap.containsKey(uniqueId)
    }

}