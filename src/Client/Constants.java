package Client;

public class Constants {
    public static int KING_HP_DELIMITER = 23;
    public static int MAP_UNIT_HAZARD_EFFECTIVENESS_DELIMITER = 135;
    public static int MAP_UNIT_RANGE_RATE_DELIMITER = 2;
    public static int MAP_UNIT_DAMAGE_RATE_DELIMITER = 2;
    public static int MAP_UNIT_HP_RATE_DELIMITER = 10;
    public static int MAP_UNIT_DISTANCE_RATE_DELIMITER = 5;
    public static int REWARD_FOR_WINNING = 100;
    public static int REWARD_FOR_LOOSING = -100;
    public static int REWARD_FOR_BITING_OPPONENT_KING = 30;
    public static int REWARD_FOR_BITING_YOUR_KING = -20;
    public static int REWARD_FOR_KILLING_OPPONENT_UNIT = 4;
    public static int REWARD_FOR_KILLING_YOUR_UNIT = -2;
    public static int FINAL_TURN = 100;
    public static int LOOSE_HP_PEAK = 10;
    public static int WIN_OPPONENT_HP_PEAK = 20;
    public static String TRANSITION_TABLE_FILE_PATH = "transition_table.txt";
    public static int IO_HANDLER_PORT = 4999;
    public static Byte IO_READ_ORDER = 'R';
    public static Byte IO_WRITE_ORDER = 'W';
    public static Byte IO_MESSAGE_VIEWER = 'M';
    public static Byte IO_CLIENT_DONE = 'F';
    public static Byte IO_CLIENT_CLOSE = 'D';
    public static String IO_EOF = "finish";
    public static int NUMBER_OF_PARALLEL_RUNNING_CLIENTS = 4;
    public static String CMD = "cmd";
    public static int PORT_RUNNER_MAKER = 6390;
    public static String CLIENT_RUN_ABDI_COMMAND_PART1 = "\"C:\\Program Files\\Java\\jdk1.8.0_201\\bin\\java.exe\" \"-javaagent:C:\\Program Files\\JetBrains\\IntelliJ IDEA 2019.3.3\\lib\\idea_rt.jar=";
    public static String CLIENT_RUN_ABDI_COMMAND_PART2 = ":C:\\Program Files\\JetBrains\\IntelliJ IDEA 2019.3.3\\bin\" -Dfile.encoding=UTF-8 -classpath \"C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\charsets.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\deploy.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\ext\\access-bridge-64.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\ext\\cldrdata.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\ext\\dnsns.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\ext\\jaccess.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\ext\\jfxrt.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\ext\\localedata.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\ext\\nashorn.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\ext\\sunec.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\ext\\sunjce_provider.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\ext\\sunmscapi.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\ext\\sunpkcs11.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\ext\\zipfs.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\javaws.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\jce.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\jfr.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\jfxswt.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\jsse.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\management-agent.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\plugin.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\resources.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\rt.jar;C:\\Users\\nick white\\Desktop\\ai challange 2020\\AIC20-changiz-master\\out\\production\\AIC20-changiz-master;D:\\software\\java-lib\\gson-2.8.5.jar\" Client.Main";
    public static String IO_RUN_ABDI_COMMAND_PART1 = "\"C:\\Program Files\\Java\\jdk1.8.0_201\\bin\\java.exe\" \"-javaagent:C:\\Program Files\\JetBrains\\IntelliJ IDEA 2019.3.3\\lib\\idea_rt.jar=";
    public static String IO_RUN_ABDI_COMMAND_PART2 = ":C:\\Program Files\\JetBrains\\IntelliJ IDEA 2019.3.3\\bin\" -Dfile.encoding=UTF-8 -classpath \"C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\charsets.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\deploy.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\ext\\access-bridge-64.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\ext\\cldrdata.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\ext\\dnsns.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\ext\\jaccess.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\ext\\jfxrt.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\ext\\localedata.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\ext\\nashorn.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\ext\\sunec.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\ext\\sunjce_provider.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\ext\\sunmscapi.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\ext\\sunpkcs11.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\ext\\zipfs.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\javaws.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\jce.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\jfr.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\jfxswt.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\jsse.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\management-agent.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\plugin.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\resources.jar;C:\\Program Files\\Java\\jdk1.8.0_201\\jre\\lib\\rt.jar;C:\\Users\\nick white\\Desktop\\ai challange 2020\\AIC20-changiz-master\\out\\production\\AIC20-changiz-master;D:\\software\\java-lib\\gson-2.8.5.jar\" Client.IOHandler";
    public static String[] SERVER_RUN_ABDI_COMMAND = {"\"C:\\Program Files\\Java\\jdk1.8.0_201\\bin\\java.exe\" -Dfile.encoding=windows-1252 -jar \"C:\\Users\\nick white\\Desktop\\ai challange 2020\\AIC20-changiz-master\\server\\server_v1.1.4.jar\""};
}
