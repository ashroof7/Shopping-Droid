package com.example.shd1;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class DB_test extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_db_test);
		String s = (String)this.getIntent().getStringExtra("stores");
		((TextView)findViewById(R.id.textView3)).setText(s);
		
	}

	

}
