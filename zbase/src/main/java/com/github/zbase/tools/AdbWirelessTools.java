package com.github.zbase.tools;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

/**
 * Created by wzz on 2018/05/25.
 * wuzhenzhen@tiamaes.com
 *
 * 用来开启Adb 无线调试
 *  注:ip地址最后采用固定的方便调试
 */

public class AdbWirelessTools {
    private static final String MSG_TAG = "AdbWirelessTools";
    private static final String PORT = "5555";

    public static boolean adbStart() {
         Log.d(MSG_TAG, "adbStart()");
        try {
            setProp("service.adb.tcp.port", PORT);
            if (isProcessRunning("adbd")) {
                runRootCommand("stop adbd");
            }
            runRootCommand("start adbd");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean adbStop() throws Exception {
         Log.d(MSG_TAG, "adbStop()");
        try {
            setProp("service.adb.tcp.port", "-1");
            runRootCommand("stop adbd");
            runRootCommand("start adbd");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static boolean isProcessRunning(String processName)
            throws Exception {
        // Log.d(MSG_TAG, "isProcessRunning("+processName+")");
        boolean running = false;
        Process process = null;
        process = Runtime.getRuntime().exec("ps");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                process.getInputStream()));
        String line = null;
        while ((line = in.readLine()) != null) {
            if (line.contains(processName)) {
                running = true;
                break;
            }
        }
        in.close();
        process.waitFor();
        return running;
    }

    private static boolean hasRootPermission() {
        // Log.d(MSG_TAG, "hasRootPermission()");
        Process process = null;
        DataOutputStream os = null;
        boolean rooted = true;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
            if (process.exitValue() != 0) {
                rooted = false;
            }
        } catch (Exception e) {
            Log.d(MSG_TAG, "hasRootPermission error: " + e.getMessage());
            rooted = false;
        } finally {
            if (os != null) {
                try {
                    os.close();
                    process.destroy();
                } catch (Exception e) {
                    // nothing
                }
            }
        }
        return rooted;
    }

    private static boolean runRootCommand(String command) {
        // Log.d(MSG_TAG, "runRootCommand("+command+")");
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            Log.d(MSG_TAG,
                    "Unexpected error - Here is what I know: " + e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                // nothing
            }
        }
        return true;
    }

    private static boolean setProp(String property, String value) {
        // Log.d(MSG_TAG, "setProp("+property+","+value+")");
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("setprop " + property + " " + value + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }

    public static String getWifiIp(Context context) {

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int ip = wifiManager.getConnectionInfo().getIpAddress();
        return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "."
                + ((ip >> 16) & 0xFF) + "." + ((ip >> 24) & 0xFF);
    }

}
