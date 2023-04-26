package com.example.rankinggames

import android.view.View
import com.example.rankinggames.model.Player

interface OnWinsClickListener: OnClickListener {

    fun addWin(player:Player, itemView: View)

    fun removeWin(player:Player,itemView: View)
}