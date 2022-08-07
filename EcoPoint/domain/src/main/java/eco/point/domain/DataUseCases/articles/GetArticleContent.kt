package eco.point.domain.DataUseCases.articles

import eco.point.domain.ArticlesRepository
import eco.point.domain.IFBCallback
import eco.point.domain.models.ArticleType1

class GetArticleContent(private val repository: ArticlesRepository) {
    fun execute(articleType1: ArticleType1, callback: IFBCallback<ArticleType1>){
        articleType1.pictures.clear()
        repository.getArticleContentPics(articleType1) { articlePics ->
            articleType1.pictures = articlePics.pictures
            callback.onReceive(articleType1)
        }
    }
}