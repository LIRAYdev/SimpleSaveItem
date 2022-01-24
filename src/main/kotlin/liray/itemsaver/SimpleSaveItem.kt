package liray.itemsaver

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

lateinit var plugin: SimpleSaveItem

class SimpleSaveItem : JavaPlugin() {

    override fun onEnable() {
        plugin = this
        Bukkit.getPluginManager().registerEvents(EventListener, plugin)
        getCommand("save-item").executor = SaveCommand
    }

}