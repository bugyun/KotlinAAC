package vip.ruoyun.googleaac.base

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.util.ArrayList
import kotlin.properties.Delegates


class TestAdapter(val main: CoroutineScope) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return null
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)


    }

    override fun getItemCount(): Int {
        return 0
    }


    private inner class AdapterDiffCallback(
        private val mOldList: List<String>,
        private val mNewList: List<String>
    ) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return mOldList.size
        }

        override fun getNewListSize(): Int {
            return mNewList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return mOldList[oldItemPosition] == mNewList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return mOldList[oldItemPosition] == mNewList[newItemPosition]
        }
    }

    fun test() {
        val main = MainScope()
        val testAdapter = TestAdapter(main)
        main.launch {
            val withContext = withContext(IO) {
                return@withContext DiffUtil.calculateDiff(
                    AdapterDiffCallback(
                        ArrayList(),
                        ArrayList()
                    )
                )
            }
            withContext.dispatchUpdatesTo(testAdapter)
        }

    }


}