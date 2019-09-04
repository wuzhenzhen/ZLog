package com.github.zbase.tools;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.github.library.ZLog;

public class FileUtil {

	public static final int UNZIP_PROGRESS = 10001;
	public static final int COPY_FOLDER = 10002;
	public static final int COPY_FILE = 10003;
	/**
	 * 解压缩功能. 将zipFile文件解压到folderPath目录下.
	 * 
	 */
	public static int upZipFile(File zipFile, String folderPath)
			throws ZipException, IOException {
		ZipFile zfile = new ZipFile(zipFile);
		Enumeration zList = zfile.entries();
		ZipEntry ze = null;
		byte[] buf = new byte[1024];
		while (zList.hasMoreElements()) {
			ze = (ZipEntry) zList.nextElement();
			if (ze.isDirectory()) {
				String dirstr = folderPath + ze.getName();
				// dirstr.trim();
				dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
				Log.d("upZipFile", "str = " + dirstr);
				File f = new File(dirstr);
				f.mkdir();
				continue;
			}
			Log.d("upZipFile", "ze.getName() = " + ze.getName());
			OutputStream os = new BufferedOutputStream(new FileOutputStream(
					getRealFileName(folderPath, ze.getName())));
			InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
			int readLen = 0;
			while ((readLen = is.read(buf, 0, 1024)) != -1) {
				os.write(buf, 0, readLen);
			}
			is.close();
			os.close();
		}
		zfile.close();
		ZLog.iii("upZipFile:"+zipFile.getPath()+"->"+folderPath);
		return 0;
	}

	/**
	 * 给定根目录，返回一个相对路径所对应的实际文件名.
	 * 
	 * @param baseDir
	 *            指定根目录
	 * @param absFileName
	 *            相对路径名，来自于ZipEntry中的name
	 * @return java.io.File 实际的文件
	 */
	public static File getRealFileName(String baseDir, String absFileName) {
		String[] dirs = absFileName.split("/");
		File ret = new File(baseDir);
		String substr = null;
		if (dirs.length > 1) {
			for (int i = 0; i < dirs.length - 1; i++) {
				substr = dirs[i];
				try {
					// substr.trim();
					substr = new String(substr.getBytes("8859_1"), "GB2312");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				ret = new File(ret, substr);
			}
			if (!ret.exists())
				ret.mkdirs();
			substr = dirs[dirs.length - 1];
			try {
				substr = new String(substr.getBytes("8859_1"), "GB2312");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			ret = new File(ret, substr);
		} else if (dirs.length == 1) {
			if (!ret.exists())
				ret.mkdirs();
			substr = dirs[0];
			try {
				substr = new String(substr.getBytes("8859_1"), "GB2312");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			ret = new File(ret, substr);
		}
		Log.d("getRealFileName: ",
				"dirs.length=" + dirs.length + ";" + ret.getAbsolutePath());
		return ret;
	}

	/**
	 *  复制当前文件夹下的内容， 不处理子文件夹
	 * @param oldPath  文件夹名称
	 * @param newPath  文件夹名称
	 * @return
	 */
	public static boolean copyFolder(String oldPath, String newPath, Handler handler){
		FileInputStream input = null;
		String outPath = null;
		FileOutputStream output = null;
		Message msg = null;
		Bundle bun = null;
		int count = 0;
		ZLog.iii("copyFolder old path is " + oldPath + " new path is " + newPath);
		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {

				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					input = new FileInputStream(temp);
					outPath = newPath + "/" + temp.getName().toString();
					File outPutFile = new File(outPath);
					if (!outPutFile.exists()) {
						outPutFile.createNewFile();
					}
					output = new FileOutputStream(outPath);
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
					count++;
				}
				ZLog.iii("copy file success=" + i+"="+temp.getPath());
				msg = handler.obtainMessage();
				msg.what = COPY_FOLDER;
				bun = new Bundle();
				bun.putInt("count", count);
				bun.putBoolean("flag", false);
				msg.setData(bun);
				handler.sendMessage(msg);
			}
			msg = handler.obtainMessage();
			msg.what = COPY_FOLDER;
			bun = new Bundle();
			bun.putInt("count", count);
			bun.putBoolean("flag", true);
			msg.setData(bun);
			handler.sendMessage(msg);
			return true;
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			ZLog.eee("copy file Exception : " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 *
	 * @param oldPath  文件夹路径如 /mnt/extsd/video_image_dir
	 * @param newPath  文件夹路径如 /mnt/sdcard/video_image_dir
	 * @return
	 */
	public static boolean copyFolderAndSubFolder(String oldPath, String newPath){
		File file = new File(oldPath);

		String[] filePath = file.list();	//文件名称列表

		if (!(new File(newPath)).exists()) {
			(new File(newPath)).mkdir();
		}

		for (int i = 0; i < filePath.length; i++) {
			if ((new File(oldPath + file.separator + filePath[i])).isDirectory()) {
				copyFolderAndSubFolder(oldPath  + file.separator  + filePath[i], newPath  + file.separator + filePath[i]);
			}

			if (new File(oldPath  + file.separator + filePath[i]).isFile()) {
				copyFile(oldPath + file.separator + filePath[i], newPath + file.separator + filePath[i]);
			}

		}
		return true;
	}

	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/folder
	 * @param newPath
	 *            String 复制后路径 如：f:/folder
	 * @return boolean
	 */
	public static boolean copyFolder(String oldPath, String newPath) {
		FileInputStream input = null;
		String outPath = null;
		FileOutputStream output = null;
		ZLog.iii("copyFolder old path is " + oldPath + " new path is " + newPath);
		try {

			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				Log.e("copy file", "success " + i);
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					input = new FileInputStream(temp);
					outPath = newPath + "/" + temp.getName().toString();
					File outPutFile = new File(outPath);
					if (!outPutFile.exists()) {
						outPutFile.createNewFile();
					}
					output = new FileOutputStream(outPath);
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}

				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
			return true;
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			ZLog.eee("copy file Exception : " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 *  复制单个文件内容
	 * @param oldPath  String 原文件路径含文件名 如：c:/folder/1.xml
	 * @param newPath  String 复制后路径 如：f:/folder
	 * @return boolean
	 */
	public static boolean copyFile(String oldPath, String newPath){
		try{
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹

			File oldFile = new File(oldPath);
			if(oldFile.exists() && oldFile.isFile()){
				FileInputStream input = new FileInputStream(oldFile);
				String outPath = newPath + "/" + oldFile.getName().toString();
				File outPutFile = new File(outPath);
				if(!outPutFile.exists()) {
					outPutFile.createNewFile();
				}
				FileOutputStream output = new FileOutputStream(outPath);
				byte[] b = new byte[1024 * 5];
				int len;
				while ( (len = input.read(b)) != -1) {
					output.write(b, 0, len);
				}
				output.flush();
				output.close();
				input.close();
			}
			return true;
		}catch (Exception e){
			System.out.println("复制文件内容操作出错");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 *  复制单个文件内容
	 * @param oldPath  String 原文件路径含文件名 如：c:/folder/1.xml
	 * @param newPath  String 复制后路径 如：f:/folder
	 * @return boolean
	 */
	public static boolean copyFile(String oldPath, String newPath, Handler handler){
		try{
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹

			File oldFile = new File(oldPath);
			if(oldFile.exists() && oldFile.isFile()){
				FileInputStream input = new FileInputStream(oldFile);
				String outPath = newPath + "/" + oldFile.getName().toString();
				File outPutFile = new File(outPath);
				if(!outPutFile.exists()) {
					outPutFile.createNewFile();
				}
				FileOutputStream output = new FileOutputStream(outPath);
				byte[] b = new byte[1024 * 5];
				int len;
				while ( (len = input.read(b)) != -1) {
					output.write(b, 0, len);
				}
				output.flush();
				output.close();
				input.close();

				Message msg = handler.obtainMessage();
				msg.what = COPY_FILE;
				Bundle bun = new Bundle();
				bun.putString("oldPath", oldPath);
				bun.putString("newPath", newPath);
				msg.setData(bun);
				handler.sendMessage(msg);
			}
			return true;
		}catch (Exception e){
			System.out.println("复制文件内容操作出错");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/file.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/file.txt
	 * @return boolean
	 */
	public static boolean copyFileWithPrBar(String oldPath, String newPath,
			Handler handler) {
		boolean isok = true;
		int progress = 0, tempProgress = 0;
		Message msg = null;
		Bundle bun = null;
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				// 检查目的地路径是否存在，不存在则创建
				File dirFile = new File(newPath.substring(0,
						newPath.lastIndexOf("/")));
				if (!dirFile.exists()) {
					dirFile.mkdirs();
				}
				File newPathFile = new File(newPath);
				if (!newPathFile.exists()) {
					newPathFile.createNewFile();
				}
				long length = oldfile.length();
				long count = 0;
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread;
					fs.write(buffer, 0, byteread);
					// 发送复制进度
					if (handler != null) {
						tempProgress = (int) (((float) bytesum / length) * 100);
						if (tempProgress > progress) {
							progress = tempProgress;
							msg = handler.obtainMessage();
							msg.what = UNZIP_PROGRESS;
							bun = new Bundle();
							bun.putInt("UNZIP_PROGRESS", progress);
							msg.setData(bun);
							handler.sendMessage(msg);
						}
					}
				}
				fs.flush();
				fs.close();
				inStream.close();
			} else {
				isok = false;
			}
		} catch (Exception e) {
			isok = false;
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
		}
		return isok;
	}

	/**
	 * 删除文件或文件夹下所有文件
	 * 
	 * @param file
	 */
	public static void delete(File file) {
		if (file.isDirectory()) {
			Log.e("delete", "delete " + file.getAbsolutePath());
		}

		if (!file.exists()) {
			return;
		}
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
			}
			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i]);
			}
			file.delete();
		}
	}

	/**
	 * 根据 修改时间排序 由新到旧
	 * 
	 * @param addr
	 * @return
	 */
	public static File[] getListFiles(String addr) {
		File Sfile = new File(addr);
		if (!Sfile.exists())
			return null;
		File[] fs = Sfile.listFiles();

		Arrays.sort(fs, new Comparator<File>() { // 根据 修改时间排序 由新到旧
					public int compare(File f1, File f2) {
						long diff = f1.lastModified() - f2.lastModified();
						if (diff > 0)
							return 1;
						else if (diff == 0)
							return 0;
						else
							return -1;
					}

					public boolean equals(Object obj) {
						return true;
					}
				});
		return fs;
	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * 
	 * @param dir
	 *            将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful. If a
	 *         deletion fails, the method stops attempting to delete and returns
	 *         "false".
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

	/**
	 * 判断文件夹是否存在，文件夹是否有文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean isDirHaveFile(String filePath) {
		File file = new File(filePath);
		// 没有文件夹直接返回
		if (!file.exists()) {
			return false;
		}
		// 有文件夹无文件直接返回
		if (file.listFiles().length <= 0) {
			return false;
		}
		return true;
	}
}
