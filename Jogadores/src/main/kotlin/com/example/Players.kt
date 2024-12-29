package com.example

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

// Definição da tabela Players
object Players : IntIdTable() {
    val nome = varchar("nome", 255)
    val idade = integer("idade")
    val clubeAtual = varchar("clube_atual", 255)
    val formacao = varchar("formacao", 255)
    val melhorPe = varchar("melhor_pe", 10)
}

// Modelo do jogador
@Serializable
data class Player(
    val id: Int,
    val nome: String,
    val idade: Int,
    val clubeAtual: String,
    val formacao: String,
    val melhorPe: String
)

// Função de mapeamento para converter ResultRow em Player
fun ResultRow.toPlayer() = Player(
    id = this[Players.id].value,
    nome = this[Players.nome],
    idade = this[Players.idade],
    clubeAtual = this[Players.clubeAtual],
    formacao = this[Players.formacao],
    melhorPe = this[Players.melhorPe]
)
