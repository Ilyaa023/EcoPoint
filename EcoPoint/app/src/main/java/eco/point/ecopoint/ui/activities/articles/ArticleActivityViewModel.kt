package eco.point.ecopoint.ui.activities.articles

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import eco.point.data.firebase.database.articles.FBArticlesRepository
import eco.point.domain.DataUseCases.articles.GetArticleContent
import eco.point.domain.IFBCallback
import eco.point.domain.models.ArticleType1

class ArticleActivityViewModel: ViewModel() {
    fun getArticlesPics(articleType1: ArticleType1, callback: (ArticleType1) -> Unit){
        GetArticleContent(FBArticlesRepository()).execute(articleType1, object : IFBCallback<ArticleType1>{
            override fun onReceive(data: ArticleType1) {
                callback(data)
            }
        })
    }

    fun getBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}