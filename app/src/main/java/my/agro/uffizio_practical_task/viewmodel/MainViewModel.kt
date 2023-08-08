package my.agro.uffizio_practical_task.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import my.agro.uffizio_practical_task.Database.DBHelper
import my.agro.uffizio_practical_task.data.*
import my.agro.uffizio_practical_task.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    val gitItemsList = MutableLiveData<GitItems>()
    var mainList = MutableLiveData<ArrayList<ItemModel>>()
    val errorMessage = MutableLiveData<String>()

    fun getAllItems() {
        val response = repository.getAllTrendingRepos()
        response.enqueue(object : Callback<GitItems> {
            override fun onResponse(call: Call<GitItems>, response: Response<GitItems>) {
                gitItemsList.postValue(response.body())
            }

            override fun onFailure(call: Call<GitItems>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun insertDB(context:Context, listItem: ArrayList<Item>){
        val db=DBHelper(context,null)
        db.clearAllItems()
        val list= mutableListOf<ItemModel>()
        val listData= mutableListOf<ItemDataModel>()

        list.clear()

        listItem.forEachIndexed{index,it->
            it.let {
                val itemDataModel = ItemDataModel(
                    it.description,
                    it.language,
                    it.stargazers_count,
                    it.forks_count
                )
                listData.clear()
                listData.add(itemDataModel)
                val itemModel=ItemModel(0,it.id,it.name,it.full_name,it.owner.avatar_url,listData)
                list.add(itemModel)
            }
            db.addItems(list.get(index))
        }

        val listVal=db.getAllItems()

        mainList.postValue(listVal)
    }

    fun checkForInternet(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}