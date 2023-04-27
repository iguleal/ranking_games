package com.example.rankinggames

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rankinggames.databinding.ActivityPlayersBinding
import com.example.rankinggames.model.App
import com.example.rankinggames.model.Group
import com.example.rankinggames.model.Player

class PlayersActivity : AppCompatActivity(), OnClickListener, OnWinsClickListener {

    private lateinit var binding: ActivityPlayersBinding
    lateinit var playerList: MutableList<Player>
    private lateinit var adapter: PlayerAdapter
    private var groupId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        groupId = intent?.extras?.getInt("groupId") ?: throw IllegalStateException("type not found")

        playerList = mutableListOf()

        binding.rvPlayers.layoutManager = LinearLayoutManager(this)
        adapter = PlayerAdapter(playerList, this)
        binding.rvPlayers.adapter = adapter

        supportActionBar()

        queryPlayers()

        binding.btnAddPlayer.setOnClickListener {

            val input = EditText(this)
            input.hint = getString(R.string.player_name)
            input.inputType = InputType.TYPE_CLASS_TEXT

            AlertDialog.Builder(this)
                .setTitle(R.string.create_player)
                .setView(input)

                .setPositiveButton(R.string.create_player) { _, _ ->

                    val playerName = input.text.toString()
                    val player = Player(name = playerName, groupId = groupId)
                    playerList.add(player)

                    Thread {
                        val app = application as App
                        val dao = app.db.playerDao()
                        dao.insertPlayer(player)

                        val daoGroup = app.db.groupDao()
                        val playerGroup = daoGroup.getGroup(groupId)

                        playerGroup.playersCount += 1
                        val newCount = playerGroup.playersCount

                        daoGroup.updateGroup(Group(id = playerGroup.id, name = playerGroup.name, playersCount = newCount))

                        runOnUiThread {
                            recreate()
                        }

                    }.start()
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                }
                .create()
                .show()
        }
    }

    override fun onDelete(any: Any, position: Int) {
        Thread {
            val app = application as App
            val dao = app.db.playerDao()
            val response = dao.deletePlayer(any as Player)

            val daoGroup = app.db.groupDao()
            val playerGroup = daoGroup.getGroup(groupId)

            playerGroup.playersCount -= 1
            val newCount = playerGroup.playersCount

            daoGroup.updateGroup(Group(id = playerGroup.id, name = playerGroup.name, playersCount = newCount))

            if (response > 0) {
                runOnUiThread {
                    playerList.removeAt(position)
                    adapter.notifyItemRemoved(position)
                }
            }
        }.start()
    }

    override fun onUpdate(any: Any) {
        val player = any as Player

        val input = EditText(this)
        input.hint = getString(R.string.player_name)
        input.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(this)
            .setTitle(R.string.edit_player)
            .setView(input)

            .setPositiveButton(R.string.save) { _, _ ->

                val newName = input.text.toString()

                Thread {
                    val app = application as App
                    val dao = app.db.playerDao()
                    dao.updatePlayer(Player(name = newName, groupId = player.groupId))

                    runOnUiThread {
                        recreate()
                    }
                }.start()
            }
            .setNegativeButton(R.string.cancel) { _, _ ->
            }
            .create()
            .show()
    }

    private fun supportActionBar() {

        Thread{
            val app = application as App
            val daoGroup = app.db.groupDao()
            val playerGroup = daoGroup.getGroup(groupId)

            runOnUiThread {
                setSupportActionBar(binding.toolbarPlayer)
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.title = playerGroup.name
            }

        }.start()
    }

    private fun queryPlayers() {
        groupId = intent?.extras?.getInt("groupId") ?: throw IllegalStateException("type not found")

        Thread {
            val app = application as App
            val dao = app.db.playerDao()
            val response = dao.getPlayerById(groupId)

            runOnUiThread {
                playerList.addAll(response)
                adapter.notifyDataSetChanged()
            }
        }.start()
    }

    override fun addWin(player: Player, itemView: View) {
        val playerWins: TextView = itemView.findViewById(R.id.txt_players_wins)

        player.wins += 1            // player.wins = player.wins + 1
        val wins = player.wins
        playerWins.text = wins.toString()

        Thread {
            val app = application as App
            val dao = app.db.playerDao()
            dao.updatePlayer(
                Player(
                    id = player.id,
                    name = player.name,
                    wins = wins,
                    groupId = player.groupId
                )
            )


        }.start()
    }

    override fun removeWin(player: Player, itemView: View) {
        val playerWins: TextView = itemView.findViewById(R.id.txt_players_wins)

        player.wins -= 1            // player.wins = player.wins - 1
        val wins = player.wins
        playerWins.text = wins.toString()

        Thread {
            val app = application as App
            val dao = app.db.playerDao()
            dao.updatePlayer(
                Player(
                    id = player.id,
                    name = player.name,
                    wins = wins,
                    groupId = player.groupId
                )
            )

        }.start()
    }

}