package com.jeeplus.modules.lu.entity;

/**
 * Created by buwan on 2017/6/20.
 */
public enum AlarmTypeName {
    WARNING1("二十四小时紧急报警", 1100),
    WARNING2("个人救护", 1101),
    WARNING3("紧急", 1102),
    WARNING4("温感报警", 1440),
    WARNING5("防区报警1", 1141),
    WARNING6("防区报警2", 1142),
    WARNING7("防区报警3", 1143),
    WARNING8("防区报警4", 1144),
    WARNING9("防区报警5", 1145),
    WARNING10("报警恢复1", 3102),
    WARNING11("报警恢复2", 3140),
    WARNING12("报警恢复3", 3141),
    WARNING13("报警恢复4", 3142),
    WARNING14("报警恢复5", 3143),
    WARNING15("报警恢复6", 3144),
    WARNING16("报警恢复7", 3145),
    WARNING17("火警1", 1110),
    WARNING18("火警2", 1113),
    WARNING19("火警3", 1114),
    WARNING20("火警4", 1115),
    WARNING21("火警5", 1116),
    WARNING22("火警6", 1117),
    WARNING23("火警7", 1118),
    WARNING24("劫盗1", 1120),
    WARNING25("劫盗2", 1121),
    WARNING26("劫盗3", 1122),
    WARNING27("劫盗4", 1123),
    WARNING28("窃盗1", 1130),
    WARNING29("窃盗2", 1131),
    WARNING30("窃盗3", 1132),
    WARNING31("窃盗4", 1133),
    WARNING32("窃盗5", 1134),
    WARNING33("窃盗6", 1135),
    WARNING34("窃盗7", 1136),
    WARNING35("窃盗8", 1137),
    WARNING36("窃盗9", 1138),
    WARNING37("气体泄漏报警", 1111),
    WARNING38("水侵报警", 1112),
    WARNING39("故障事件1", 1300),
    WARNING40("交流电恢复", 3301),
    WARNING41("无交流", 1301),
    WARNING42("系统电池电压低", 1302),
    WARNING43("系统电池电压恢复", 3302),
    WARNING44("校验故障1", 1303),
    WARNING45("校验故障2", 1304),
    WARNING46("系统重新设定1", 1305),
    WARNING47("系统重新设定2", 1306),
    WARNING48("编程被改动", 1307),
    WARNING49("主机停机", 1308),
    WARNING50("故障事件2", 1309),
//    WARNING54("主机复位1", 130A),
    WARNING51("警号故障1", 1320),
    WARNING52("警号故障2", 1321),
    WARNING53("警号故障3", 1322),
    WARNING54("总线开路", 1331),
    WARNING55("总线恢复", 3331),
    WARNING56("电话线故障1", 1351),
    WARNING57("电话线故障2", 1352),
    WARNING58("通讯失败", 1354),
    WARNING59("无线感应器电池低", 1384),
    WARNING60("无线感应器电池低恢复", 3384),
    WARNING61("无线监控失败", 1381),
    WARNING62("无线监控恢复", 3381),
    WARNING63("主机防拆报警", 1701),
    WARNING64("探测器防拆报警", 1702);

    private String alarmTypeName;
    private int alarmType;

    AlarmTypeName() {
    }

    AlarmTypeName(String alarmTypeName, int alarmType) {
        this.alarmTypeName = alarmTypeName;
        this.alarmType = alarmType;
    }

    public String getAlarmTypeName() {
        return alarmTypeName;
    }

    public void setAlarmTypeName(String alarmTypeName) {
        this.alarmTypeName = alarmTypeName;
    }

    public int getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

    public static AlarmTypeName getByType(int alarmType){
        for(AlarmTypeName alarmTypeNameObject : AlarmTypeName.values()){
            if(alarmType == alarmTypeNameObject.getAlarmType()){
                return alarmTypeNameObject;
            }
        }
        return null;
    }

    public static AlarmTypeName getByName(String alarmTypeName){
        for(AlarmTypeName alarmTypeNameObject : AlarmTypeName.values()){
            if(alarmTypeName.equals(alarmTypeNameObject.getAlarmTypeName())){
                return alarmTypeNameObject;
            }
        }
        return null;
    }
}
