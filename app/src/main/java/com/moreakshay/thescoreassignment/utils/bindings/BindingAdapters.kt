package com.moreakshay.thescoreassignment.utils.bindings

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moreakshay.thescoreassignment.ui.teamlist.TeamAdapter
import com.moreakshay.thescoreassignment.ui.teamlist.domainmodels.Team
import com.moreakshay.thescoreassignment.utils.constants.NOT_AVAILABLE


@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, teamList: List<Team>?) =
        teamList?.let { (recyclerView.adapter as TeamAdapter).submitList(it) } // TODO: do something about this

@BindingAdapter("displayName")
fun displayName(textView: TextView, name: String){
    if(name.isNotEmpty()) textView.text = name
    else textView.text = NOT_AVAILABLE
}
