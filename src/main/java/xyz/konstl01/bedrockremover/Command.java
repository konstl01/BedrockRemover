package xyz.konstl01.bedrockremover;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        sender.sendMessage(BedrockRemover.getPrefix()+"Replaced "+BedrockRemover.count+" Blocks of Bedrock");
        return true;
    }
}
