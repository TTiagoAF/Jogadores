package com.example

import org.jetbrains.exposed.sql.Database

object DatabaseFactory {
    fun init() {
        Database.connect(
            url = "jdbc:mysql://localhost:3306/players",
            driver = "com.mysql.cj.jdbc.Driver",
            user = "root",
            password = ""
        )
    }
}