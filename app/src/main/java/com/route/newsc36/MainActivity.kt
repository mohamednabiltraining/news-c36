package com.route.newsc36

import android.content.DialogInterface
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.route.base.BaseActivity
import com.route.newsc36.api.ApiManager
import com.route.newsc36.api.model.NewsResponse
import com.route.newsc36.api.model.Source
import com.route.newsc36.api.model.SourcesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity() {
    val apiKey = "5909ae28122a471d8b0c237d5989cb73";
    lateinit var tabLayout: TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tabLayout = findViewById(R.id.tab_layout);
    }

    override fun onStart() {
        super.onStart()
        getNewsSources();
    }

    fun getNewsSources() {
        showLoadingDialog();
        ApiManager
            .getService()
            .getNewsSources(apiKey)
            .enqueue(object : Callback<SourcesResponse> {
                override fun onFailure(call: Call<SourcesResponse>, t: Throwable) {
                    hideLoading()
                    showMessage("Something went wrong try again",
                        posActionTitle = "tryAgain",
                        posAction = DialogInterface.OnClickListener { dialogInterface, i ->
                            dialogInterface.dismiss()
                            getNewsSources()
                        });
                }

                override fun onResponse(
                    call: Call<SourcesResponse>,
                    response: Response<SourcesResponse>
                ) {
                    hideLoading()
                    if (response.body()?.status.equals("error")) {
                        showMessage(
                            response.body()?.message ?: "",
                            posActionTitle = "Ok"
                        )
                    } else {
                        showSourcesInTabs(response.body()?.sources);
                    }
                }
            })
    }

    fun getNews(sourceId: String) {
        showLoadingDialog()
        ApiManager
            .getService()
            .getNews(apiKey, sourceId)
            .enqueue(object : Callback<NewsResponse> {
                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    hideLoading()
                    showMessage("Something went wrong try again",
                        posActionTitle = "tryAgain",
                        posAction = DialogInterface.OnClickListener { dialogInterface, i ->
                            dialogInterface.dismiss()
                            getNews(sourceId)
                        });

                }

                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {
                    hideLoading()
                    if (response.body()?.status.equals("error")) {
                        showMessage(response?.body()?.message ?: "");
                        return
                    }

                    // Todo: show news in recycler view

                }
            })
    }

    fun showSourcesInTabs(sources: List<Source?>?) {
        if (sources == null) return;
//        for(i in 0..sources.size-1){
//            val item = sources[i];
//        }

        sources.forEach { source ->
            val tab = tabLayout.newTab()
            tab.text = source?.name
            tab.tag = source
            tabLayout.addTab(tab)
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                val source = tab?.tag as Source
                getNews(source.id!!)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val source = tab?.tag as Source
                getNews(source.id!!)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
        })
        tabLayout.getTabAt(0)?.select()
    }
}