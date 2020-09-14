package com.moreakshay.thescoreassignment.ui.teamdetails

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.moreakshay.thescoreassignment.R
import com.moreakshay.thescoreassignment.databinding.ActivityTeamDetailsBinding
import com.moreakshay.thescoreassignment.ui.teamlist.domainmodels.Team

class TeamDetailsActivity : AppCompatActivity() {
    companion object {
        private const val TEAM_INTENT_KEY = "TEAM_INTENT_KEY"

        fun intentFor(context: Context, team: Team): Intent {
            return Intent(context, TeamDetailsActivity::class.java).apply {
                putExtra(TEAM_INTENT_KEY, team)
            }
        }
    }

    val team: Team by lazy {
        intent.getParcelableExtra(TEAM_INTENT_KEY)!!
    }

    val binding: ActivityTeamDetailsBinding by lazy {
        DataBindingUtil.setContentView(
            this,
            R.layout.activity_team_details
        )
    }

    val adapter: PlayerAdapter = PlayerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        bind()
    }

    private fun bind() {
        binding.team = team
        adapter.submitList(team.players)
        binding.rvRoster.adapter = adapter

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }
}