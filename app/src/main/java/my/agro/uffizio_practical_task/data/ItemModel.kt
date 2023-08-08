package my.agro.uffizio_practical_task.data

data class ItemModel(val id: Int=0,
                     val itemId: Int=0,
                     val itemName: String="",
                     val itemFullName: String="",
                     val itemImage: String="",
                     var itemData: MutableList<ItemDataModel> = ArrayList(),
                     var type:Int = ConstantsVal.PARENT,
                     var isExpanded:Boolean = false
                     )
