package vip.ruoyun.googleaac

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import vip.ruoyun.googleaac.base.ListAdapter

class TestAdapter(mCoScope: CoroutineScope, diffCallback: DiffUtil.ItemCallback<User>) :
    ListAdapter<User, RecyclerView.ViewHolder>(mCoScope, diffCallback) {

    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}