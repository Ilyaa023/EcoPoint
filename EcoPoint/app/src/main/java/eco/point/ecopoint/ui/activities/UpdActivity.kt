package eco.point.ecopoint.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import eco.point.data.storage.LocalUserRepository
import eco.point.domain.DataUseCases.auth.skiped.GetSkiped
import eco.point.domain.DataUseCases.auth.visited.GetVisited
import eco.point.ecopoint.R
import eco.point.ecopoint.ui.activities.authorization.AuthorizationActivity
import eco.point.ecopoint.ui.activities.main.MainActivity

class UpdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upd)
        findViewById<Button>(R.id.ver_skip_button).setOnClickListener {
            val repository = LocalUserRepository(applicationContext)
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