package com.example.sys_serv_check;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private Button checkButton;
	private Button hupButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);

		checkButton = (Button) findViewById(R.id.check_button);
		checkButton.setOnClickListener(this);

		hupButton = (Button) findViewById(R.id.hup_button);
		hupButton.setOnClickListener(this);
		
	}

	private String execCmd(String commandLine) {
		
		try {
		
			Process process = Runtime.getRuntime().exec(commandLine);
			InputStreamReader reader = new InputStreamReader(process.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(reader);
			int numRead;
			char[] buffer = new char[5000];
			StringBuffer commandOutput = new StringBuffer();
			while ((numRead = bufferedReader.read(buffer)) > 0) {
				commandOutput.append(buffer, 0, numRead);
			}
			bufferedReader.close();
			process.waitFor();

			return commandOutput.toString();

		} catch (IOException e) {
	    	throw new RuntimeException(e);
		} catch (InterruptedException e) {
	    	throw new RuntimeException(e);
		}
	}
	
	private String check() throws IOException {
		return execCmd("/system/bin/ls /system/bin");
	}
	
	private String hup(){
		return execCmd("/system/bin/ls /system/xbin");
	}

	public void onClick(View v) {
		
		if (v == checkButton || v == hupButton) {
			
			Integer toggle = (v == checkButton) ? 1 : 0;
			String output = "";
			
			try { // maybe IOException from check() and hup()
				output = toggle == 1 ?  check() : hup();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// popup
//			Toast.makeText(this, output, Toast.LENGTH_LONG).show();
			
			// text field
			TextView greetingField = (TextView) findViewById(R.id.output_field);
			greetingField.setText(output);
		}
	}

}