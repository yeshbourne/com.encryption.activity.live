package com.target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class LaunchActivity extends Activity {
	
	    boolean flag =false;
	    String extStorageDirectory;
	    //String tgtFolder = "/Target_docs";

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
			String key = "__@#$%%$^&^targ!!!@#$#%$%^$%etteam123$%^&^mem$%$^$^$$%6^$%^$%^$^$^$%^$%^$^4^%$^$ber^&^*^&*^crypt*%$^$^$%^$^$logic@#$@!$!@!$@!$!$1"; // needs to be at least 8 characters for DES


			//tmp output the file to guest sd/internal memory.
			
			
			
			File file_out_config = new File(extStorageDirectory,"config.xml");
			File file_out_html = new File(extStorageDirectory,"index.html");
            
            //input the encrypted password file to decrypter 
			InputStream is_config =getApplicationContext().getAssets().open("target_config.tgt");
			InputStream is_html =getApplicationContext().getAssets().open("target_html.tgt");

			
			//donot encrypt the file back becoz write is not allowed on assest folder.but we can encrypt in the sd card!!!

            decrypt(key, is_config, new FileOutputStream(file_out_config));
            decrypt(key, is_html, new FileOutputStream(file_out_html));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	    
		Toast.makeText(LaunchActivity.this, "Saving Decrypted File On Internal Storage", Toast.LENGTH_LONG).show();    

	}
	
	//function to encrypt currently used to encrypt target file outside the application .
	
	public static void encrypt(String key, InputStream is, OutputStream os) throws Throwable {
		encryptOrDecrypt(key, Cipher.ENCRYPT_MODE, is, os);
	}
	//function to decrypt currently used to decrypt target file as the app launches!!!
	public static void decrypt(String key, InputStream is, OutputStream os) throws Throwable {
		encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os);
	}

	//handling encryption and decryption.
	
	public static void encryptOrDecrypt(String key, int mode, InputStream is, OutputStream os) throws Throwable {

		DESKeySpec dks = new DESKeySpec(key.getBytes());
		SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
		SecretKey desKey = skf.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for SunJCE

		if (mode == Cipher.ENCRYPT_MODE) {
			cipher.init(Cipher.ENCRYPT_MODE, desKey);
			CipherInputStream cis = new CipherInputStream(is, cipher);
			doCopy(cis, os);
		} else if (mode == Cipher.DECRYPT_MODE) {
			cipher.init(Cipher.DECRYPT_MODE, desKey);
			CipherOutputStream cos = new CipherOutputStream(os, cipher);
			doCopy(is, cos);
		}
	}

	public static void doCopy(InputStream is, OutputStream os) throws IOException {
		byte[] bytes = new byte[64];
		int numBytes;
		while ((numBytes = is.read(bytes)) != -1) {
			os.write(bytes, 0, numBytes);
		}
		os.flush();
		os.close();
		is.close();
		
		
	}
	
	 public void DroidLaunch(final View button){  	
		 
		 flag = true;
		 Intent i = new Intent(LaunchActivity.this, PhoneGapActivity.class);
			startActivity(i);
		    Log.d("DroidLaunch", "launch successfull"); 
		    }
	 
 public void onExit(final View button){  	
		 
	 File file = new File(extStorageDirectory,"config"+".xml");
		boolean deleted = file.delete();
		File file2 = new File(extStorageDirectory,"index"+".html");
		boolean deleted_html = file2.delete();
		Log.d("onStop","deleted"+ deleted_html);
		    }
	  
 
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
 
 
		protected void onDestroy() {	
			super.onDestroy();	
			 File file = new File(extStorageDirectory,"config"+".xml");
			boolean deleted = file.delete();
			File file2 = new File(extStorageDirectory,"index"+".html");
			boolean deleted_html = file2.delete();
			Log.d("onStop","deleted"+ deleted_html);
		}
	 	 
}