package com.moreakshay.thescoreassignment.ui.teamlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.moreakshay.thescoreassignment.R
import com.moreakshay.thescoreassignment.databinding.ItemTeamBinding
import com.moreakshay.thescoreassignment.ui.teamlist.domainmodels.Team

class TeamAdapter(private val clickListener: TeamClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TeamViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_team,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TeamViewHolder).bind(differ.currentList[position], clickListener)
    }

    override fun getItemCount(): Int = differ.currentList.size

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Team>() {
        override fun areItemsTheSame(oldItem: Team, newItem: Team): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Team, newItem: Team): Boolean {
            return oldItem.id == newItem.id
        }
    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(list: List<Team>) {
        differ.submitList(list)
    }
}

class TeamViewHolder(private val binding: ItemTeamBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(team: Team, clickListener: TeamClickListener) {
        binding.team = team
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }
}

class TeamClickListener constructor(val clickListener: (team: Team) -> Unit) {
    fun onClick(team: Team) = clickListener(team)
}