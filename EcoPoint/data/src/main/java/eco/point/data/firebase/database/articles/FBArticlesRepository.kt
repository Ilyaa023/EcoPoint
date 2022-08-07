package eco.point.data.firebase.database.articles

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import eco.point.domain.ArticlesRepository
import eco.point.domain.models.ArticleType1
import eco.point.domain.models.enums.ArticleKeys
import eco.point.domain.models.enums.DataType

class FBArticlesRepository: ArticlesRepository() {
    val articles = FirebaseDatabase.getInstance(
        "https://my-ecopoint-project-default-rtdb.europe-west1.firebasedatabase.app/"
    ).getReference(DataType.ARTICLES.key)
    private val articlesPicStore = Firebase.storage("gs://my-ecopoint-project.appspot.com").getReference("Articles")

    override fun getArticlesRTDB(callback: (ArrayList<ArticleType1>) -> Unit) {
        articles.get().addOnCompleteListener{
            if (it.isSuccessful) {
                val articlesList = ArrayList<ArticleType1>()
                for (snapshot: DataSnapshot in it.result.children){
                    val id = snapshot.key
                    val name = snapshot.child(ArticleKeys.NAME.key).value.toString()
                    val pub = snapshot.child(ArticleKeys.PUBLIC.key).value.toString().toBoolean()
                    val steps = ArrayList<String>()
                    val texts = ArrayList<String>()
                    for (num: Int in 0 until snapshot.child(ArticleKeys.STEPS.key).childrenCount.toInt()){
                        steps.add(num, snapshot.child(ArticleKeys.STEPS.key).child(num.toString()).value.toString())
                        texts.add(num, snapshot.child(ArticleKeys.TEXTS.key).child(num.toString()).value.toString())
                    }
                    if (pub)
                        articlesList.add(
                            ArticleType1(
                                id = id!!,
                                name = name,
                                public = pub,
                                steps = steps,
                                texts = texts
                            )
                        )
                }
                callback(articlesList)
            }
        }
    }

    override fun getArticleMainPic(articleType1: ArticleType1, callback: (ArticleType1) -> Unit) {
        articlesPicStore.child(articleType1.id).child(ArticleKeys.MAIN_PIC.key).child("pic.jpg").getBytes(
            Long.MAX_VALUE).addOnCompleteListener{
            try {
                articleType1.mainPic = it.result
            }catch (e:Exception){}
            callback(articleType1)
        }
    }

    override fun getArticleContentPics(articleType1: ArticleType1, callback: (ArticleType1) -> Unit) {
        articleType1.pictures = ArrayList<ByteArray?>()
        var origSum = 0
        var sum = 0
        for (counter: Int in 0..articleType1.steps.size - 1) {
            origSum += counter
            articleType1.pictures.add(counter, null)
            articlesPicStore.child(articleType1.id)
                .child(ArticleKeys.PICTURES.key).child(counter.toString())
                .child("pic.jpg").getBytes(
                    Long.MAX_VALUE
                ).addOnCompleteListener {
                    sum += counter
                    try {
                        articleType1.pictures.set(counter, it.result)
                    } catch (e: Exception) { }
                    if (sum == origSum) {
                        callback(articleType1)
                    }
                }
        }
    }
}