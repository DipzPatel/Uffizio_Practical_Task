package my.agro.uffizio_practical_task.repository

import my.agro.lahagora_practical_task.util.RetrofitService

class MainRepository constructor(private val retrofitService: RetrofitService) {

    fun getAllTrendingRepos() = retrofitService.getAllTrendingRepos()
}