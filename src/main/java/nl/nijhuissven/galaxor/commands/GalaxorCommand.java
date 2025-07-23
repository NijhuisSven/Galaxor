package nl.nijhuissven.galaxor.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.nijhuissven.galaxor.Galaxor;
import nl.nijhuissven.galaxor.util.ChatUtils;
import org.bukkit.command.CommandSender;

@CommandAlias("galaxor")
@CommandPermission("galaxor.command")
public class GalaxorCommand extends BaseCommand {

    @Default
    public void onDefault(CommandSender sender) {
        sender.sendMessage(ChatUtils.prefixed("Command not recognized. Use /galaxor help for a list of commands."));
    }

    @Subcommand("about")
    @CommandPermission("galaxor.about")
    public void onAbout(CommandSender sender) {
        sender.sendMessage(ChatUtils.prefixed("Galaxor Plugin Version: " + Galaxor.instance().getPluginMeta().getVersion()));
        sender.sendMessage(ChatUtils.prefixed("Developed by NijhuisSven"));
        sender.sendMessage(ChatUtils.prefixed("For more information, visit: <u><click:open_url:'https://github.com/nijhuissven/galaxor'>github</click></u>"));
    }

    @HelpCommand
    public void onHelp(CommandSender sender) {
        sender.sendMessage(ChatUtils.prefixed("Available commands:"));
        sender.sendMessage(ChatUtils.prefixed("/galaxor reload - Reload the configuration."));
        sender.sendMessage(ChatUtils.prefixed("/galaxor track [value] - Show your current stats."));
        sender.sendMessage(ChatUtils.prefixed("/galaxor help - Show this help message."));
        sender.sendMessage(ChatUtils.prefixed("/galaxor about - Show the plugin version."));
    }
}
