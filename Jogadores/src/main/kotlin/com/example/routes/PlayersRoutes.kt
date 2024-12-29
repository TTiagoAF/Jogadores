package com.example.routes

import com.example.Players
import com.example.toPlayer
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.playerRoutes() {
    route("/players") {
        // Criar Jogador
        post {
            val params = call.receive<Map<String, String>>()
            val nome = params["nome"] ?: return@post call.respondText("Nome é obrigatório", status = HttpStatusCode.BadRequest)
            val idade = params["idade"]?.toIntOrNull() ?: return@post call.respondText("Idade inválida", status = HttpStatusCode.BadRequest)
            val clubeAtual = params["clubeAtual"] ?: return@post call.respondText("Clube Atual é obrigatório", status = HttpStatusCode.BadRequest)
            val formacao = params["formacao"] ?: return@post call.respondText("Formação é obrigatória", status = HttpStatusCode.BadRequest)
            val melhorPe = params["melhorPe"] ?: return@post call.respondText("Melhor Pé é obrigatório", status = HttpStatusCode.BadRequest)

            transaction {
                Players.insert {
                    it[Players.nome] = nome
                    it[Players.idade] = idade
                    it[Players.clubeAtual] = clubeAtual
                    it[Players.formacao] = formacao
                    it[Players.melhorPe] = melhorPe
                }
            }
            call.respondText("Jogador adicionado com sucesso", status = HttpStatusCode.Created)
        }

        // Listar todos os jogadores
        get("/") {
            try {
                val players = transaction {
                    Players.selectAll().map { it.toPlayer() }
                }
                call.respond(players)
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Erro ao buscar jogadores.")
            }
        }

        // Buscar jogador por ID
        get("/{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID inválido.")
                    return@get
                }

                val player = transaction {
                    Players.select { Players.id eq id }
                        .map { it.toPlayer() }
                        .singleOrNull()
                }

                if (player == null) {
                    call.respond(HttpStatusCode.NotFound, "Jogador não encontrado.")
                } else {
                    call.respond(player)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Erro ao buscar jogador.")
            }
        }

        // Atualizar jogador
        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@put call.respondText("ID inválido", status = HttpStatusCode.BadRequest)
            val params = call.receive<Map<String, String>>()
            val nome = params["nome"]
            val idade = params["idade"]?.toIntOrNull()
            val clubeAtual = params["clubeAtual"]
            val formacao = params["formacao"]
            val melhorPe = params["melhorPe"]

            transaction {
                Players.update({ Players.id eq id }) {
                    if (nome != null) it[Players.nome] = nome
                    if (idade != null) it[Players.idade] = idade
                    if (clubeAtual != null) it[Players.clubeAtual] = clubeAtual
                    if (formacao != null) it[Players.formacao] = formacao
                    if (melhorPe != null) it[Players.melhorPe] = melhorPe
                }
            }
            call.respondText("Jogador atualizado com sucesso")
        }

        // Deletar jogador
        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respondText("ID inválido", status = HttpStatusCode.BadRequest)

            transaction {
                Players.deleteWhere { Players.id eq id }
            }
            call.respondText("Jogador deletado com sucesso")
        }

        get("/position/{formacao}") {
            try {
                val formacao = call.parameters["formacao"]
                if (formacao.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest, "Posição (formação) é obrigatória.")
                    return@get
                }

                val players = transaction {
                    Players.select { Players.formacao eq formacao }
                        .map { it.toPlayer() }
                }

                if (players.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound, "Nenhum jogador encontrado para a posição $formacao.")
                } else {
                    call.respond(players)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Erro ao buscar jogadores por posição.")
            }
        }

        get("/foot/{melhorPe}") {
            try {
                val melhorPe = call.parameters["melhorPe"]
                if (melhorPe.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest, "Melhor pé é obrigatório.")
                    return@get
                }

                val players = transaction {
                    Players.select { Players.melhorPe eq melhorPe }
                        .map { it.toPlayer() }
                }

                if (players.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound, "Nenhum jogador encontrado com o pé $melhorPe.")
                } else {
                    call.respond(players)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Erro ao buscar jogadores por pé.")
            }
        }

        get("/club/{clubeAtual}") {
            try {
                val clubeAtual = call.parameters["clubeAtual"]
                if (clubeAtual.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest, "Nome do clube é obrigatório.")
                    return@get
                }

                val players = transaction {
                    Players.select { Players.clubeAtual eq clubeAtual }
                        .map { it.toPlayer() }
                }

                if (players.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound, "Nenhum jogador encontrado para o clube $clubeAtual.")
                } else {
                    call.respond(players)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Erro ao buscar jogadores do clube.")
            }
        }
    }
}
