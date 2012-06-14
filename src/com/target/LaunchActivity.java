package com.target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class LaunchActivity extends Activity {
	
	String extStorageDirectory;
	byte[] buf = new byte[1024];

	Cipher ecipher;
	Cipher dcipher;
    boolean flag =false;
   //String tgtFolder = "/Target_docs";//creating a dummmy directory for target in the sd.

	/** Called when the activity is first created. */
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File mkTarDir=new File(extStorageDirectory );
		mkTarDir.mkdir();
		
	}

	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
         flag =false;
         try {
        	 
        	 //initiate the key and 
 			startCipher();
 			//output files from cipher.
 			File file_out_html = new File(extStorageDirectory, "index.html");
 			File file_out_xml = new File(extStorageDirectory, "config.xml");

 			//input files to cipher.
 			InputStream is_html = getApplicationContext().getAssets().open("target_html.tgt");
 			InputStream is_xml = getApplicationContext().getAssets().open("target_config.tgt");

 			//passing the inputstream to decrypt function.
 			decrypt(is_html, new FileOutputStream(file_out_html));
 			decrypt(is_xml, new FileOutputStream(file_out_xml));

 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 		Toast.makeText(LaunchActivity.this,"Saving Decrypted File On Internal Storage", Toast.LENGTH_LONG).show();
 	}
	
	
	public void startCipher() {

		Log.d("TargetAesCipherActivity", "SecretKey Call");
		{
			byte[] iv = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
					0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };

			byte[] KEY = { 0, 42, 2, 54, 4, 45, 6, 7, 65, 9, 54, 11, 12, 13,
					60, 15 };

			SecretKeySpec keySpec = new SecretKeySpec(KEY, "AES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			try {
				ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				// CBC requires an initialization vector
				ecipher.init(Cipher.ENCRYPT_MODE, keySpec, paramSpec);
				dcipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void encrypt(InputStream in, OutputStream out) {
		try {

			// Bytes written to out will be encrypted
			out = new CipherOutputStream(out, ecipher);
			// Read in the cleartext bytes and write to out to encrypt
			int numRead = 0;
			while ((numRead = in.read(buf)) >= 0) {
				out.write(buf, 0, numRead);
			}
			out.close();
		} catch (java.io.IOException e) {
		}
	}

	public void decrypt(InputStream in, OutputStream out) {

		Log.d("TargetAesCipherActivity", "decryption");
		try {

			// Bytes read from in will be decrypted
			in = new CipherInputStream(in, dcipher);
			// Read in the decrypted bytes and write the cleartext to out
			int numRead = 0;
			while ((numRead = in.read(buf)) >= 0) {
				out.write(buf, 0, numRead);
			}
			out.close();
		} catch (java.io.IOException e) {
		}
	}

//Launching the phonegap Application on button push
	public void DroidLaunch(final View button){  	
		 
		 flag = true;
		 Intent i = new Intent(LaunchActivity.this, PhoneGapActivity.class);
			startActivity(i);
		    Log.d("DroidLaunch", "launch successfull"); 
		    }

	
	//deleting the decrypted file from device.
public void onExit(final View button){  	
		 
	File file = new File(extStorageDirectory,"config"+".xml");
		boolean deleted = file.delete();
		File file2 = new File(extStorageDirectory,"index"+".html");
		boolean deleted_html = file2.delete();
		Log.d("onStop","deleted"+ deleted_html);
		    }
	  
//deleting the decrypted file from device.
protected void onPause() {	
		super.onPause();	
		
		if(flag==false){
		File file = new File(extStorageDirectory,"config"+".xml");
		boolean deleted = file.delete();
		File file2 = new File(extStorageDirectory,"index"+".html");
		boolean deleted_html = file2.delete();
		Log.d("onPause","deleted"+ deleted_html);
		}
	}
 
//deleting the decrypted file from device.
		protected void onDestroy() {	
			super.onDestroy();	
	 File file = new File(extStorageDirectory,"config"+".xml");
		boolean deleted = file.delete();
			File file2 = new File(extStorageDirectory,"index"+".html");
			boolean deleted_html = file2.delete();
			Log.d("onStop","deleted"+ deleted_html);
		}
	 	 
}