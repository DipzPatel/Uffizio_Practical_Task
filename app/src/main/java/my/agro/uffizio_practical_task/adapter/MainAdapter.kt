package my.agro.uffizio_practical_task.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import my.agro.uffizio_practical_task.data.ConstantsVal
import my.agro.uffizio_practical_task.data.ItemDataModel
import my.agro.uffizio_practical_task.data.ItemModel
import my.agro.uffizio_practical_task.databinding.SingleChildlistBinding
import my.agro.uffizio_practical_task.databinding.SingleMainlistBinding


class MainAdapter(private val list: ArrayList<ItemModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if(viewType== ConstantsVal.PARENT){
            val binding = SingleMainlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            GroupViewHolder(binding)
        } else {
            val binding = SingleChildlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ChildViewHolder(binding)
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val dataList = list[position]
        if (dataList.type == ConstantsVal.PARENT) {
            holder as GroupViewHolder
            holder.apply {
                binding.txtName.text = dataList.itemName
                binding.txtFullName.text = dataList.itemFullName
                Glide.with(holder.itemView.context).load(dataList.itemImage).into(binding.imgProfile)

                holder.itemView.setOnClickListener {
                    val dataList1 = list[position]
                    expandOrCollapseParentItem(dataList1,position)
                }
            }
        } else {
            holder as ChildViewHolder

            holder.apply {
                val singleService = dataList.itemData.first()
                binding.txtDesc.text =singleService.itemDesc
                binding.txtLang.text =singleService.language
                binding.txtStar.text =singleService.totalStar.toString()
                binding.txtFork.text =singleService.totalFork.toString()
            }
        }
    }
    private fun expandOrCollapseParentItem(singleBoarding: ItemModel,position: Int) {

        if (singleBoarding.isExpanded) {
            collapseParentRow(position)
        } else {
            expandParentRow(position)
        }
    }

    private fun expandParentRow(position: Int){
        val currentBoardingRow = list[position]
        val services = currentBoardingRow.itemData
        currentBoardingRow.isExpanded = true
        var nextPosition = position
        if(currentBoardingRow.type==ConstantsVal.PARENT){

            services.forEach { service ->
                val parentModel =  ItemModel()
                parentModel.type = ConstantsVal.CHILD
                val subList : ArrayList<ItemDataModel> = ArrayList()
                subList.add(service)
                parentModel.itemData=subList
                list.add(++nextPosition,parentModel)
            }
            notifyDataSetChanged()
        }
    }

    private fun collapseParentRow(position: Int){
        val currentBoardingRow = list[position]
        val services = currentBoardingRow.itemData
        list[position].isExpanded = false
        if(list[position].type==ConstantsVal.PARENT){
            services.forEach { _->
                list.removeAt(position + 1)
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemViewType(position: Int): Int = list[position].type

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class GroupViewHolder(val binding: SingleMainlistBinding) : RecyclerView.ViewHolder(binding.root)
    class ChildViewHolder(val binding: SingleChildlistBinding) : RecyclerView.ViewHolder(binding.root)
}