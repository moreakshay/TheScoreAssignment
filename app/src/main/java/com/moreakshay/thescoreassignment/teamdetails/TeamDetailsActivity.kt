package com.moreakshay.thescoreassignment.teamdetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.moreakshay.thescoreassignment.R
import com.moreakshay.thescoreassignment.databinding.ActivityTeamDetailsBinding
import com.moreakshay.thescoreassignment.teamlist.domainmodels.Team
import com.moreakshay.thescoreassignment.utils.constants.TEAM_INTENT_KEY

class TeamDetailsActivity : AppCompatActivity() {

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
    }

    public fun onIvBackClicked(view: View) = onBackPressed()
}