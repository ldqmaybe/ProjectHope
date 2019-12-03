package com.one.greendaohope.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
	private static final String TAG = "CrashHandler";
	private Context mContext;
	private static CrashHandler mInstance;
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	// 用来存储设备信息和异常信息
	private Map<String, String> mInfo = new HashMap<>();
	private DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private CrashHandler() {
	}

	public static CrashHandler getInstance() {
		if (mInstance == null) {
			synchronized (CrashHandler.class) {
				if (mInstance == null) {
					mInstance = new CrashHandler();
				}
			}
		}
		return mInstance;
	}

	public void init(Context context) {
		mContext = context;
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		// 如果用户没有处理则让系统默认的异常处理器来处理
		if (!handleException(e) && mDefaultHandler != null) {
			Log.e(TAG,"error:"+e.getMessage(),e);
			mDefaultHandler.uncaughtException(t, e);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				Log.e(TAG, "error:"+e.getMessage(), e);
			}
			// 退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}

	// 自定义错误处理，收集错误信息，发送错误报告等操作均在此完成.
	private boolean handleException(Throwable e) {
		if (e == null) {
			return false;
		}
		Log.e(TAG,"error:"+e.getMessage());
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_SHORT)
						.show();
				Looper.loop();
			}
		}.start();
		// 收集设备参数信息
		collectErrorInfo();
		// 保存日志文件
		saveErrorInfo(e);
		return true;
	}

	// 收集设备参数信息
	private void collectErrorInfo() {
		PackageManager pm = mContext.getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = TextUtils.isEmpty(pi.versionName) ? "未设置版本号"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				mInfo.put("versionName", versionName);
				mInfo.put("versionCode", versionCode);
			}
			Field[] fields = Build.class.getFields();
			if (fields != null && fields.length > 0) {
				for (Field field : fields) {
					field.setAccessible(true);
					try {
						mInfo.put(field.getName(), field.get(null).toString());
					} catch (IllegalAccessException e) {
						Log.e(TAG, "an error occured when collect crash info",
								e);
					}
				}
			}
		} catch (PackageManager.NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
	}

	// 保存错误信息到文件中
	private void saveErrorInfo(Throwable e) {
		StringBuffer stringBuffer = new StringBuffer();
		for (Map.Entry<String, String> entry : mInfo.entrySet()) {
			String keyName = entry.getKey();
			String value = entry.getKey();
			stringBuffer.append(keyName + "=" + value + "\n");
		}
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		e.printStackTrace(printWriter);
		Throwable cause = e.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		Log.e(TAG, "开始：");
		System.out.println("crash:\n"+result);
		stringBuffer.append(result);
		long currentTime = System.currentTimeMillis();
		String time = mDateFormat.format(new Date());
		String fileName = "crash-" + time + "-" + currentTime + ".log";
		// 判断有没有SD卡
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File dir = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/shanghaicrash");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(dir + "/" + fileName);
				fos.write(stringBuffer.toString().getBytes());
			} catch (FileNotFoundException e1) {
				Log.e(TAG, "an error occured due to file not found", e);
			} catch (IOException e2) {
				Log.e(TAG, "an error occured while writing file...", e);
			} finally {
				try {
					fos.close();
				} catch (IOException e1) {
					Log.e(TAG, "an error occured when close file", e);
				}
			}
		}
	}
}
