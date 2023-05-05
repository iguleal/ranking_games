package com.example.rankinggames.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rankinggames.OnClickListener
import com.example.rankinggames.R
import com.example.rankinggames.adapter.GroupAdapter
import com.example.rankinggames.databinding.ActivityGroupsBinding
import com.example.rankinggames.model.App
import com.example.rankinggames.model.Group
import com.example.rankinggames.model.Player

class GroupsActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding: ActivityGroupsBinding
    private lateinit var groupList: MutableList<Group>
    private lateinit var adapter: GroupAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        groupList = mutableListOf()

        binding.rvGroups.layoutManager = LinearLayoutManager(this)
        adapter = GroupAdapter(groupList, this)
        binding.rvGroups.adapter = adapter

        supportActionBar()

        queryGroups()

        binding.btnAddGroup.setOnClickListener {

            val input = EditText(this)
            input.hint = getString(R.string.group_name)
            input.inputType = InputType.TYPE_CLASS_TEXT

            AlertDialog.Builder(this)
                .setTitle(R.string.create_group)
                .setView(input)

                .setPositiveButton(R.string.create_group) { _, _ ->

                    val groupName = input.text.toString()
                    val group = Group(name = groupName)

                    Thread {
                        val app = application as App
                        val dao = app.db.groupDao()
                        dao.insertGroup(group)

                        runOnUiThread {
                            groupList.add(group)
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

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    override fun onDelete(any: Any, position: Int) {

        val group = any as Group

        Thread {
            val app = application as App
            val dao = app.db.groupDao()
            val response = dao.deleteGroup(group)

            deletePlayers(group.id)

            if (response > 0) {
                runOnUiThread {
                    groupList.removeAt(position)
                    adapter.notifyItemRemoved(position)
                }
            }

        }.start()
    }

    private fun deletePlayers(groupId: Int) {

        Thread {
            val app = application as App
            val dao = app.db.playerDao()
            val listPlayers = dao.getPlayerById(groupId)

            listPlayers.forEach {
                dao.deletePlayer(it)
            }

            runOnUiThread {

            }

        }.start()
    }

    override fun onUpdate(any: Any) {

        val group = any as Group

        val input = EditText(this)
        input.hint = getString(R.string.group_name)
        input.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(this)
            .setTitle(R.string.edit_group)
            .setView(input)

            .setPositiveButton(R.string.save) { _, _ ->

                val newName = input.text.toString()

                Thread {
                    val app = application as App
                    val dao = app.db.groupDao()
                    dao.updateGroup(
                        Group(
                            id = group.id,
                            name = newName,
                            playersCount = group.playersCount
                        )
                    )

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

    private fun queryGroups() {
        Thread {
            val app = application as App
            val dao = app.db.groupDao()
            val response = dao.getGroups()


            runOnUiThread {
                groupList.addAll(response)
                adapter.notifyDataSetChanged()
            }

        }.start()
    }

    private fun supportActionBar() {
        setSupportActionBar(binding.toolbarGroup)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.groups)
    }
}