package vip.ruoyun.googleaac.base

import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

abstract class ListAdapter<T, VH : RecyclerView.ViewHolder>(
    private val mCoScope: CoroutineScope,
    val diffCallback: ItemCallback<T>
) : RecyclerView.Adapter<VH>() {

    private var mList: List<T>? = null

    private var mReadOnlyList = emptyList<T>()

    private val mUpdateCallback: AdapterListUpdateCallback by lazy(LazyThreadSafetyMode.NONE) {
        AdapterListUpdateCallback(this)
    }

    public fun submitList(newList: List<T>?) {
        // incrementing generation means any currently-running diffs are discarded when they finish
        if (newList === mList) {
            // nothing to do (Note - still had to inc generation, since may have ongoing work)
            return
        }

        // fast simple remove all
        if (newList == null) {

            val countRemoved = mList!!.size
            mList = null
            mReadOnlyList = emptyList<T>()
            // notify last, after list is updated
            mUpdateCallback.onRemoved(0, countRemoved)
            return
        }

        // fast simple first insert
        if (mList == null) {
            mList = newList
            mReadOnlyList = Collections.unmodifiableList<T>(newList)
            // notify last, after list is updated
            mUpdateCallback.onInserted(0, newList.size)
            return
        }

        val oldList = mList
        mCoScope.launch {
            val withContext = withContext(IO) {
                return@withContext DiffUtil.calculateDiff(
                    AdapterDiffCallback(
                        oldList!!,
                        newList
                    )
                )
            }
            mList = newList
            mReadOnlyList = Collections.unmodifiableList<T>(newList)
            withContext.dispatchUpdatesTo(mUpdateCallback)
        }
    }

    public fun getItem(position: Int): T {
        return mReadOnlyList[position]
    }

    public override fun getItemCount(): Int {
        return mReadOnlyList.size
    }

    private inner class AdapterDiffCallback(
        private val oldList: List<T>,
        private val newList: List<T>
    ) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return if (oldItem != null && newItem != null) {
                diffCallback.areItemsTheSame(oldItem, newItem)
            } else oldItem == null && newItem == null
            // If both items are null we consider them the same.
        }

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            if (oldItem != null && newItem != null) {
                return diffCallback.areContentsTheSame(oldItem, newItem)
            }
            if (oldItem == null && newItem == null) {
                return true
            }
            // There is an implementation bug if we reach this point. Per the docs, this
            // method should only be invoked when areItemsTheSame returns true. That
            // only occurs when both items are non-null or both are null and both of
            // those cases are handled above.
            throw AssertionError()
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            if (oldItem != null && newItem != null) {
                return diffCallback.getChangePayload(oldItem, newItem)
            }
            // There is an implementation bug if we reach this point. Per the docs, this
            // method should only be invoked when areItemsTheSame returns true AND
            // areContentsTheSame returns false. That only occurs when both items are
            // non-null which is the only case handled above.
            throw AssertionError()
        }
    }
}