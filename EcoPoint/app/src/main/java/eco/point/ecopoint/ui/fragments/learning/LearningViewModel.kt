package eco.point.ecopoint.ui.fragments.learning

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eco.point.domain.models.ArticleType1

class LearningViewModel : ViewModel() {

    val articles = MutableLiveData<ArrayList<ArticleType1>>()
    
    fun getArticles(){
        /*GetArticlesPreview(FBArticlesRepository()).execute {
            articles.value = it
        }*/

    }
    
    fun getBitmap(byteArray: ByteArray): Bitmap{
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}