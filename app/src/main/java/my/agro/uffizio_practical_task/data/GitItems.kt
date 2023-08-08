package my.agro.uffizio_practical_task.data

data class GitItems(
    val incomplete_results: Boolean,
    val items: ArrayList<Item>,
    val total_count: Int,
)

object ConstantsVal {
    const val PARENT = 0
    const val CHILD = 1
}