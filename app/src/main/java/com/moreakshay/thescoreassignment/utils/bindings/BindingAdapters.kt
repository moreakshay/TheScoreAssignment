package com.moreakshay.thescoreassignment.utils.bindings

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moreakshay.thescoreassignment.ui.teamlist.TeamAdapter
import com.moreakshay.thescoreassignment.ui.teamlist.domainmodels.Team
import com.moreakshay.thescoreassignment.utils.constants.NOT_AVAILABLE


@BindingAdapter("displayName")
fun displayName(textView: TextView, name: String){
    //TODO: take first name and last name add no space if either is empty
    if(name.isNotEmpty()) textView.text = name
    else textView.text = NOT_AVAILABLE
}
