package com.example.rankinggames

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rankinggames.databinding.ActivityPlayersBinding
import com.example.rankinggames.model.App
import com.example.rankinggames.model.Group
import com.example.rankinggames.model.Player

class PlayersActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding: ActivityPlayersBinding
    private lateinit var playerList: MutableList<Player>
    private lateinit var adapter: PlayerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val groupId = intent?.extras?.getInt("groupId") ?: throw IllegalStateException("type not found")

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
                    dao.updatePlayer(Player(id = player.id, name = newName, wins = player.wins, groupId = player.groupId))

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

    private fun supportActionBar(){
        setSupportActionBar(binding.toolbarPlayer)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Jogadores"
    }

    private fun queryPlayers(){
        val groupId = intent?.extras?.getInt("groupId") ?: throw IllegalStateException("type not found")

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
}