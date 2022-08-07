package eco.point.domain.DataUseCases.articles

import eco.point.domain.ArticlesRepository
import eco.point.domain.models.ArticleType1


class GetArticlesPreview(private val repository: ArticlesRepository) {
    fun execute(callback: (ArrayList<ArticleType1>) -> Unit){
        repository.getArticlesRTDB { articles ->
            var sum = 0
            var pubSum = 0
            val pubArticleType1s = ArrayList<ArticleType1>()
            for (art: ArticleType1 in articles){
                if (art.public)
                    pubSum++
                    repository.getArticleMainPic(art) { article ->
                        art.mainPic = article.mainPic
                        pubArticleType1s.add(art)
                        sum++
                        if (sum == pubSum){
                            callback(pubArticleType1s)
                        }
                    }
            }
        }
    }
}