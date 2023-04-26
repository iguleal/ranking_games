package com.example.rankinggames

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.rankinggames.model.App
import com.example.rankinggames.model.Player

class PlayerAdapter(
    private val playerList : List<Player>,
    val listener : OnWinsClickListener,
    ) : RecyclerView.Adapter<PlayerAdapter.GroupViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.player_item, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val itemCurrent = playerList[position]
        holder.bind(itemCurrent)
    }

    override fun getItemCount(): Int {
        return playerList.size
    }
    inner class GroupViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind( itemCurrent: Player){
            val playerName: TextView = itemView.findViewById(R.id.player_name)
            playerName.text = itemCurrent.name

            val btnPlus: ImageView = itemView.findViewById(R.id.btn_plus)
            val btnMinus: ImageView = itemView.findViewById(R.id.btn_minus)

            val playerWins: TextView = itemView.findViewById(R.id.txt_players_wins)
            playerWins.text = itemCurrent.wins.toString()

            btnPlus.setOnClickListener {
               listener.addWin(itemCurrent, itemView)
            }

            btnMinus.setOnClickListener {
                listener.removeWin(itemCurrent,itemView)
            }

            val container : CardView = itemView.findViewById(R.id.player_container_item)
            container.setOnLongClickListener {

             AlertDialog.Builder(itemView.rootView.context)
                 .setTitle(R.string.titleDialog)
                 .setPositiveButton(R.string.delete){_,_ ->
                     listener.onDelete(itemCurrent, adapterPosition)
                 }
                 .setNeutralButton(R.string.edit){_,_ ->
                     listener.onUpdate(itemCurrent)
                 }
                 .create()
                 .show()

                true
            }
        }
    }
}