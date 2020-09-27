package midoriiro.madfx.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity()
{
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		val intent = Intent(this, MainActivity::class.java)
		this.startActivity(intent)
		this.finish()
	}
}