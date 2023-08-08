package my.agro.uffizio_practical_task

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import my.agro.lahagora_practical_task.util.RetrofitService
import my.agro.uffizio_practical_task.Database.DBHelper
import my.agro.uffizio_practical_task.adapter.MainAdapter
import my.agro.uffizio_practical_task.databinding.ActivityMainBinding
import my.agro.uffizio_practical_task.factory.MyViewModelFactory
import my.agro.uffizio_practical_task.repository.MainRepository
import my.agro.uffizio_practical_task.util.Constant
import my.agro.uffizio_practical_task.viewmodel.MainViewModel
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private val retrofitService = RetrofitService.getInstance()
    private val db = DBHelper(this, null)
    var isFirstTime:Boolean=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setCustomView(R.layout.actionbar_custom)

        binding.shimmerLayout.startShimmer()
        //get viewModel instance using MyViewModelFactory
        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(MainRepository(retrofitService))
        )[MainViewModel::class.java]
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.setHasFixedSize(true)
        bindLiveData()
    }

    private fun bindLiveData() {

        setDataFromLocal()
        startTimer()

        viewModel.gitItemsList.observe(this) {
            Log.d(Constant.TAG, "gitItemsList: $it")
            binding.apply {
                if (it.items.isNotEmpty()) {
                    val list = it.items
                    viewModel.insertDB(this@MainActivity, list)
                }
            }
        }

        viewModel.mainList.observe(this) {
            Log.d(Constant.TAG, "mainList: $it")
            binding.apply {
                if (it.isNotEmpty()) {
                    shimmerLayout.stopShimmer()
                    shimmerLayout.visibility = View.GONE
                    relativeLayout.visibility = View.GONE
                    recyclerview.visibility = View.VISIBLE
                    val adapter = MainAdapter(it)
                    recyclerview.adapter = adapter
                }
            }
        }

        viewModel.errorMessage.observe(this) {
            Log.d(Constant.TAG, "errorMessage: $it")
            setView(false)
        }

        binding.btnRetry.setOnClickListener {
            setDataFromLocal()
        }

        binding.container.setOnRefreshListener {
            binding.container.isRefreshing = false
            setDataFromRemote()
        }
    }

    private fun setDataFromLocal() {
        val listVal = db.getAllItems()
        if (listVal.isEmpty()) {
            if (viewModel.checkForInternet(this)) {
                setView(true)
                viewModel.getAllItems()
            } else {
                setView(false)
            }
        } else {
            viewModel.mainList.postValue(listVal)
        }
    }

    fun setDataFromRemote() {
        if (viewModel.checkForInternet(this@MainActivity)) {
            setView(true)
            viewModel.getAllItems()
        } else {
            db.clearAllItems()
            setView(false)
        }
    }

    private fun setView(isNetworkConnected: Boolean) {
        binding.apply {
            if (isNetworkConnected) {
                shimmerLayout.startShimmer()
                shimmerLayout.visibility = View.VISIBLE
                recyclerview.visibility = View.GONE
                relativeLayout.visibility = View.GONE
            } else {
                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE
                recyclerview.visibility = View.GONE
                relativeLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun startTimer() {
        Timer().scheduleAtFixedRate( object : TimerTask() {
            override fun run() {
                runOnUiThread{
                    if(!isFirstTime){
                        Log.d(Constant.TAG,"timer Called after 2 Hours")
                        setDataFromRemote()
                    }
                    isFirstTime=false
                }
            }
        }, 0, 7200000)
    }

}
