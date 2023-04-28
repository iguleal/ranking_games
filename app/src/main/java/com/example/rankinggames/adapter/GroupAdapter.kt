package com.example.rankinggames.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.rankinggames.OnClickListener
import com.example.rankinggames.ui.PlayersActivity
import com.example.rankinggames.R
import com.example.rankinggames.model.Group

class GroupAdapter(
    private val groupList: List<Group>,
    val listener: OnClickListener,
) : RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.group_item, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val itemCurrent = groupList[position]
        holder.bind(itemCurrent)
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(itemCurrent: Group) {
            val groupName: TextView = itemView.findViewById(R.id.group_name)
            groupName.text = itemCurrent.name

            val playerCount: TextView = itemView.findViewById(R.id.txt_players_count)
            playerCount.text = itemCurrent.playersCount.toString()

            val container: CardView = itemView.findViewById(R.id.group_container_item)

            container.setOnLongClickListener {

                AlertDialog.Builder(itemView.context)
                    .setTitle(R.string.titleDialog)
                    .setPositiveButton(R.string.delete) { _, _ ->
                        listener.onDelete(itemCurrent, adapterPosition)
                    }
                    .setNeutralButton(R.string.edit) { _, _ ->
                        listener.onUpdate(itemCurrent)
                    }
                    .create()
                    .show()

                true
            }

            container.setOnClickListener {
                val i = Intent(itemView.context, PlayersActivity::class.java)
                i.putExtra("groupId", itemCurrent.id)
                itemView.context.startActivity(i)
            }

        }
    }
}