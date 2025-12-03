import com.example.ApplicationKt.DeviceTokens
import com.example.DeviceToken
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import java.time.LocalDateTime

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class DeviceTokenRepository {
// fun saveToken(deviceToken: DeviceToken) = transaction {
//     val updated = DeviceTokens.update({ DeviceTokens.userId eq deviceToken.userId }) {
//         it[token] = deviceToken.token
//         it[updatedAt] = System.currentTimeMillis()
//     }

//     if (updated == 0) {
//         DeviceTokens.insert {
//             it[userId] = deviceToken.userId
//             it[token] = deviceToken.token
//             it[updatedAt] = System.currentTimeMillis()
//         }
//     }
// }



        fun saveToken(dt: DeviceToken) {
        transaction {
            // Check if this user already saved a token
            val existingUser = DeviceTokens.select { DeviceTokens.userId eq dt.userId }
                .singleOrNull()

            if (existingUser == null) {
                println("ℹ️ NEW USER → saving token: ${dt.token}")
                DeviceTokens.insert {
                    it[userId] = dt.userId
                    it[token] = dt.token
                    it[updatedAt] = System.currentTimeMillis()
                }
            } else {
                println("ℹ️ EXISTING USER → updating token: ${dt.token}")
                DeviceTokens.update({ DeviceTokens.userId eq dt.userId }) {
                    it[token] = dt.token            // update token
                    it[updatedAt] = System.currentTimeMillis()
                }
            }
        }
    }

    

    fun getToken(userId: String): String? = transaction {
        DeviceTokens
            .select { DeviceTokens.userId eq userId }
            .singleOrNull()
            ?.get(DeviceTokens.token)
    }

    fun removeToken(userId: String) = transaction {
        DeviceTokens.deleteWhere { DeviceTokens.userId eq userId }
    }

    // fun getAllTokens(): List<DeviceToken> = transaction {
    //     DeviceTokens.selectAll().map {
    //         DeviceToken(
    //             userId = it[DeviceTokens.userId],
    //             token = it[DeviceTokens.token],
    //             updatedAt = it[DeviceTokens.updatedAt]
    //         )
    //     }
    // }


        fun getAllTokens(): List<DeviceToken> = transaction {
            DeviceTokens.selectAll().map {
                DeviceToken(
                    userId = it[DeviceTokens.userId],
                    token = it[DeviceTokens.token],
                    updatedAt = it[DeviceTokens.updatedAt]
                )
            }
        }


    fun cleanupDuplicateTokens() {
        transaction {
            val all = DeviceTokens.selectAll().toList()

            val grouped = all.groupBy { it[DeviceTokens.userId] }

            for ((userId, rows) in grouped) {
                if (rows.size > 1) {
                    // Keep the most recent row based on updatedAt
                    val sorted = rows.sortedByDescending { it[DeviceTokens.updatedAt] }
                    val keep = sorted.first()
                    val deleteOthers = sorted.drop(1)

                    println("🧹 Removing ${deleteOthers.size} duplicate tokens for userId=$userId")

                    deleteOthers.forEach { row ->
                        DeviceTokens.deleteWhere {
                            DeviceTokens.userId eq row[DeviceTokens.userId] and
                                    (DeviceTokens.token eq row[DeviceTokens.token])
                        }
                    }
                }
            }
        }
    }




    
}

