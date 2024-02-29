package org.sobadfish.report;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import com.smallaswater.easysql.exceptions.MySqlLoginException;
import com.smallaswater.easysql.mysql.utils.UserData;
import com.smallaswater.easysql.v3.mysql.manager.SqlManager;
import org.sobadfish.report.command.ReportCommand;
import org.sobadfish.report.config.ReportConfig;
import org.sobadfish.report.manager.IDataManager;
import org.sobadfish.report.manager.PlayerInfoManager;
import org.sobadfish.report.manager.ReportSqlManager;
import org.sobadfish.report.manager.ReportYamlManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SoBadFish
 * 2022/1/19
 */
public class ReportMainClass extends PluginBase {

    private static IDataManager dataManager;

    private static ReportMainClass mainClass;

    private ReportConfig reportConfig;

    private Config messageConfig;

    private Config adminPlayerConfig;

    public static int PAGE_SIZE = 20;

    public static int PAGE_OFFSET = 20;

    private PlayerInfoManager playerInfoManager;

    private List<String> adminPlayers = new ArrayList<>();


    @Override
    public void onEnable() {
        mainClass = this;
        saveDefaultConfig();
        reloadConfig();
        PAGE_SIZE = getConfig().getInt(" page-size",20);
        PAGE_OFFSET = getConfig().getInt(" page-offset",20);
        sendMessageToConsole("&e举报系统正在加载");
        if(!initSql()){
            sendMessageToConsole("&c无法接入数据库!");
            dataManager = new ReportYamlManager(this);
        }
        this.getServer().getPluginManager().registerEvents(new ReportListener(),this);
        reportConfig = new ReportConfig(getConfig());
        saveResource("message.yml",false);
        saveResource("players.yml",false);
        messageConfig = new Config(this.getDataFolder()+"/message.yml",Config.YAML);
        adminPlayerConfig = new Config(this.getDataFolder()+"/players.yml",Config.YAML);
        adminPlayers = adminPlayerConfig.getStringList("admin-players");
        playerInfoManager = new PlayerInfoManager();
        this.getServer().getCommandMap().register("report",new ReportCommand("rp"));
        this.getServer().getScheduler().scheduleRepeatingTask(this,playerInfoManager,20);
        sendMessageToConsole("举报系统加载完成");
    }

    public List<String> getAdminPlayers() {
        return adminPlayers;
    }

    public Config getAdminPlayerConfig() {
        return adminPlayerConfig;
    }

    public void saveAdminPlayers(){
        adminPlayerConfig.set("admin-players",adminPlayers);
        adminPlayerConfig.save();
    }

    public PlayerInfoManager getPlayerInfoManager() {
        return playerInfoManager;
    }

    public ReportConfig getReportConfig() {
        return reportConfig;
    }

    public Config getMessageConfig() {
        return messageConfig;
    }

    public static ReportMainClass getMainClass() {
        return mainClass;
    }

    private boolean initSql(){
        sendMessageToConsole("初始化数据库");
        try {

            Class.forName("com.smallaswater.easysql.EasySql");
            String user = getConfig().getString("mysql.username");
            int port = getConfig().getInt("mysql.port");
            String url = getConfig().getString("mysql.host");
            String passWorld = getConfig().getString("mysql.password");
            String table = getConfig().getString("mysql.database");
            UserData data = new UserData(user, passWorld, url, port, table);
            SqlManager sql = new SqlManager(this, data);
            dataManager = new ReportSqlManager(sql);
            if (!((ReportSqlManager) dataManager).createTable()) {
                sendMessageToConsole("&c创建表单 " + ReportSqlManager.SQL_TABLE + " 失败!");
            }
            sendMessageToConsole("&a数据库初始化完成");
            return true;

        }catch (Exception e) {
            sendMessageToConsole("&c数据库初始化失败");
            return false;
        }

    }

    public static IDataManager getDataManager() {
        return dataManager;
    }

    private static void sendMessageToConsole(String msg){
        sendMessageToObject(msg,null);
    }

    public static void sendMessageToAdmin(String msg){
        for(Player player: Server.getInstance().getOnlinePlayers().values()){
            if(player.isOp() || getMainClass().adminPlayers.contains(player.getName())){
                sendMessageToObject(msg,player);
            }
        }
    }

    public static void sendMessageToObject(String msg, CommandSender target){
        if(target == null){
            mainClass.getLogger().info(formatString(msg));
        }else{
            target.sendMessage(formatString(msg));
        }
    }

    public static void sendMessageToAll(String msg){
        Server.getInstance().broadcastMessage(formatString(msg));
    }


    private static String formatString(String str){
        return TextFormat.colorize('&',"&7[&e举报系统&7] &r>&r "+str);
    }
}
