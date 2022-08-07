package eco.point.domain

import eco.point.domain.models.ArticleType1

abstract class ArticlesRepository {
    open fun getArticlesRTDB(callback: (ArrayList<ArticleType1>) -> Unit){}
    open fun getArticleMainPic(articleType1: ArticleType1, callback: (ArticleType1) -> Unit){}
    open fun getArticleContentPics(articleType1: ArticleType1, callback: (ArticleType1) -> Unit){}
}