package liray.itemsaver

import de.tr7zw.changeme.nbtapi.NBTItem
import org.bukkit.Material
import org.bukkit.block.Chest
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

object SaveCommand : TabExecutor {

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {

        if(sender !is Player)
            return false

        if(args[0] == "enable") {
            val item = sender.inventory.itemInMainHand ?: return false
            val nbtItem = NBTItem(item)
            nbtItem.setBoolean("undroppable", true)
            sender.inventory.itemInMainHand = nbtItem.item
            sender.sendMessage("enable")
            return true
        }

        if(args[0] == "disable") {
            val item = sender.inventory.itemInMainHand ?: return false
            val nbtItem = NBTItem(item)
            nbtItem.setBoolean("undroppable", false)
            sender.inventory.itemInMainHand = nbtItem.item
            sender.sendMessage("disable")
            return true
        }

        if(args[0] == "remove") {
            val item = sender.inventory.itemInMainHand ?: return false
            val nbtItem = NBTItem(item)
            nbtItem.removeKey("undroppable")
            sender.inventory.itemInMainHand = nbtItem.item
            sender.sendMessage("remove")
            return true
        }

        return false
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String> {

        return if(args.size == 1) listOf("enable",
                                         "disable",
                                         "remove",
        )
        else listOf()
    }

}