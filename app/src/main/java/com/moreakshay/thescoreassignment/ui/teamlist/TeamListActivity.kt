package com.moreakshay.thescoreassignment.ui.teamlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.moreakshay.thescoreassignment.R
import com.moreakshay.thescoreassignment.TheScoreApplication
import com.moreakshay.thescoreassignment.databinding.ActivityTeamListBinding
import com.moreakshay.thescoreassignment.ui.teamdetails.TeamDetailsActivity
import com.moreakshay.thescoreassignment.utils.network.Status
import kotlinx.android.synthetic.main.activity_team_list.*
import kotlinx.android.synthetic.main.snackbar_error.*
import javax.inject.Inject

class TeamListActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: TeamListViewModel by viewModels { viewModelFactory }

    private val binding: ActivityTeamListBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_team_list)
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

            if (resource.data?.isNotEmpty() == true) {
                rvTeam.smoothScrollToPosition(0)
            }

            binding.status = resource.status
        }
        binding.rvTeam.adapter = adapter

        bRefresh.setOnClickListener { viewModel.refresh() }
        bSortAlpha.setOnClickListener { viewModel.sortBy(SortOrder.NAME) }
        bSortWins.setOnClickListener { viewModel.sortBy(SortOrder.WINS) }
        bSortLosses.setOnClickListener { viewModel.sortBy(SortOrder.LOSSES) }
    }
}