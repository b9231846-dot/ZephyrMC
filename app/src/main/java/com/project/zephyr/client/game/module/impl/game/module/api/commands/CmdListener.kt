import com.project.zephyr.client.constructors.CheatCategory
import com.project.zephyr.client.constructors.Element
import com.project.zephyr.client.constructors.GameManager
import com.project.zephyr.client.game.InterceptablePacket
import com.project.zephyr.client.util.AssetManager

class CmdListener(private val moduleManager: GameManager) : Element(
    name = "ChatListener",
    category = CheatCategory.Misc,
    displayNameResId = AssetManager.getString("module_chat_listener")
) {
    override fun beforePacketBound(interceptablePacket: InterceptablePacket) {

    }
} 