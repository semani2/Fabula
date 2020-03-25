package com.sai.fabula.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.sai.fabula.R
import com.sai.fabula.database.model.Article
import com.sai.fabula.databinding.ItemArticleBinding
import io.reactivex.rxjava3.subjects.PublishSubject

/**
 * ViewHolder to inflate articles for the News Adapter
 *
 * @see NewsAdapter
 */
class ArticleViewHolder(private val binding: ItemArticleBinding, private val publishSubject: PublishSubject<String>)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(article: Article) {
        binding.articleTitle.text = article.title
        binding.articleAuthor.text = article.author
        binding.articleSource.text = article.source
        binding.articleImage.load(article.imageUrl) {
            placeholder(R.drawable.ic_photo)
            error(R.drawable.ic_broken_image)
        }
        binding.articleItemLayout.setOnClickListener {
            publishSubject.onNext(article.url)
        }
    }
}
