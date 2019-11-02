package com.github.zftpserver;

/**
 * Created by wzz on 2019/11/02.
 * kgd.zhen@gmail.com
 */
public interface FtpListener {
    /**
     *  打开 ftp
     * @param type  0 打开成功   1 打开失败
     * @param message  打开失败原因
     */
    public void openFtp(int type, String message);

    /**
     *  ftp已关闭
     */
    public void closeFtp();

    /**
     *  ftp用户登录
     * @param type    0，登录成功，  1登录失败
     * @param message  登录失败原因
     */
    public void login(int type, String message);

    /**
     *  ftp上传文件成功
     * @param file  文件路径
     *               如： /storage/emulated/legacy/EBSB_SDCardLog/ebsb.apk
     */
    public void addFile(String file);

    /**
     *  删除文件成功
     * @param file
     */
    public void delFile(String file);

    //打印日志
    public void log(String log);
}
