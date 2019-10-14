package com.example.radioswitchbutton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		rab_button.setOnSwitchListener(object :RadioAnimButtons.OnSwitchListener{
			override fun onSwitch(pos: Int) {
				Toast.makeText(this@MainActivity, "$pos", Toast.LENGTH_SHORT).show()
			}
		})
		
	}
}
