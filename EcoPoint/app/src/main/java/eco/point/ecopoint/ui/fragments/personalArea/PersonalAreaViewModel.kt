package eco.point.ecopoint.ui.fragments.personalArea

import android.graphics.Bitmap
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eco.point.data.firebase.database.user.FBUserRepository
import eco.point.data.storage.LocalUserRepository
import eco.point.domain.DataUseCases.auth.skiped.GetSkiped
import eco.point.domain.DataUseCases.user.bonuses.GetUserBonuses
import eco.point.domain.DataUseCases.user.bonuses.SetUserBonuses
import eco.point.domain.DataUseCases.user.garbageCounter.GetUserGarbageCounter
import eco.point.domain.DataUseCases.user.garbageCounter.SetUserGarbageCounter
import eco.point.domain.DataUseCases.user.level.GetUserLevel
import eco.point.domain.DataUseCases.user.level.SetUserLevel
import eco.point.domain.DataUseCases.user.name.GetUserName
import eco.point.domain.DataUseCases.user.name.SetUserName
import eco.point.domain.IFBCallback
import eco.point.domain.UserRepository
import java.io.File
import java.io.FileOutputStream

class PersonalAreaViewModel : ViewModel() {
    private var skiped: Boolean? = null;
    fun saveBmpInStore(bitmap: Bitmap){
        val rootPath = Environment.getExternalStorageDirectory()
        var cachePath = File(rootPath.absolutePath + "/DCIM/EcoPoint")
        if (!cachePath.exists())
            cachePath.mkdir()
        cachePath = File(cachePath.absolutePath + "/UserPic.jpg")
        try {
            cachePath.createNewFile()
            val oStream = FileOutputStream(cachePath)
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, oStream)
            oStream.close()
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun getName(locRep: LocalUserRepository, callback: (String) -> Unit){
        if (!getSkiped(locRep))
            GetUserName(FBUserRepository()).execute(object : IFBCallback<String>{
                override fun onReceive(data: String) {
                    if (GetUserName(locRep).execute() != data)
                        SetUserName(locRep).execute(data)
                    callback(data)
                }
            })
        else
            callback(GetUserName(locRep).execute())
    }

    fun getBonuses(locRep: LocalUserRepository, callback: (Int) -> Unit) {
        if (!getSkiped(locRep))
            GetUserBonuses(FBUserRepository()).execute(object : IFBCallback<Int> {
                override fun onReceive(data: Int) {
                    if (GetUserBonuses(locRep).execute() != data) SetUserBonuses(locRep).execute(data)
                    callback(data)
                }
            })
        else
            callback(GetUserBonuses(locRep).execute())
    }

    fun getLevel(locRep: LocalUserRepository, callback: (Int) -> Unit){
        if (!getSkiped(locRep))
            GetUserLevel(FBUserRepository()).execute(object : IFBCallback<Int>{
                override fun onReceive(data: Int) {
                    if (GetUserLevel(locRep).execute() != data) SetUserLevel(locRep).execute(data)
                    callback(data)
                }
            })
        else
            callback(GetUserLevel(locRep).execute())
    }

    fun getGC(locRep: LocalUserRepository, callback: (Int) -> Unit){
        if (!getSkiped(locRep))
            GetUserGarbageCounter(FBUserRepository()).execute(object : IFBCallback<Int>{
                override fun onReceive(data: Int) {
                    if (GetUserGarbageCounter(locRep).execute() != data) SetUserGarbageCounter(locRep).execute(data)
                    callback(data)
                }
            })
        else
            callback(GetUserGarbageCounter(locRep).execute())
    }

    fun getSkiped(locRep: LocalUserRepository): Boolean{
        if (skiped == null){
            skiped = GetSkiped(locRep).execute()
        }
        return skiped!!
    }
}