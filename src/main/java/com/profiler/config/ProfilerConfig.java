package com.profiler.config;

import com.profiler.common.util.PropertyUtils;
import com.profiler.util.NumberUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ProfilerConfig {


    private static final Logger logger = Logger.getLogger(ProfilerConfig.class.getName());

    private boolean profileEnable = true;
    public static String SERVER_IP = "127.0.0.1";
    public static int SERVER_UDP_PORT = 9995;

    @Deprecated
    public static int AGENT_TCP_LISTEN_PORT = 9990;
    @Deprecated
    public static int SERVER_TCP_LISTEN_PORT = 9991;


    public static long JVM_STAT_GAP = 5000L;
    public static long SERVER_CONNECT_RETRY_GAP = 1000L;

    /**
     * If sql query count is over 10000 it consumes Memory. So sqlHashSet uses
     * CopyOnWriteArraySet. It is slow, but it is stable. Default set is false
     * and it uses HashSet.
     */
    @Deprecated
    public static boolean QUERY_COUNT_OVER_10000 = false;

    private boolean jdbcProfile = true;
    private boolean jdbcProfileMySql = true;
    private boolean jdbcProfileMsSql = true;
    private boolean jdbcProfileOracle = true;
    private boolean jdbcProfileCubrid = true;
    private boolean jdbcProfileDbcp = true;


    public ProfilerConfig() {
    }

    public void readConfigFile() throws IOException{
        String hippoConfigFileName = System.getProperty("hippo.config");
        if (hippoConfigFileName == null) {
            logger.info("hippo.config property is not set. Using default value:" + this);
            return;
        }

        try {
            // TODO file path를 찾는 부분을 수정해야됨 현재 제대로 안찾아짐. 설정파일이 classpath 에 걸려 있지않으므로 파일위치를 못찾음.
            hippoConfigFileName = "hippo.config";
            Properties properties = PropertyUtils.readProperties(hippoConfigFileName);
            readPropertyValues(properties);
        } catch (FileNotFoundException fe) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, hippoConfigFileName + " file is not exists. Please check configuration.");
            }
            throw fe;
        } catch (IOException e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, hippoConfigFileName + " file read error. Cause:" + e.getMessage(), e);
            }
            throw e;
        }
    }

    public boolean isProfileEnable() {
        return profileEnable;
    }

    public boolean isJdbcProfile() {
        return jdbcProfile;
    }

    public boolean isJdbcProfileMySql() {
        return jdbcProfileMySql;
    }

    public boolean isJdbcProfileMsSql() {
        return jdbcProfileMsSql;
    }

    public boolean isJdbcProfileOracle() {
        return jdbcProfileOracle;
    }

    public boolean isJdbcProfileCubrid() {
        return jdbcProfileCubrid;
    }

    private void readPropertyValues(Properties prop) {
        // TODO : use Properties defaultvalue instead of using temp variable.

        this.profileEnable = readBoolean(prop, "PROFILE_ENABLE", true);

        this.SERVER_IP = readString(prop, "SERVER_IP", "127.0.0.1");
        this.SERVER_UDP_PORT = readInt(prop, "SERVER_UDP_PORT", 9995);

        Object temp = null;
        if ((temp = prop.get("AGENT_TCP_LISTEN_PORT")) != null) {
            this.AGENT_TCP_LISTEN_PORT = Integer.parseInt(temp.toString());
            if (logger.isLoggable(Level.INFO)) {
                logger.info("AGENT_TCP_LISTEN_PORT=" + AGENT_TCP_LISTEN_PORT);
            }
        }
        if ((temp = prop.get("SERVER_TCP_LISTEN_PORT")) != null) {
            this.SERVER_TCP_LISTEN_PORT = Integer.parseInt(temp.toString());
            if (logger.isLoggable(Level.INFO)) {
                logger.info("SERVER_TCP_LISTEN_PORT=" + SERVER_TCP_LISTEN_PORT);
            }
        }
        if ((temp = prop.get("JVM_STAT_GAP")) != null) {
            this.JVM_STAT_GAP = Long.parseLong(temp.toString());
            if (logger.isLoggable(Level.INFO)) {
                logger.info("JVM_STAT_GAP=" + JVM_STAT_GAP);
            }
        }
        if ((temp = prop.get("SERVER_CONNECT_RETRY_GAP")) != null) {
            this.SERVER_CONNECT_RETRY_GAP = Long.parseLong(temp.toString());
            if (logger.isLoggable(Level.INFO)) {
                logger.info("SERVER_CONNECT_RETRY_GAP=" + SERVER_CONNECT_RETRY_GAP);
            }
        }
//        if ((temp = prop.get("QUERY_COUNT_OVER_10000")) != null) {
//            this.QUERY_COUNT_OVER_10000 = Boolean.parseBoolean(temp.toString());
//            if (logger.isLoggable(Level.INFO)) {
//                logger.info("QUERY_COUNT_OVER_10000=" + QUERY_COUNT_OVER_10000);
//            }
//        }

        this.jdbcProfile = readBoolean(prop, "JDBC_PROFILE", true);
        this.jdbcProfileMySql = readBoolean(prop, "JDBC_PROFILE_MYSQL", true);
        this.jdbcProfileMsSql = readBoolean(prop, "JDBC_PROFILE_MSSQL", true);
        this.jdbcProfileOracle = readBoolean(prop, "JDBC_PROFILE_ORACLE", true);
        this.jdbcProfileCubrid = readBoolean(prop, "JDBC_PROFILE_CUBRID", true);
        this.jdbcProfileCubrid = readBoolean(prop, "JDBC_PROFILE_DBCP", true);


        logger.info("configuration loaded successfully.");
    }

    private String readString(Properties prop, String propertyName, String defaultValue) {
        String value = prop.getProperty(propertyName, defaultValue);
        if (logger.isLoggable(Level.INFO)) {
            logger.info(propertyName + "=" + value);
        }
        return value;
    }

    private int readInt(Properties prop, String propertyName, int defaultValue) {
        String value = prop.getProperty(propertyName);
        int result = NumberUtils.parseInteger(value, defaultValue);
        if (logger.isLoggable(Level.INFO)) {
            logger.info(propertyName + "=" + result);
        }
        return result;
    }

    private boolean readBoolean(Properties prop, String propertyName, boolean defaultValue) {
        String value = prop.getProperty(propertyName, Boolean.toString(defaultValue));
        boolean result = Boolean.parseBoolean(value);
        if (logger.isLoggable(Level.INFO)) {
            logger.info(propertyName + "=" + result);
        }
        return result;
    }

    public boolean isJdbcProfileDbcp() {
        return jdbcProfileDbcp;
    }

    @Override
    public String toString() {
        return "ProfilerConfig{" +
                "profileEnable=" + profileEnable +
                ", jdbcProfile=" + jdbcProfile +
                ", jdbcProfileMySql=" + jdbcProfileMySql +
                ", jdbcProfileMsSql=" + jdbcProfileMsSql +
                ", jdbcProfileOracle=" + jdbcProfileOracle +
                ", jdbcProfileCubrid=" + jdbcProfileCubrid +
                ", jdbcProfileDbcp=" + jdbcProfileDbcp +
                '}';
    }
}
