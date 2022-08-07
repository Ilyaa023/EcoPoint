package eco.point.ecopoint.ui.activities.authorization

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import eco.point.domain.IGoogleAuthCallback
import eco.point.domain.models.User
import eco.point.ecopoint.R
import eco.point.data.firebase.googleAuth.GoogleSignInImpl
import eco.point.domain.IFBCallback
import eco.point.ecopoint.databinding.ActivityAuthorizationBinding
import eco.point.ecopoint.models.ActivityResultModel
import eco.point.ecopoint.ui.activities.main.MainActivity
import java.util.*

class AuthorizationActivity : AppCompatActivity(), IGoogleAuthCallback {
    private lateinit var vm: AuthorizationViewModel
    private lateinit var binding: ActivityAuthorizationBinding
    private lateinit var loginResultHandler: ActivityResultLauncher<Intent>
    private lateinit var Cities: Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Cities = resources.getStringArray(R.array.Cities)
        vm = ViewModelProvider(
            this,
            AuthorizationViewModelFactory(this, Cities)
        )[AuthorizationViewModel::class.java]
        initUI()
        loginResultHandler = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                vm.signIn(
                    resultModel = ActivityResultModel(
                        activity = this@AuthorizationActivity,
                        result = it
                    ),
                    callback = this@AuthorizationActivity
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            vm.reload()
        }catch(e: Exception){}
    }

    override fun onAuth(resultCode: Int) {
        showShortToast(vm.getToast(resultCode))
        if(resultCode == GoogleSignInImpl.COMPLETE){
            vm.sendBaseData()
            val user1 = vm.getAllLocalUserData()
            var user2 = User()
            vm.getAllCloudUserData(object : IFBCallback<User> {
                override fun onReceive(data: User) {
                    user2 = data
                    if (vm.compareUserData(user1, user2))
                        next(false)
                    else
                        if (user2.level == -1 || user2.bonuses == -1 || user2.garbageCounter == -1) {
                            vm.sendOtherData(user1)
                            next(false)
                        }
                        else
                            makeAlertDialog(user1, user2)
                }
            })
        }
    }

    private fun makeAlertDialog(user1: User, user2: User){
        val dialog = AlertDialog.Builder(this).create()
        dialog.show()
        dialog.setContentView(R.layout.layout_for_choose_data)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val localBtn = dialog.findViewById<LinearLayout>(R.id.alert_continue_with_local)
        val cloudBtn = dialog.findViewById<LinearLayout>(R.id.alert_continue_with_cloud)
        val localLvl = dialog.findViewById<TextView>(R.id.local_lvl)
        val cloudLvl = dialog.findViewById<TextView>(R.id.cloud_lvl)
        val localGC = dialog.findViewById<TextView>(R.id.local_handovers)
        val cloudGC = dialog.findViewById<TextView>(R.id.cloud_handovers)
        val localBonuses = dialog.findViewById<TextView>(R.id.local_bonuses)
        val cloudBonuses = dialog.findViewById<TextView>(R.id.cloud_bonuses)
        localLvl?.text = user1.level.toString()
        localGC?.text = user1.garbageCounter.toString()
        localBonuses?.text = user1.bonuses.toString()
        cloudLvl?.text = user2.level.toString()
        cloudGC?.text = user2.garbageCounter.toString()
        cloudBonuses?.text = user2.bonuses.toString()
        cloudBtn?.setOnClickListener{
            vm.setBonuses(user1.bonuses)
            vm.setLevel(user1.level)
            vm.setGarbageCounter(user1.garbageCounter)
            next(false)
        }
        localBtn?.setOnClickListener {
            vm.sendOtherData(user1)
            next(false)
        }
    }
    private fun showShortToast(text: String){
        Toast.makeText(
            this@AuthorizationActivity,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }
    private fun initUI() = with(binding) {
        vm.userDate.observe(this@AuthorizationActivity){
            calendarView.updateDate(
                it.get(Calendar.YEAR),
                it.get(Calendar.MONTH),
                it.get(Calendar.DAY_OF_MONTH)
            )
        }
        ZeroSkill.isChecked = true
        changeRButtons()
        ZeroSkill.setOnClickListener {
            changeRButtons()
            vm.checkedRB.value = 0
        }
        MediumSkill.setOnClickListener {
            changeRButtons()
            vm.checkedRB.value = 1
        }
        LargeSkill.setOnClickListener {
            changeRButtons()
            vm.checkedRB.value = 2
        }
        PersonCity.setAdapter(
            ArrayAdapter<String>(
                this@AuthorizationActivity,
                android.R.layout.simple_dropdown_item_1line,
                Cities
            )
        )
        vm.city.observe(this@AuthorizationActivity){
            PersonCity.setText(it)
        }
        vm.name.observe(this@AuthorizationActivity){
            PersonName.setText(it)
        }
        vm.correctName.observe(this@AuthorizationActivity){
            nameError.visibility = if (it) View.GONE else View.VISIBLE
        }
        vm.correctCity.observe(this@AuthorizationActivity){
            cityError.visibility = if (it) View.GONE else View.VISIBLE
        }
        vm.correctDate.observe(this@AuthorizationActivity){
            dateError.visibility = if (it) View.GONE else View.VISIBLE
        }
        val textWithLink = "<a href=\"https://www.st.com/resource/en/reference_manual/dm00031020-stm32f405415-stm32f407417-stm32f427437-and-stm32f429439-advanced-armbased-32bit-mcus-stmicroelectronics.pdf\">text for link</a>"
        linkToDocs.setText(
            Html.fromHtml(textWithLink, Html.FROM_HTML_MODE_LEGACY)
        )
        linkToDocs.linksClickable = true
        linkToDocs.movementMethod = LinkMovementMethod.getInstance()

        backButton.setOnClickListener {
            if (firstLayout.visibility == View.VISIBLE){
                backButton.visibility = View.GONE
                skipButton.visibility = View.GONE
                helloLayout.visibility = View.VISIBLE
            }
            if (secondLayout.visibility == View.VISIBLE){
                backButton.visibility = View.GONE
                secondLayout.visibility = View.GONE
                skipButton.visibility = View.GONE
                firstLayout.visibility = View.VISIBLE
                helloLayout.visibility = View.VISIBLE
            }
        }
        continueButt.setOnClickListener{
            this@AuthorizationActivity.currentFocus?.let { view ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
            vm.checkName(PersonName.text.toString())
            vm.checkCity(PersonCity.text.toString())
            vm.checkDate(GregorianCalendar(
                calendarView.year,
                calendarView.month,
                calendarView.dayOfMonth
            ))
            if (
                vm.correctCity.value!!
                && vm.correctName.value!!
                && vm.correctDate.value!!
            ){
                vm.setStatus()
                backButton.visibility = View.VISIBLE
                secondLayout.visibility = View.VISIBLE
                skipButton.visibility = View.VISIBLE
                firstLayout.visibility = View.GONE
                helloLayout.visibility = View.GONE
                googleSigninButton.setOnClickListener {
                    vm.setSkiped(false)
                    loginResultHandler.launch(vm.googleSignInImpl.googleSignInIntent)
                }
            }
        }
        skipButton.setOnClickListener {
            next(true)
        }
    }
    private fun changeRButtons() = with(binding){
        if (ZeroSkill.isChecked){
            ZeroSkill.setText(R.string.auth_rbutt_newman_full)
            ZeroSkill.setTextColor(getColor(R.color.black))
        } else{
            ZeroSkill.setText(R.string.auth_rbutt_newman)
            ZeroSkill.setTextColor(getColor(R.color.light_green))
        }
        if (MediumSkill.isChecked){
            MediumSkill.setText(R.string.auth_rbutt_adultman_full)
            MediumSkill.setTextColor(getColor(R.color.black))
        } else{
            MediumSkill.setText(R.string.auth_rbutt_adultman)
            MediumSkill.setTextColor(getColor(R.color.light_green))
        }
        if (LargeSkill.isChecked){
            LargeSkill.setText(R.string.auth_rbutt_proman_full)
            LargeSkill.setTextColor(getColor(R.color.black))
        } else{
            LargeSkill.setText(R.string.auth_rbutt_proman)
            LargeSkill.setTextColor(getColor(R.color.light_green))
        }
    }
    private fun next(skiped: Boolean) {
        vm.setSkiped(state = skiped)
        vm.setVisited(state = true)
        startActivity(Intent(this@AuthorizationActivity, MainActivity::class.java))
        finish()
    }
}

/*
    override fun onResult(result: Int) {
        if (result == FBAuthConnect.COMPLETE){
            fbConnect.getData(DataType.USERS, object: IFBConnect{
                override fun onReceive(dataSnapshot: DataSnapshot) {
                    val snapshot = dataSnapshot.child(fbAuthConnect.getUID()!!)
                    var lvlStr = "0"
                    var bonusStr = "0"
                    var garbageStr = "0"
                    try {
                        lvlStr = snapshot.child(FBUserKeys.LEVEL.data).value.toString()
                        if (lvlStr == "null")
                            lvlStr = "0"
                    }catch (e: Exception){}
                    try {
                        bonusStr = snapshot.child(FBUserKeys.BONUSES.data).value.toString()
                        if (bonusStr == "null")
                            bonusStr = "0"
                    }catch (e: Exception){}
                    try {
                        garbageStr = snapshot.child(FBUserKeys.GARBAGE_COUNTER.data).value.toString()
                        if (garbageStr == "null")
                            garbageStr = "0"
                    }catch (e: Exception){}
                    var user1 = User(user.id, user.name, user.city, user.birth, bonusStr.toInt(), garbageStr.toInt(), lvlStr.toInt())
                    if (!(snapshot.hasChild(FBUserKeys.LEVEL.data) &&
                                snapshot.hasChild(FBUserKeys.BONUSES.data) &&
                                snapshot.hasChild(FBUserKeys.GARBAGE_COUNTER.data))){
                        fbConnect.SetUserData(user, FBUserKeys.LEVEL)
                        fbConnect.SetUserData(user, FBUserKeys.BONUSES)
                        fbConnect.SetUserData(user, FBUserKeys.GARBAGE_COUNTER)
                        next()
                    } else
                        try {
                            makeAlertDialog(user, user1)
                        }catch (e: Exception) {}
                }
            })
        }
    }

    private fun makeAlertDialog(user: User, user1: User){
        val dialog = AlertDialog.Builder(this).create()
        dialog.show()
        dialog.setContentView(R.layout.layout_for_choose_data)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val localBtn = dialog.findViewById<Button>(R.id.alert_continue_with_local)
        val cloudBtn = dialog.findViewById<Button>(R.id.alert_continue_with_cloud)
        val localData = dialog.findViewById<TextView>(R.id.alert_local_data)
        val cloudData = dialog.findViewById<TextView>(R.id.alert_cloud_data)
        localData?.text = "Данные в памяти устройства:\nУровень = ${user.level}\nКоличество сдач = ${user.garbageCounter}\nБонусы = ${user.bonuses}"
        cloudData?.text = "Данные в облачном хранилище:\nУровень = ${user1.level}\nКоличество сдач = ${user1.garbageCounter}\nБонусы = ${user1.bonuses}"
        localBtn?.setOnClickListener {
            fbConnect.SetUserData(user, FBUserKeys.LEVEL)
            fbConnect.SetUserData(user, FBUserKeys.BONUSES)
            fbConnect.SetUserData(user, FBUserKeys.GARBAGE_COUNTER)
            next()
            dialog.dismiss()
        }
        cloudBtn?.setOnClickListener {
            StorageTool.SetBonuses(user1.bonuses)
            StorageTool.SetLevel(user1.level)
            StorageTool.SetHandOverCount(user1.garbageCounter)
            next()
            dialog.dismiss()
        }
    }

    /*private fun checkEmailPwd(): Boolean {
        if (binding.emailField.text.toString().matches(Regex("(.+)"))) {
            binding.errorEmail.visibility = View.VISIBLE
            return false
        }
        if (!binding.passwordField.text.toString().matches(Regex("(.{6,30})"))) {
            binding.errorEmail.visibility = View.VISIBLE
            return false
        }
        binding.errorEmail.visibility = View.GONE
        return true
    }*/
}*/