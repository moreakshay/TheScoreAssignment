package com.moreakshay.thescoreassignment.teamdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.moreakshay.thescoreassignment.R
import com.moreakshay.thescoreassignment.databinding.ItemRosterBinding
import com.moreakshay.thescoreassignment.teamlist.domainmodels.Team

class PlayerAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlayerViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_roster,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PlayerViewHolder).bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Team.Player>() {
        override fun areItemsTheSame(oldItem: Team.Player, newItem: Team.Player): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Team.Player, newItem: Team.Player): Boolean {
            return oldItem.id == newItem.id
        }
    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(list: List<Team.Player>) {
        differ.submitList(list)
    }

}

class PlayerViewHolder(private val binding: ItemRosterBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(player: Team.Player) {
        binding.player = player
        binding.executePendingBindings()
    }
}
