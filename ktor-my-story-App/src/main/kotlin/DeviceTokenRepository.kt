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
fun saveToken(deviceToken: DeviceToken) = transaction {
    val updated = DeviceTokens.update({ DeviceTokens.userId eq deviceToken.userId }) {
        it[token] = deviceToken.token
        it[updatedAt] = System.currentTimeMillis()
    }

    if (updated == 0) {
        DeviceTokens.insert {
            it[userId] = deviceToken.userId
            it[token] = deviceToken.token
            it[updatedAt] = System.currentTimeMillis()
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

    fun getAllTokens(): List<DeviceToken> = transaction {
        DeviceTokens.selectAll().map {
            DeviceToken(
                userId = it[DeviceTokens.userId],
                token = it[DeviceTokens.token],
                updatedAt = it[DeviceTokens.updatedAt]
            )
        }
    }



}

