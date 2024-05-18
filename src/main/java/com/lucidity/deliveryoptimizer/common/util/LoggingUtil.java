package com.lucidity.deliveryoptimizer.common.util;


import com.lucidity.deliveryoptimizer.common.constants.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.slf4j.MDC;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.Map;

public class LoggingUtil {
    private static final Logger logger = LogManager.getLogger(LoggingUtil.class);

    private LoggingUtil() {
    }

    public static void info(JSONObject data) {
        logger.info("{}", addMetaData(data));
    }

    public static void debug(JSONObject data) {
        logger.debug("{}", addMetaData(data));
    }

    public static void error(JSONObject data) {
        logger.error("{}", addMetaData(data));
    }

    public static void info(Map<String, String> data) {
        StringBuilder logBuilder = new StringBuilder();
        addMetaData(data);
        for (Map.Entry<String, String> entry : data.entrySet()) {
            logBuilder.append(entry.getKey()).append(" = ").append(entry.getValue()).append(", ");
        }
        logger.info("INFO : {}", logBuilder);
    }

    public static void error(Map<String, String> data) {
        StringBuilder logBuilder = new StringBuilder();
        addMetaData(data);
        for (Map.Entry<String, String> entry : data.entrySet()) {
            logBuilder.append(entry.getKey()).append(" = ").append(entry.getValue()).append(", ");
        }
        logger.error("ERROR : {}", logBuilder);
    }

    public static void debug(Map<String, String> data) {
        StringBuilder logBuilder = new StringBuilder();
        addMetaData(data);
        for (Map.Entry<String, String> entry : data.entrySet()) {
            logBuilder.append(entry.getKey()).append(" = ").append(entry.getValue()).append(", ");
        }
        logger.debug("DEBUG : {}", logBuilder);
    }

    private static void addMetaData(Map<String, String> data) {
        data.put(Constant.Logging.KEY_TIME, String.valueOf(Calendar.getInstance().getTime()));
        data.put(Constant.Logging.KEY_EPOCH_TIME, String.valueOf(System.currentTimeMillis()));
        if (!CollectionUtils.isEmpty(MDC.getCopyOfContextMap())) {
            for (Map.Entry<String, String> entry : MDC.getCopyOfContextMap().entrySet()) {
                data.put(entry.getKey(), entry.getValue());
            }
        }
    }

    private static JSONObject addMetaData(JSONObject data) {
        data.put(Constant.Logging.KEY_TIME, Calendar.getInstance().getTime());
        data.put(Constant.Logging.KEY_EPOCH_TIME, System.currentTimeMillis());
        if (!CollectionUtils.isEmpty(MDC.getCopyOfContextMap())) {
            for (Map.Entry<String, String> entry : MDC.getCopyOfContextMap().entrySet()) {
                data.put(entry.getKey(), entry.getValue());
            }
        }
        return data;
    }
}
