package br.com.mantunes.sped.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CarrinhoOper(
    val carrinhoOper: Carrinho,
    val tipoOper: String
) : Parcelable
