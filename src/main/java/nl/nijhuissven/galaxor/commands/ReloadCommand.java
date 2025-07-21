package nl.nijhuissven.galaxor.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import nl.nijhuissven.galaxor.util.ChatUtils;
import org.bukkit.command.CommandSender;
import nl.nijhuissven.galaxor.Galaxor;

@CommandAlias("galaxor")
@CommandPermission("galaxor.reload")
public class ReloadCommand extends BaseCommand {

    @Subcommand("reload")
    public void onReload(CommandSender sender) {
        Galaxor.instance().configuration().reload();
        Galaxor.instance().trackerConfiguration().reload();
        sender.sendMessage(ChatUtils.prefixed("Configuration reloaded successfully!"));
    }
}
