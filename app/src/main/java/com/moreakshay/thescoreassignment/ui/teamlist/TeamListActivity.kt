package com.moreakshay.thescoreassignment.ui.teamlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.moreakshay.thescoreassignment.R
import com.moreakshay.thescoreassignment.TheScoreApplication
import com.moreakshay.thescoreassignment.databinding.ActivityTeamListBinding
import com.moreakshay.thescoreassignment.ui.teamdetails.TeamDetailsActivity
import com.moreakshay.thescoreassignment.utils.network.Status
import javax.inject.Inject

class TeamListActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: TeamListViewModel by viewModels { viewModelFactory }

    private val binding: ActivityTeamListBinding by lazy {
        DataBindingUtil.setContentView<ActivityTeamListBinding>(this, R.layout.activity_team_list)
    }

    private val adapter: TeamAdapter = TeamAdapter(TeamClickListener { team ->
        startActivity(TeamDetailsActivity.intentFor(this, team))
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        (application as TheScoreApplication).component.inject(this)
        bind()
    }

    private fun bind() {
        viewModel.teamList.observe(this) { resource ->
            adapter.submitList(resource.data ?: emptyList())
            binding.status = resource.status
        }
        binding.rvTeam.adapter = adapter
    }
}