package com.sai.fabula

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sai.fabula.database.FabulaNewsDatabase
import com.sai.fabula.database.dao.ArticlesDao
import com.sai.fabula.database.model.Article
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArticlesDaoTest {

    private lateinit var articlesDao: ArticlesDao
    private lateinit var fabulaNewsDb: FabulaNewsDatabase

    private var testArticle1 = Article(id = 1,
        title = "News title 1",
        description = "News description...",
        source = "NewsSource1",
        url = "https://news_source.com",
        imageUrl = "https://news_source.com/image.png",
        author = "News Writer 1")

    private var testArticle2 = Article(id = 2,
        title = "News title 2",
        description = "News description...",
        source = "NewsSource2",
        url = "https://news_source.com",
        imageUrl = "https://news_source.com/image.png",
        author = "News Writer 2")

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        fabulaNewsDb = Room.inMemoryDatabaseBuilder(context, FabulaNewsDatabase::class.java)
            .build()

        articlesDao = fabulaNewsDb.getArticlesDao()
    }

    @After
    fun closeDb() {
        fabulaNewsDb.close()
    }

    @Test
    fun test_get_all_articles_returns_empty_list_when_no_data_inserted() = runBlocking {
        val articles = articlesDao.getAllArticles()
        assertEquals("Non empty list returned", articles.size, 0)
    }

    @Test
    fun test_get_all_articles_returns_inserted_data() = runBlocking {
        articlesDao.insertArticles(listOf(testArticle1, testArticle2))

        val articles = articlesDao.getAllArticles()
        assertEquals("Less than inserted items returned", articles.size, 2)
    }

    @Test
    fun test_delete_articles_removes_all_articles() = runBlocking {
        articlesDao.insertArticles(listOf(testArticle1, testArticle2))

        var articles = articlesDao.getAllArticles()
        assertEquals("Less than inserted items returned", articles.size, 2)

        articlesDao.deleteAllPosts()

        articles = articlesDao.getAllArticles()
        assertEquals("Returned non empty list after delete", articles.size, 0)
    }
}
