package my.agro.lahagora_practical_task.util

import my.agro.uffizio_practical_task.data.GitItems
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RetrofitService {
    @GET("repositories?q=%7Bquery%7D&sort=stars")
    fun getAllTrendingRepos(): Call<GitItems>

    companion object {

        private var retrofitService: RetrofitService? = null

        //Create the Retrofit service instance using the retrofit.
        fun getInstance(): RetrofitService {

            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.github.com/search/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }
}