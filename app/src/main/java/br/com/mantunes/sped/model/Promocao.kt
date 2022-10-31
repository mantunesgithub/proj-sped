package br.com.mantunes.sped.model

import androidx.annotation.DrawableRes

data class Promocao(
  val title: String,
  @DrawableRes val drawableId: Int,
  val nome: String,
  val desc: String,
  val valorAnt : String,
  val valorAtua : String
)
