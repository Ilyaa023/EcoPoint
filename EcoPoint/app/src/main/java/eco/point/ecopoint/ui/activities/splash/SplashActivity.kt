package eco.point.ecopoint.ui.activities.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import eco.point.data.firebase.database.updates.FBUpdatesRepository
import eco.point.domain.DataUseCases.auth.skiped.GetSkiped
import eco.point.domain.DataUseCases.auth.visited.GetVisited
import eco.point.ecopoint.R
import eco.point.data.storage.LocalUserRepository
import eco.point.domain.DataUseCases.updates.GetUpdates
import eco.point.ecopoint.ui.activities.UpdActivity
import eco.point.ecopoint.ui.activities.authorization.AuthorizationActivity
import eco.point.ecopoint.ui.activities.main.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val repository = LocalUserRepository(applicationContext)
        GetUpdates(FBUpdatesRepository()).execute {
            val version = this.packageManager.getPackageInfo(packageName, 0).versionName
            Log.e("TAG", "onCreate: $version", )
            if (it != version)
                startActivity(Intent(this, UpdActivity::class.java))
            else
                if (GetVisited(repository = repository).execute())
                    startActivity(Intent(this, MainActivity::class.java))
                else
                    if (GetSkiped(repository = repository).execute())
                        startActivity(Intent(this, MainActivity::class.java))
                    else
                        startActivity(Intent(this, AuthorizationActivity::class.java))
            finish()
        }
    }
}