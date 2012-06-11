package com.target;

import java.io.File;

import android.os.Bundle;
import android.util.Log;

import org.apache.cordova.*;;


public class PhoneGapActivity extends DroidGap {
	
	String extStorageDirectory;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.loadUrl("file:///sdcard/index.html");
    }

    
    protected void onPause() {	
		super.onPause();	
		 File file = new File(extStorageDirectory,"config"+".xml");
		boolean deleted = file.delete();
		File file2 = new File(extStorageDirectory,"index"+".html");
		boolean deleted_html = file2.delete();
		Log.d("onphonegapPause","deleted"+ deleted_html);
	}
}