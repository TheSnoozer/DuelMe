package com.teozcommunity.teozfrank.duelme.commands;

import com.teozcommunity.teozfrank.duelme.commands.admin.CreateCmd;
import com.teozcommunity.teozfrank.duelme.commands.admin.DuelAdminCmd;
import com.teozcommunity.teozfrank.duelme.commands.admin.RemoveCmd;
import com.teozcommunity.teozfrank.duelme.commands.admin.SetCmd;
import com.teozcommunity.teozfrank.duelme.commands.duel.AcceptCmd;
import com.teozcommunity.teozfrank.duelme.commands.duel.DuelCmd;
import com.teozcommunity.teozfrank.duelme.commands.duel.SendCmd;
import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.util.DuelArena;
import com.teozcommunity.teozfrank.duelme.util.DuelManager;
import com.teozcommunity.teozfrank.duelme.util.SendConsoleMessage;
import com.teozcommunity.teozfrank.duelme.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by frank on 24/12/13.
 */
public class DuelAdminExecutor extends CmdExecutor implements CommandExecutor {

    public DuelAdminExecutor(DuelMe plugin) {
        super(plugin);

        DuelAdminCmd create = new CreateCmd(plugin, "duelme.admin.create");
        DuelAdminCmd set = new SetCmd(plugin, "duelme.admin.set");
        DuelAdminCmd remove = new RemoveCmd(plugin,"duelme.admin.remove");

        addCmd("create", create, new String[] {
            "c,new"
        });

        addCmd("set", set, new String[]{
                "s"
        });

        addCmd("remove", remove, new String[]{
                "r","delete"
        });

        create.needsObject = false;
        set.needsObject = false;
        remove.needsObject = true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        final String INVALID_PARAMS = ChatColor.RED + "You entered invalid parameters for this command.";


        if (command.getName().equalsIgnoreCase("dueladmin")) {

            if (args.length < 1) {

                Util.sendEmptyMsg(sender, Util.LINE_BREAK);
                Util.sendEmptyMsg(sender, ChatColor.GOLD + "                           DuelMe - Admin Commands");
                Util.sendEmptyMsg(sender, Util.LINE_BREAK);
                Util.sendEmptyMsg(sender, "");
                Util.sendEmptyMsg(sender,ChatColor.GREEN+ "/dueladmin create <arenaname> - "+ ChatColor.GOLD + "create a duel arena with the given name");
                Util.sendEmptyMsg(sender,ChatColor.GREEN+ "/dueladmin remove <arenaname> - "+ ChatColor.GOLD + "removes a duel arena with the given name");
                Util.sendEmptyMsg(sender,ChatColor.GREEN+ "/dueladmin set lobbyspawn - "+ ChatColor.GOLD + "set the lobby spawn");
                Util.sendEmptyMsg(sender, "");
                Util.sendEmptyMsg(sender, Util.LINE_BREAK);
                Util.sendCredits(sender);
                Util.sendEmptyMsg(sender, Util.LINE_BREAK);
                return true;
            }

        }
        DuelArena arena = null;
        String sub = args[0].toLowerCase();
        String objId = "";
        String[] params;

        if (args.length > 1)
            objId = args[1];

        DuelAdminCmd cmd = (DuelAdminCmd) super.getCmd(sub);

        if (cmd == null) {
            Util.sendMsg(sender, ChatColor.RED + "\"" + sub + "\" is not valid for the DuelAdmin command.");
            return true;
        }

        sub = cmd.getCommand(sub);


        if (!cmd.permissible(sender, null)) {
            Util.sendMsg(sender, cmd.NO_PERM);
            return true;
        }

        if (cmd.needsObject) {

            DuelManager dm = plugin.getDuelManager();
            arena = dm.getDuelArenaByName(objId);

            if(dm.getDuelArenas().size() == 0){
                Util.sendMsg(sender,ChatColor.RED + "There are no arenas to remove!");
                return true;
            }

            if(args.length == 1){
                Util.sendMsg(sender,ChatColor.YELLOW + "You must provide a Duel Arena name for this command!");
                return true;
            }

            if (arena == null) {
                Util.sendMsg(sender,ChatColor.RED + "Duel Arena " + ChatColor.AQUA + objId + ChatColor.RED + " does not exist!");
                return true;
            }

            params = makeParams(args, 2);

        } else {

            params = makeParams(args, 1);

            try {
                cmd.run(null, sender, sub, params);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }

        try {
            cmd.run(arena, sender, sub, params);
        } catch (ArrayIndexOutOfBoundsException e) {
            Util.sendMsg(sender, INVALID_PARAMS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }

        return true;
    }

}
