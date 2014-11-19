package com.borqs.ivi_collect.update;

import java.io.File;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.RemoteException;
import android.util.Log;

public class ApkManagerService extends IntentService {


	private static final String TAG = "ApkManageService";
	private static final String FLAG = "flag";
	private static final String INSTALL="install";
	private static final String UNINSTALL="uninstall";
	private static final String FILE_PATH="/mnt/sdcard/IVI_collect11.apk";
	private static final String PACKAGE_NAME="IVI_collect11.apk";

	public ApkManagerService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
//		String flag = intent.getStringExtra(FLAG);
//		if(INSTALL.equals(flag)){
//			String filePath = intent.getStringExtra(FILE_PATH);
//			String packageName =intent.getStringExtra(PACKAGE_NAME);
//			Log.d(TAG, "filePath : "+filePath);
//			autoInstallApk(getApplicationContext(), filePath, packageName);
//		}else if(UNINSTALL.equals(flag)){
//			String packageName =intent.getStringExtra(PACKAGE_NAME);
//			Log.d(TAG,"uninstall packageName :��"+ packageName);
//			uninstallApkDefaul(getApplicationContext(), packageName);
//		}else{
//			return;
//		}
		Log.d(TAG,"------FILE_PATH-------->>"+FILE_PATH);
		Log.d(TAG,"------PACKAGE_NAME-------->>"+PACKAGE_NAME);
		autoInstallApk(getApplicationContext(), FILE_PATH, PACKAGE_NAME);
	}
	
	/** 
	 * Silent installation
	 * */ 
	public static void autoInstallApk(Context context, String filePath, 
			String packageName) { 
		Log.d(TAG, "autoInstallApk :" + packageName + ",filePath:" + filePath); 
		File file = new File(filePath);
		int installFlags = 0; 
		if (!file.exists()) {
			Log.d(TAG, "file not is exists!");
			return; 
		}
		installFlags |= PackageManager.INSTALL_REPLACE_EXISTING; 
		if (hasSdcard()) { 
			installFlags |= PackageManager.INSTALL_EXTERNAL; 
		}
		PackageManager pm = context.getPackageManager(); 
		try { 
			IPackageInstallObserver observer = new MyPakcageInstallObserver(); 
			Log.i(TAG, "########installFlags:" + installFlags+",packagename:"+packageName); 
			pm.installPackage(Uri.fromFile(file), observer, installFlags, 
					packageName); 
		} catch (Exception e) { 
			Log.e(TAG, "Exception by autoInstallApk . ",e);
		} 
	} 

	/**
	 *  Silent installation callback
	 *
	 */
	private static class MyPakcageInstallObserver extends IPackageInstallObserver.Stub { 

		@Override 
		public void packageInstalled(String packageName, int returnCode) { 
			Log.i(TAG,"install returnCode = " + returnCode);// return 1 : install success!
		} 
	}


	/**
	 * Silent discharge
	 * @param context
	 * @param packageName
	 */
	public static void uninstallApkDefaul(Context context,String packageName) {
		PackageManager pm = context.getPackageManager(); 
		try {
			IPackageDeleteObserver observer = new MyPackageDeleteObserver(); 
			pm.deletePackage(packageName, observer, 0);
		} catch (Exception e) {
			Log.e(TAG, "Exception by uninstallApkDefaul . ",e);
		} 
	} 

	/**
	 * Silent discharge callback 
	 * */ 
	private static class MyPackageDeleteObserver extends IPackageDeleteObserver.Stub { 
		@Override
		public void packageDeleted(boolean succeeded)
				throws RemoteException {
			Log.d(TAG, "uninstall returnCode = " + succeeded);//return true,uninstall success!  
		} 
	}



	public static boolean hasSdcard() { 
		String status = Environment.getExternalStorageState(); 
		if (status.equals(Environment.MEDIA_MOUNTED) || status.equals("/mnt/sdcard")) { 
			Log.i(TAG, "has sdcard...."); 
			return true; 
		} else { 
			return false; 
		} 
	}

}
