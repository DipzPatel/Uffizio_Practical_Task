package my.agro.uffizio_practical_task.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import my.agro.uffizio_practical_task.repository.MainRepository
import my.agro.uffizio_practical_task.viewmodel.MainViewModel

class MyViewModelFactory constructor(private val repository: MainRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}