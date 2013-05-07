package com.example.shd1;


import com.example.shd1.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class TestMapsActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_maps);
		View launch = findViewById(R.id.launch); 
        launch.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch(v.getId()){
		case R.id.launch :{
			Intent i = new Intent(this, MapsActivity.class);
			startActivity(i);
			break;
		}
		}
		
	}

}
