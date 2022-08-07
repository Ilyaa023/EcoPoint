package eco.point.data.firebase.googleAuth

import android.app.Activity
import android.util.Log
import androidx.activity.result.ActivityResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import eco.point.domain.IGoogleAuth
import eco.point.domain.IGoogleAuthCallback

class GoogleSignInImpl(
    private val activity: Activity,
    ): IGoogleAuth {
    companion object {
        val COMPLETE = 0
        val CANCEL = 1
        val FAIL = 2
    }
    private val TAG = javaClass.name
    private val auth = FirebaseAuth.getInstance()
    private lateinit var result: ActivityResult
    private var gso: GoogleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(activity.getString(com.firebase.ui.auth.R.string.default_web_client_id))
        .requestEmail().build()
    private var googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(activity, gso)
    var googleSignInIntent = googleSignInClient.signInIntent
        private set
    var UID = auth.currentUser?.uid
        private set

    fun initResult (result: ActivityResult){
        this.result = result
    }

    override fun signIn(callback: IGoogleAuthCallback) {
        try {
            val account = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                .getResult(ApiException::class.java)
            val credential: AuthCredential =
                GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener(activity){
                if (it.isSuccessful)
                    callback.onAuth(COMPLETE)
                else
                    callback.onAuth(FAIL)
            }.addOnCanceledListener(activity){
                callback.onAuth(CANCEL)
            }.addOnFailureListener(activity){
                callback.onAuth(FAIL)
            }
        } catch (e: ApiException){
            Log.e(TAG, "handleSignInResult: ${e.message}")
        }

    }

    override fun reload() {
        auth.currentUser?.reload()?.addOnCompleteListener {
            if(it.isSuccessful)
                Log.i(TAG, "reload: successful")
            else
                Log.i(TAG, "reload: failed")
        }
    }

    override fun signOut() {
        auth.signOut()
        googleSignInClient.signOut()
    }
}