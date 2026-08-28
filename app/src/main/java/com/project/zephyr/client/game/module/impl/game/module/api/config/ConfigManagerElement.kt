import com.project.zephyr.client.constructors.CheatCategory
import com.project.zephyr.client.constructors.Element
import com.project.zephyr.client.game.InterceptablePacket
import com.project.zephyr.client.util.AssetManager

class ConfigManagerElement : Element(
    name = "config_manager",
    category = CheatCategory.Config,
    displayNameResId = AssetManager.getString("module_config_manager")
) {
    override fun beforePacketBound(interceptablePacket: InterceptablePacket) {

    }
} 