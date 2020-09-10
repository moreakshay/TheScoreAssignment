package com.moreakshay.thescoreassignment.teamlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.moreakshay.thescoreassignment.R
import com.moreakshay.thescoreassignment.TheScoreApplication
import com.moreakshay.thescoreassignment.databinding.ActivityTeamListBinding
import com.moreakshay.thescoreassignment.teamdetails.TeamDetailsActivity
import javax.inject.Inject

class TeamListActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: TeamListViewModel by viewModels {viewModelFactory}
    private val binding: ActivityTeamListBinding by lazy {
        DataBindingUtil.setContentView<ActivityTeamListBinding>(this, R.layout.activity_team_list)
    }
    private val adapter: TeamAdapter = TeamAdapter(TeamClickListener { team ->
        var intent = Intent(this, TeamDetailsActivity::class.java)
//        intent.putExtra
        startActivity(intent)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        (application as TheScoreApplication).component.inject(this)
        bind()
    }

    private fun bind(){
        binding.teamList = viewModel.teamList
        binding.rvTeam.adapter = adapter
    }
}