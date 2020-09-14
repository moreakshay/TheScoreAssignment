package com.moreakshay.thescoreassignment.utils.bindings

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moreakshay.thescoreassignment.ui.teamlist.TeamAdapter
import com.moreakshay.thescoreassignment.ui.teamlist.domainmodels.Team
import com.moreakshay.thescoreassignment.utils.constants.NOT_AVAILABLE
import java.util.stream.Stream


@BindingAdapter("firstName", "lastName")
fun displayName(textView: TextView, firstName: String, lastName: String) {
    var name = firstName
    name = when {
        firstName.isNotEmpty() -> "$name $lastName"
        firstName.isEmpty() -> lastName
        else -> NOT_AVAILABLE
    }
    textView.text = name
}
