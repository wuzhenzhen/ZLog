package com.github.library;

import android.content.Context;
import android.os.Environment;
import android.os.Process;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.github.library.flow.ZFlow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 *
 * Copyright  2019
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Created by wzz on 2019/03/02.
 * kgd.zhen
 */
public class ZLog {
    //1 �� Tag
    public static final String TAG = "ZLog";

    // ��־���ͱ�ʶ��(���ȼ����ɵ͵������У�ȡֵԽС���ȼ�Խ��)
    public static final char SHOW_VERBOSE_LOG = 0x01;
    public static final char SHOW_DEBUG_LOG = 0x01 << 1;
    public static final char SHOW_INFO_LOG = 0x01 << 2;
    public static final char SHOW_WARN_LOG = 0x01 << 3;
    public static final char SHOW_ERROR_LOG = 0x01 << 4;
    //����json �����������
    public static final char SHOW_JSON_LOG = 0x01 << 5;
    // ��־�����ʽ
    public static final char LOG_OUTPUT_FORMAT_1 = 0x01;        //��־����Ĭ�ϸ�ʽ
    public static final char LOG_OUTPUT_FORMAT_2 = 0x01 << 1;   //��־���澫���ʽ
    public static final char LOG_OUTPUT_FORMAT_3 = 0x01 << 2;
    //λ����
    public static final char OPERATION_BIT = 0; //����ʾ ĳ����Log
    public static final char NOT_SHOW_LOG = 0; //����ʾ LogTransaction

    //��־����
    public static final String V = "V/";
    public static final String D = "D/";
    public static final String I = "I/";
    public static final String W = "W/";
    public static final String E = "E/";
    public static final String JSON = "JSON/";

    //Tag �ָ����
    public static final String TAG_SEPARATOR = "->";

    public static final char SHOW_ALL_LOG =
            SHOW_VERBOSE_LOG |
                    SHOW_DEBUG_LOG |
                    SHOW_INFO_LOG |
                    SHOW_WARN_LOG |
                    SHOW_ERROR_LOG |
                    SHOW_JSON_LOG;

    public static final char SHOW_FILE_LOG = 0x01;

    public static final char OUTPUT_FORMAT = 0x01; //

    //  Ĭ��Ϊ������־���;��� LogCat �������ʾ
    private static char m_cLogCatShowLogType = SHOW_ALL_LOG;

    // Ĭ��Ϊ������־���;��� ��־�ļ� ���������
    private static char m_cFileSaveLogType = SHOW_ALL_LOG;

    //Ĭ����־�����ʽ
    private static char m_cOutputFormat = OUTPUT_FORMAT;

    //Ĭ����־�����ļ���
    public static final String DEFAULT_LOG_DIR = "logs";

    // �Զ�������־�ļ���Ŀ¼ȫ·��
    public static String sLogFolderPath = "";
    //Application Context ��ֹ�ڴ�й¶
    private static Context mContext;

    //���з�
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    //�ļ� separator
    private static final String FILE_SEPARATOR = File.separator;

    private static final int JSON_INDENT = 3;
    private static final String LOGFILE_SUFFIX = ".log";  //��־��׺��չ��

    //��־��ӡ����
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    //��־��ӡ���� 2
    private static SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm:ss");

    //Ĭ���ļ�Log �ļ���
    private static SimpleDateFormat fileSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    //���߳� ��д�ļ� ��ֹ anr
    private static ExecutorService mSingleExecutors = Executors.newSingleThreadExecutor();
    public static final int INDEX = 5;

    @IntDef({SHOW_VERBOSE_LOG, SHOW_DEBUG_LOG, SHOW_INFO_LOG,
            SHOW_WARN_LOG, SHOW_ERROR_LOG, SHOW_JSON_LOG, NOT_SHOW_LOG})
    @Retention(RetentionPolicy.SOURCE)
    private @interface LockLevel {
    }

    private ZLog(){
        throw new UnsupportedOperationException();
    }

    public static void initialize(@NonNull Context context) {
        mContext = context.getApplicationContext();
        initialize(context, defaultConfig());
    }

    /**
     * @param context
     * @param config
     */
    public static void initialize(@NonNull Context context, @NonNull Config config) {
        mContext = context.getApplicationContext();
        if (config.logSavePath == null || config.logSavePath.trim().equals("")) {
            defaultConfig();
        } else {
            checkSaveLogPath(config.logSavePath);
        }
        if (config.logCatLogLevel != null) {
            m_cLogCatShowLogType = config.logCatLogLevel;
        }
        if (config.fileLogLevel != null) {
            m_cFileSaveLogType = config.fileLogLevel;
            if (m_cFileSaveLogType == NOT_SHOW_LOG)
                mSingleExecutors = null; //Recycle
        }
        if (config.fileOutFormat != null) {
            m_cOutputFormat = config.fileOutFormat;
        }
        ZFlow.initialize(context.getApplicationContext());
    }

    private static Config defaultConfig() {
        Builder builder = newBuilder();

        // ��ѭ����ֻ��Ϊ�˼��ٷ�֧�������
        do {
            String state = Environment.getExternalStorageState();
            // δ��װ SD ��
            if (!Environment.MEDIA_MOUNTED.equals(state)) {
                Log.w(TAG, "Not mount SD card!");
                break;
            }

            // SD ������д
            if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                Log.w(TAG, "Not allow write SD card!");
                break;
            }

            File externalCacheDir = mContext.getExternalCacheDir();
            // context.getExternalCacheDir() maybe null
            if (externalCacheDir != null) {
                builder.logSavePath = externalCacheDir.getAbsolutePath() + File.separator + DEFAULT_LOG_DIR;
            } else {
                Log.e(TAG, "externalCacheDir is null!");
                builder.fileLogLevel(OPERATION_BIT);
                break;
            }

            // ֻ�д����ⲿ SD ���ҿ�д�������²���������־�ļ���ָ��Ŀ¼·����
            // û��ָ����־�ļ����λ�õĻ�����д��Ĭ��λ�ã��� ��ǰӦ�� SD ����Ŀ¼�µ� Cache/LogTransaction Ŀ¼��
            String strSaveLogPath = builder.logSavePath;

            checkSaveLogPath(strSaveLogPath);
        } while (false);

        return new Config(builder);
    }

    private static void checkSaveLogPath(@NonNull String strSaveLogPath) {
        if ("".equals(sLogFolderPath.trim())) {
            File fileSaveLogFolderPath = new File(strSaveLogPath);
            // ������־�ļ���·�������ڵĻ����ʹ�����
            if (!fileSaveLogFolderPath.exists()) {
                boolean mkdirs = fileSaveLogFolderPath.mkdirs();
                if (mkdirs) {
                    Log.i(TAG, "Create log folder success!");
                } else {
                    Log.e(TAG, "Create log folder failed!");
                }
            }

            // ָ����־�ļ������·�����ļ������ڲ�������ʱ����ʽ
            sLogFolderPath = strSaveLogPath;
        }
    }

    public static LogTransaction level(LogLevel logLevel) {
        return new LogStackRecord(logLevel);
    }

    public static LogTransaction v() {
        return new LogStackRecord(LogLevel.Verbose);
    }

    public static LogTransaction d() {
        return new LogStackRecord(LogLevel.Debug);
    }

    public static LogTransaction i() {
        return new LogStackRecord(LogLevel.Info);
    }

    public static LogTransaction w() {
        return new LogStackRecord(LogLevel.Warn);
    }

    public static LogTransaction e() {
        return new LogStackRecord(LogLevel.Error);
    }

    public static LogTransaction json() {
        return new LogStackRecord(LogLevel.Json);
    }


    //1.����̨Log
    public static void v(String tag, Object msg) {
        consoleLog(SHOW_VERBOSE_LOG, msg, tag);
    }

    public static void d(String tag, Object msg) {
        consoleLog(SHOW_DEBUG_LOG, msg, tag);
    }

    public static void i(String tag, Object msg) {
        consoleLog(SHOW_INFO_LOG, msg, tag);
    }

    public static void w(String tag, Object msg) {
        consoleLog(SHOW_WARN_LOG, msg, tag);
    }

    public static void e(String tag, Object msg) {
        consoleLog(SHOW_ERROR_LOG, msg, tag);
    }

    public static void json(String tag, String msg) {
        consoleLog(SHOW_JSON_LOG, msg, tag);
    }

    public static void v(Object msg, final String... tag) {
        consoleLog(SHOW_VERBOSE_LOG, msg, tag);
    }

    public static void d(Object msg, final String... tag) {
        consoleLog(SHOW_DEBUG_LOG, msg, tag);
    }

    public static void i(Object msg, final String... tag) {
        consoleLog(SHOW_INFO_LOG, msg, tag);
    }

    public static void w(Object msg, final String... tag) {
        consoleLog(SHOW_WARN_LOG, msg, tag);
    }

    public static void e(Object msg, final String... tag) {
        consoleLog(SHOW_ERROR_LOG, msg, tag);
    }

    public static void json(String msg, final String... tag) {
        consoleLog(SHOW_JSON_LOG, msg, tag);
    }

    //2.�������Ĭ��Log�ļ�

    public static void vv(final Object msg, final String... tag) {
        writeLog(SHOW_VERBOSE_LOG, msg, null, tag);
    }

    /**
     * @param msg
     * @param tag
     */
    public static void dd(final Object msg, final String... tag) {
        writeLog(SHOW_DEBUG_LOG, msg, null, tag);
    }

    /**
     * @param msg
     * @param tag
     */
    public static void ii(final Object msg, final String... tag) {
        writeLog(SHOW_INFO_LOG, msg, null, tag);
    }

    /**
     * @param msg
     * @param tag
     */
    public static void ww(final Object msg, final String... tag) {
        writeLog(SHOW_WARN_LOG, msg, null, tag);
    }

    /**
     * @param msg
     * @param tag
     */
    public static void ee(final Object msg, final String... tag) {
        writeLog(SHOW_ERROR_LOG, msg, null, tag);
    }

    /**
     * @param msg
     * @param tag
     */
    public static void fjson(final String msg, final String... tag) {
        writeLog(SHOW_JSON_LOG, msg, null, tag);
    }

    //3.����̨ + �ļ�Log
    /**
     * 3.����̨ + �ļ�Log
     *
     * @param msg
     * @param tag
     */
    public static void vvv(final Object msg, final String... tag) {
        //����̨
        consoleLog(SHOW_VERBOSE_LOG, msg, tag);
        //�ļ�Log
        writeLog(SHOW_VERBOSE_LOG, msg, null, tag);
    }

    public static void ddd(final Object msg, final String... tag) {
        consoleLog(SHOW_DEBUG_LOG, msg, tag);

        writeLog(SHOW_DEBUG_LOG, msg, null, tag);
    }

    public static void iii(final Object msg, final String... tag) {
        consoleLog(SHOW_INFO_LOG, msg, tag);

        writeLog(SHOW_INFO_LOG, msg, null, tag);
    }

    public static void www(final Object msg, final String... tag) {
        consoleLog(SHOW_WARN_LOG, msg, tag);

        writeLog(SHOW_WARN_LOG, msg, null, tag);
    }

    public static void eee(final Object msg, final String... tag) {
        consoleLog(SHOW_ERROR_LOG, msg, tag);
        writeLog(SHOW_ERROR_LOG, msg, null, tag);
    }

    /**
     * 3.����̨ + �ļ�Log
     *
     * @param msg
     * @param tag
     */
    public static void cfjson(final Object msg, final String... tag) {
        //����̨log
        consoleLog(SHOW_JSON_LOG, msg, tag);
        //�ļ�Log
        writeLog(SHOW_JSON_LOG, msg, null, tag);
    }

//4.ָ��Log�ļ���,���Log ���ļ�

    /**
     * 4.ָ��Log�ļ���,�����Log ���ļ�
     *
     * @param msg
     * @param logFileName
     * @param tag
     */
    public static void fv(final Object msg, @Nullable final String logFileName, final String... tag) {
        writeLog(SHOW_VERBOSE_LOG, msg, logFileName, tag);
    }

    public static void fd(final Object msg, @Nullable final String logFileName, final String... tag) {
        writeLog(SHOW_DEBUG_LOG, msg, logFileName, tag);
    }

    public static void fi(final Object msg, @Nullable final String logFileName, final String... tag) {
        writeLog(SHOW_INFO_LOG, msg, logFileName, tag);
    }

    public static void fw(final Object msg, @Nullable final String logFileName, final String... tag) {
        writeLog(SHOW_WARN_LOG, msg, logFileName, tag);
    }

    public static void fe(final Object msg, @Nullable final String logFileName, final String... tag) {
        writeLog(SHOW_ERROR_LOG, msg, logFileName, tag);
    }

    /**
     * 4.ָ��Log�ļ���,�����Log ���ļ�
     *
     * @param msg
     * @param logFileName ��־�ļ���
     * @param tag
     */
    public static void ffjson(final String msg, @Nullable final String logFileName, final String... tag) {
        writeLog(SHOW_JSON_LOG, msg, logFileName, tag);
    }

    //4.����̨ + ָ��Log�ļ���,�����Log ���ļ�

    /**
     * 4.����̨ + ָ��Log�ļ���,�����Log ���ļ�
     *
     * @param msg
     * @param logFileName
     * @param tag
     */
    public static void fvv(final Object msg, @Nullable final String logFileName, final String... tag) {
        consoleLog(SHOW_VERBOSE_LOG, msg, tag);

        writeLog(SHOW_VERBOSE_LOG, msg, logFileName, tag);
    }

    public static void fdd(final Object msg, @Nullable final String logFileName, final String... tag) {
        consoleLog(SHOW_DEBUG_LOG, msg, tag);

        writeLog(SHOW_DEBUG_LOG, msg, logFileName, tag);
    }

    public static void fii(final Object msg, @Nullable final String logFileName, final String... tag) {
        consoleLog(SHOW_INFO_LOG, msg, tag);

        writeLog(SHOW_INFO_LOG, msg, logFileName, tag);
    }

    public static void fww(final Object msg, @Nullable final String logFileName, final String... tag) {
        consoleLog(SHOW_WARN_LOG, msg, tag);

        writeLog(SHOW_WARN_LOG, msg, logFileName, tag);
    }

    public static void fee(final Object msg, @Nullable final String logFileName, final String... tag) {
        consoleLog(SHOW_ERROR_LOG, msg, tag);

        writeLog(SHOW_ERROR_LOG, msg, logFileName, tag);

    }

    /**
     * 4.����̨ + ָ��Log�ļ���,�����Log ���ļ�
     *
     * @param msg
     * @param logFileName
     * @param tag
     */
    public static void fcfjson(final String msg, @Nullable final String logFileName, final String... tag) {
        consoleLog(SHOW_JSON_LOG, msg, tag);

        writeLog(SHOW_JSON_LOG, msg, logFileName, tag);
    }


    /**
     * ��msg д����־�ļ�
     *
     * @param msg
     */
    private static void saveLog2File(String msg) {
        // �õ���ǰ����ʱ���ָ����ʽ�ַ���
        String strDateTimeFileName = fileSimpleDateFormat.format(new Date());
        saveLog2File(msg, strDateTimeFileName + LOGFILE_SUFFIX);
    }

    /**
     * ��msg д����־�ļ�
     *
     * @param msg
     * @param logFileName log �ļ���
     */
    private static void saveLog2File(String msg, String logFileName) {
        FileWriter objFilerWriter = null;
        BufferedWriter objBufferedWriter = null;

        do { // ��ѭ����ֻ��Ϊ�˼��ٷ�֧�������
            String state = Environment.getExternalStorageState();
            // δ��װ SD ��
            if (!Environment.MEDIA_MOUNTED.equals(state)) {
                Log.d(TAG, "Not mount SD card!");
                break;
            }

            // SD ������д
            if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                Log.d(TAG, "Not allow write SD card!");
                break;
            }

            File rootPath = new File(sLogFolderPath);
            if (rootPath.exists()) {


                File fileLogFilePath = new File(sLogFolderPath, logFileName);
                // �����־�ļ������ڣ��򴴽���
                if (!fileLogFilePath.exists()) {
                    try {
                        fileLogFilePath.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }

                // ���ִ�е��ⲽ��־�ļ��������ڣ��Ͳ�д��־���ļ���
                if (!fileLogFilePath.exists()) {
                    Log.d(TAG, "Create log file failed!");
                    break;
                }

                try {
                    objFilerWriter = new FileWriter(fileLogFilePath, true); // ��д������
                } catch (IOException e1) {
                    Log.d(TAG, "New FileWriter Instance failed");
                    e1.printStackTrace();
                    break;
                }

                objBufferedWriter = new BufferedWriter(objFilerWriter);

                try {
                    objBufferedWriter.write(msg);
                    objBufferedWriter.flush();
                } catch (IOException e) {
                    Log.d(TAG, "objBufferedWriter.write or objBufferedWriter.flush failed");
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, "LogTransaction savePath invalid!");
            }
        } while (false);

        if (null != objBufferedWriter) {
            try {
                objBufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (null != objFilerWriter) {
            try {
                objFilerWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * @param type
     * @param objectMsg
     * @param tagArgs
     */
    private static void printLog(int type, Object objectMsg, @Nullable String... tagArgs) {
        //��ǰ�̵߳Ķ�ջ���
        int index = 4;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        final StackTraceElement stackTraceElement = stackTrace[index];
        printLog(stackTraceElement, type, objectMsg, tagArgs);
    }

    /**
     * @param stackTraceElement
     * @param type
     * @param objectMsg
     * @param tagArgs
     */
    private static void printLog(final StackTraceElement stackTraceElement, int type, Object objectMsg, @Nullable String... tagArgs) {
        String msg;
        if (m_cLogCatShowLogType == OPERATION_BIT) {
            return;
        }

        String fileName = stackTraceElement.getFileName();
        String methodName = stackTraceElement.getMethodName();
        int lineNumber = stackTraceElement.getLineNumber();

        StringBuilder tagBuilder = new StringBuilder();
        tagBuilder.append(TAG);
        if (tagArgs == null) {
            tagBuilder.append(TAG_SEPARATOR);
            tagBuilder.append(fileName);
        } else {
            for (String tagArg : tagArgs) {
                tagBuilder.append(TAG_SEPARATOR);
                tagBuilder.append(tagArg);
            }
        }

        methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[ (").append(fileName).append(":").append(lineNumber).append(")#").append(methodName).append(" ] ");

        if (objectMsg == null) {
            msg = "null";
        } else {
            msg = objectMsg.toString();
        }
        if (msg != null && type != SHOW_JSON_LOG) {
            stringBuilder.append(msg);
        }

        String logStr = stringBuilder.toString();

        switch (type) {
            case SHOW_VERBOSE_LOG:
                Log.v(tagBuilder.toString(), logStr);
                break;
            case SHOW_DEBUG_LOG:
                Log.d(tagBuilder.toString(), logStr);
                break;
            case SHOW_INFO_LOG:
                Log.i(tagBuilder.toString(), logStr);
                break;
            case SHOW_WARN_LOG:
                Log.w(tagBuilder.toString(), logStr);
                break;
            case SHOW_ERROR_LOG:
                Log.e(tagBuilder.toString(), logStr);
                break;
            case SHOW_JSON_LOG: {
                if (TextUtils.isEmpty(msg)) {
                    Log.d(tagBuilder.toString(), "Empty or Null json content");
                    return;
                }

                String message = null;

                try {
                    if (msg.startsWith("{")) {
                        JSONObject jsonObject = new JSONObject(msg);
                        message = jsonObject.toString(JSON_INDENT);
                    } else if (msg.startsWith("[")) {
                        JSONArray jsonArray = new JSONArray(msg);
                        message = jsonArray.toString(JSON_INDENT);
                    }
                } catch (JSONException e) {
                    e("JSONException/" + tagBuilder.toString(), e.getCause().getMessage() + LINE_SEPARATOR + msg);
                    return;
                }

                printLine(JSON + tagBuilder.toString(), true);
                message = logStr + LINE_SEPARATOR + message;
                String[] lines = message.split(LINE_SEPARATOR);
                StringBuilder jsonContent = new StringBuilder();
                for (String line : lines) {
                    jsonContent.append("�U ").append(line).append(LINE_SEPARATOR);
                }
                Log.d(JSON + tagBuilder.toString(), jsonContent.toString());
                printLine(JSON + tagBuilder.toString(), false);
            }
            break;
        }

    }

    /**
     * дLog ���ļ�
     *
     * @param logLevel
     * @param msg
     * @param logFileName
     * @param tag
     */
    private static void writeLog(@LockLevel final int logLevel, final Object msg, @Nullable final String logFileName, final String... tag) {
        if (OPERATION_BIT != (logLevel &
                m_cFileSaveLogType)) {
            //��ǰ���̵߳Ķ�ջ���
            final StackTraceElement stackTraceElement = getStackTraceElement(INDEX);
            mSingleExecutors.execute(new Runnable() {
                @Override
                public void run() {
                    Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    fileLog(stackTraceElement, logLevel, msg, logFileName, tag);
                }
            });
        }
    }

    public static void writeLog(@LockLevel final int logLevel, int INDEX, final Object msg, @Nullable final String logFileName, final String... tag) {
        if (OPERATION_BIT != (logLevel &
                m_cFileSaveLogType)) {
            //��ǰ���̵߳Ķ�ջ���
            final StackTraceElement stackTraceElement = getStackTraceElement(INDEX);
            mSingleExecutors.execute(new Runnable() {
                @Override
                public void run() {
                    Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    fileLog(stackTraceElement, logLevel, msg, logFileName, tag);
                }
            });
        }
    }

    /**
     * �������̨��־
     *
     * @param logLevel
     * @param msg
     * @param tag
     */
    private static void consoleLog(@LockLevel final int logLevel, Object msg, String... tag) {
        //��ǰ�̵߳Ķ�ջ���
        consoleLog(logLevel, getStackTraceElement(INDEX), msg, tag);
    }

    public static void consoleLog(@LockLevel final int logLevel, int INDEX, Object msg, String[] tag) {
        //��ǰ�̵߳Ķ�ջ���
        consoleLog(logLevel, getStackTraceElement(INDEX), msg, tag);
    }

    /**
     * �������̨��־
     *
     * @param logLevel
     * @param msg
     * @param tag
     */
    private static void consoleLog(@LockLevel final int logLevel, final StackTraceElement stackTraceElement, Object msg, String[] tag) {
        if (OPERATION_BIT != (logLevel &
                m_cLogCatShowLogType)) {
            printLog(stackTraceElement, logLevel, msg, tag);
        }
    }

    /**
     * @param stackTraceElement
     * @param type
     * @param objectMsg
     * @param tagArgs
     */
    private static void fileLog(StackTraceElement stackTraceElement, int type, Object objectMsg, @Nullable String... tagArgs) {
        fileLog(stackTraceElement, type, objectMsg, null, tagArgs);
    }

    private static StackTraceElement getStackTraceElement(int index) {
        //��ǰ�̵߳Ķ�ջ���
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return stackTrace[index];
    }

    /**
     * @param stackTraceElement
     * @param type
     * @param objectMsg
     * @param logFileName
     * @param tagArgs
     */
    private static void fileLog(StackTraceElement stackTraceElement, int type, Object objectMsg, @Nullable String logFileName, @Nullable String... tagArgs) {
        String msg = "";
        if (m_cFileSaveLogType == OPERATION_BIT) {
            return;
        }

        String fileName = stackTraceElement.getFileName();
        String className = stackTraceElement.getClassName();
        String methodName = stackTraceElement.getMethodName();
        int lineNumber = stackTraceElement.getLineNumber();

        StringBuilder tagBuilder = new StringBuilder();
        tagBuilder.append(TAG);
        if (tagArgs == null) {
            tagBuilder.append(TAG_SEPARATOR);
            tagBuilder.append(className);
        } else {
            for (String tagArg : tagArgs) {
                tagBuilder.append(TAG_SEPARATOR);
                tagBuilder.append(tagArg);
            }
        }
        methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);

        StringBuilder stringBuilder = new StringBuilder();
        // �õ���ǰ����ʱ���ָ����ʽ�ַ���
        String strDateTimeLogHead = simpleDateFormat.format(new Date());
        switch (m_cOutputFormat){
            case LOG_OUTPUT_FORMAT_2:
                // �õ���ǰ����ʱ���ָ����ʽ�ַ���
                strDateTimeLogHead = simpleDateFormat2.format(new Date());
                // ����ǩ������ʱ��ͷ����־��Ϣ��������
                stringBuilder
                        .append(tagBuilder.toString())
                        .append(" ")
                        .append(strDateTimeLogHead)
                        .append(" ")
                        .append(className)
                        .append(":")
                        .append(methodName)
                        .append(":")
                        .append(lineNumber)
                        .append(" ");
                if (objectMsg == null) {
                    msg = "null";
                } else {
                    msg = objectMsg.toString();
                }
                if (msg != null && type != SHOW_JSON_LOG) {
                    stringBuilder.append(msg);
                }
                stringBuilder.append(LINE_SEPARATOR + LINE_SEPARATOR);
                break;
            case LOG_OUTPUT_FORMAT_3:
                // �õ���ǰ����ʱ���ָ����ʽ�ַ���
                strDateTimeLogHead = simpleDateFormat2.format(new Date());
                // ����ǩ������ʱ��ͷ����־��Ϣ��������
                stringBuilder
                        .append(tagBuilder.toString())
                        .append(" ")
                        .append(strDateTimeLogHead)
                        .append(" ")
                        .append(fileName)
                        .append(":")
                        .append(methodName)
                        .append("#")
                        .append(lineNumber)
                        .append(" ");
                if (objectMsg == null) {
                    msg = "null";
                } else {
                    msg = objectMsg.toString();
                }
                if (msg != null && type != SHOW_JSON_LOG) {
                    stringBuilder.append(msg);
                }
                stringBuilder.append(LINE_SEPARATOR + LINE_SEPARATOR);
                break;
            case LOG_OUTPUT_FORMAT_1:
            default:
                // ����ǩ������ʱ��ͷ����־��Ϣ��������
                stringBuilder
                        .append(tagBuilder.toString())
                        .append(" ")
                        .append(strDateTimeLogHead)
                        .append(LINE_SEPARATOR)
                        .append("fileName:")
                        .append(fileName)
                        .append(LINE_SEPARATOR)
                        .append("className:")
                        .append(className)
                        .append(LINE_SEPARATOR)
                        .append("methodName:")
                        .append(methodName)
                        .append(LINE_SEPARATOR)
                        .append("lineNumber:")
                        .append(lineNumber)
                        .append(LINE_SEPARATOR);
                if (objectMsg == null) {
                    msg = "null";
                } else {
                    msg = objectMsg.toString();
                }
                if (msg != null && type != SHOW_JSON_LOG) {
                    stringBuilder.append(msg);
                }
                stringBuilder.append(LINE_SEPARATOR + LINE_SEPARATOR);
                    break;
        }

        switch (type) {
            case SHOW_VERBOSE_LOG:
                if (TextUtils.isEmpty(logFileName)) {
                    saveLog2File(V + stringBuilder.toString());
                } else {
                    saveLog2File(V + stringBuilder.toString(), logFileName);
                }

                break;
            case SHOW_DEBUG_LOG:
                if (TextUtils.isEmpty(logFileName)) {
                    saveLog2File(D + stringBuilder.toString());
                } else {
                    saveLog2File(D + stringBuilder.toString(), logFileName);
                }
                break;
            case SHOW_INFO_LOG:
                if (TextUtils.isEmpty(logFileName)) {
                    saveLog2File(I + stringBuilder.toString());
                } else {
                    saveLog2File(I + stringBuilder.toString(), logFileName);
                }

                break;
            case SHOW_WARN_LOG:
                if (TextUtils.isEmpty(logFileName)) {
                    saveLog2File(W + stringBuilder.toString());
                } else {
                    saveLog2File(W + stringBuilder.toString(), logFileName);
                }
                break;
            case SHOW_ERROR_LOG:
                if (TextUtils.isEmpty(logFileName)) {
                    saveLog2File(E + stringBuilder.toString());
                } else {
                    saveLog2File(E + stringBuilder.toString(), logFileName);
                }
                break;
            case SHOW_JSON_LOG: {
                if (TextUtils.isEmpty(msg)) {
                    Log.d(tagBuilder.toString(), "Empty or Null json content");
                    return;
                }

                String message = null;

                try {
                    if (msg.startsWith("{")) {
                        JSONObject jsonObject = new JSONObject(msg);
                        message = jsonObject.toString(JSON_INDENT);
                    } else if (msg.startsWith("[")) {
                        JSONArray jsonArray = new JSONArray(msg);
                        message = jsonArray.toString(JSON_INDENT);
                    }
                } catch (JSONException e) {
                    e("JSONException/" + tagBuilder.toString(), e.getCause().getMessage() + LINE_SEPARATOR + msg);
                    return;
                }

                stringBuilder.append(JSON);
                stringBuilder.append(LINE_SEPARATOR);
                stringBuilder.append("�X�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T");
                message = stringBuilder.toString() + LINE_SEPARATOR + message;
                String[] lines = message.split(LINE_SEPARATOR);
                StringBuilder jsonContent = new StringBuilder();
                for (String line : lines) {
                    jsonContent.append("�U ").append(line).append(LINE_SEPARATOR);
                }
                jsonContent.append("�^�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T");
                if (TextUtils.isEmpty(logFileName)) {
                    saveLog2File(jsonContent.toString());
                } else {
                    saveLog2File(stringBuilder.toString(), logFileName);
                }

            }
            break;
        }

    }

    private static void printLine(String tag, boolean isTop) {
        if (isTop) {
            Log.d(tag, "�X�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T");
        } else {
            Log.d(tag, "�^�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T�T");
        }
    }



    @NonNull
    public static Builder newBuilder() {
        return new Builder();
    }
}
