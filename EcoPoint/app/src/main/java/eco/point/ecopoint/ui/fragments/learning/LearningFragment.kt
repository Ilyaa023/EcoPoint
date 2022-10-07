package eco.point.ecopoint.ui.fragments.learning

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import eco.point.domain.models.ArticleType1
import eco.point.ecopoint.databinding.FragmentLearningBinding
import eco.point.ecopoint.ui.activities.articles.tmp.PlMarkingsActivity
import eco.point.ecopoint.ui.activities.articles.tmp.PrepGlActivity
import eco.point.ecopoint.ui.activities.articles.tmp.PrepPlActivity

class LearningFragment : Fragment() {

    private val articleType1s = ArrayList<ArticleType1>()

    private lateinit  var binding: FragmentLearningBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val learningViewModel = ViewModelProvider(this)[LearningViewModel::class.java]
        binding = FragmentLearningBinding.inflate(inflater, container, false)
        binding.preparationPlasticArticle.setOnClickListener {
            startActivity(Intent(requireContext(), PrepPlActivity::class.java))
        }
        binding.preparationGlassArticle.setOnClickListener {
            startActivity(Intent(requireContext(), PrepGlActivity::class.java))
        }
        binding.plasticMarkingsArticle.setOnClickListener {
            startActivity(Intent(requireContext(), PlMarkingsActivity::class.java))
        }
        return binding.root
    }
}

//TODO: load from cloud

//        learningViewModel.articleType1s.observe(viewLifecycleOwner){
//            articleType1s.clear()
//            if (it.size > 0){
//                binding.articlesProgressBar.visibility = View.GONE
//            }
//            for (button: View in binding.articlesView.children){
//                binding.articlesView.removeView(button)
//            }
//            val dpW = resources.displayMetrics.widthPixels / 411f
//            for (article: ArticleType1 in it!!) {
//                articleType1s.add(article)
//                val button = Button(context)
//
//                val lp = LinearLayout.LayoutParams((367 * dpW).toInt(), (91 * dpW).toInt())
//                val tt = (22 * dpW).toInt()
//                lp.setMargins(tt, tt, tt, 0)
//                button.layoutParams = lp
//                button.setPadding((134 * dpW).toInt(), 0, (29 * dpW).toInt(), 0)
//                button.setTextColor(getColor(resources, R.color.white, null))
//                button.textSize = 12F
//                button.text = article.name
//                val bitmap = learningViewModel.getBitmap(article.mainPic!!)
//                val res: Bitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
//                val mask = BitmapFactory.decodeResource(resources, R.drawable.ic_article_mask).extractAlpha()
//                val canvas = Canvas(res)
//                val paint = Paint(Paint.ANTI_ALIAS_FLAG)
//                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
//                canvas.drawBitmap(bitmap, 0f, 0f, null)
//                canvas.drawBitmap(mask, 0f, 0f, paint)
//                paint.xfermode = null
//                paint.style = Paint.Style.STROKE
//                button.background = BitmapDrawable(resources, res)
//                button.setOnClickListener {
//                    val intent = Intent(context, ArticleActivity::class.java)
//                    intent.putStringArrayListExtra("Steps", article.steps)
//                    intent.putStringArrayListExtra("Texts", article.texts)
//                    intent.putExtra("id", article.id)
//                    startActivity(intent)
//                }
//                binding.articlesView.addView(button)
//            }
//        }
//        learningViewModel.getArticles()