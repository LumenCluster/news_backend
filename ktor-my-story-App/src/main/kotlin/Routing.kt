package com.example
import com.example.ApplicationKt.Notifications
import com.example.ApplicationKt.Articles
import com.example.ApplicationKt.Articles.category
import com.example.ApplicationKt.Articles.name
import com.example.ApplicationKt.Categories
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less

fun Application.configureRouting() {
    routing {
        get("/") {
            val homeArticles = transaction {
                Articles.selectAll()
                    .orderBy(Articles.createdAt, SortOrder.DESC)
                    .limit(10)
                    .map {
                        Article(
                            id = it[Articles.id],
                            title = it[Articles.title],
                            content = it[Articles.content],
                            name = it[Articles.name],
                            category = it[Articles.category].value,
                            imageUrl = it[Articles.imageUrl],
                            createdAt = it[Articles.createdAt],
                            author = it[Articles.author],
                            views = it[Articles.views]
                        )
                    }
            }

            call.respond(homeArticles)
        }


        get("/articles/categories") {
//         val categoryParam = call.parameters["categories"]
//             ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing category")
//
//         val categorie = categoryParam.toIntOrNull()
//             ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid category ID")
            val categories = transaction {
                Categories.selectAll().map {
                    Category(
                        it[Categories.id].value,
                        it[Categories.name]
                    )
                }
            }
            call.respond(categories)
        }

        // Get All Categories
        get("/categories") {
            val categories = transaction {
                Categories.selectAll().map {
                    Category(
                        it[Categories.id].value,
                        it[Categories.name]
                    )
                }
            }
            call.respond(categories)
        }

        // Get articles Category by name
       get("/articles/categories/{name}")
        // get("/categories/{name}") 
        {
            val name = call.parameters["name"]?.lowercase()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid category")

            val category = transaction {
                Categories.select { LowerCase (Categories.name) eq name }
                    .map { Category(it[Categories.id].value, it[Categories.name]) }
                    .singleOrNull()
            } ?: return@get call.respond(HttpStatusCode.NotFound, "Category not found")

            call.respond(category)
        }
//        // Get articles Category by name
//     get("/articles/{name}") {
//         val name = call.parameters["name"]?.lowercase()
//             ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid category")
//
//         val category = transaction {
//             Categories.select { LowerCase (Categories.name) eq name }
//                 .map { Category(it[Categories.id].value, it[Categories.name]) }
//                 .singleOrNull()
//         } ?: return@get call.respond(HttpStatusCode.NotFound, "Category not found")
//
//         call.respond(category)
//     }
        // Get Category by ID
        get("/categories/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid ID")

            val category = transaction {
                Categories.select { Categories.id eq id }
                    .map { Category(it[Categories.id].value, it[Categories.name]) }
                    .singleOrNull()
            } ?: return@get call.respond(HttpStatusCode.NotFound, "Category not found")

            call.respond(category)
        }

        // Create Category
        post("/categories") {
            val category = call.receive<Category>()

            val categoryId = transaction {
                Categories.insertAndGetId {
                    it[name] = category.name
                }.value
            }

            call.respond(HttpStatusCode.Created, "Category added with ID $categoryId")
        }

        // Update Category
        put("/categories/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid ID")

            val category = call.receive<Category>()

            val updatedRows = transaction {
                Categories.update({ Categories.id eq id }) {
                    it[name] = category.name
                }
            }

            if (updatedRows == 0) {
                call.respond(HttpStatusCode.NotFound, "Category not found")
            } else {
                call.respond(HttpStatusCode.OK, "Category updated successfully")
            }
        }

        // Delete Category
        delete("/categories/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid ID")

            val deletedRows = transaction {
                Categories.deleteWhere { Categories.id eq id }
            }

            if (deletedRows == 0) {
                call.respond(HttpStatusCode.NotFound, "Category not found")
            } else {
                call.respond(HttpStatusCode.OK, "Category deleted successfully")
            }
        }

//        post("/articles") {
//            val article = call.receive<com.example.Article>()
//
//            org.jetbrains.exposed.sql.transactions.transaction {
//                com.example.ApplicationKt.Articles.insert {
//                    it[title] = article.title
//                    it[com.example.ApplicationKt.Articles.content] = article.content
//                    it[com.example.ApplicationKt.Articles.name] = article.name
//                    it[com.example.ApplicationKt.Articles.category] = article.category
//                    it[com.example.ApplicationKt.Articles.imageUrl] = article.imageUrl ?: ""
//                    it[com.example.ApplicationKt.Articles.createdAt] = java.lang.System.currentTimeMillis()
//                    it[com.example.ApplicationKt.Articles.author] = article.author
//                    it[com.example.ApplicationKt.Articles.views] = article.views
//
//
//                }
//            }
//            call.respond(io.ktor.http.HttpStatusCode.Created, "Article added")
//
//
//            // Fetch latest articles
//            get("/articles/latest") {
//                val latestArticles = transaction {
//                    com.example.ApplicationKt.Articles
//                        .selectAll()
//                        .orderBy(
//                            com.example.ApplicationKt.Articles.createdAt,
//                            SortOrder.DESC
//                        )
//                        .limit(10)
//                        .map {
//                            com.example.Article(
//                                it[com.example.ApplicationKt.Articles.id],
//                                it[Articles.title],
//                                it[com.example.ApplicationKt.Articles.content],
//                                it[com.example.ApplicationKt.Articles.name],
//                                it[com.example.ApplicationKt.Articles.category].value,
//                                it[com.example.ApplicationKt.Articles.imageUrl],
//                                it[com.example.ApplicationKt.Articles.createdAt],
//                                it[com.example.ApplicationKt.Articles.author],
//                                it[com.example.ApplicationKt.Articles.views],
//
//                                )
//                        }
//                }
//                call.respond(latestArticles)
//            }
//
//// Fetch trending articles
//            get("/articles/trending") {
//                val trendingArticles = transaction {
//                    com.example.ApplicationKt.Articles
//                        .selectAll()
//                        .orderBy(com.example.ApplicationKt.Articles.views, SortOrder.DESC)
//                        .limit(10)
//                        .map {
//                            com.example.Article(
//                                it[com.example.ApplicationKt.Articles.id],
//                                it[Articles.title],
//                                it[com.example.ApplicationKt.Articles.content],
//                                it[com.example.ApplicationKt.Articles.name],
//                                it[com.example.ApplicationKt.Articles.category].value,
//                                it[com.example.ApplicationKt.Articles.imageUrl],
//                                it[com.example.ApplicationKt.Articles.createdAt],
//                                it[com.example.ApplicationKt.Articles.author],
//                                it[com.example.ApplicationKt.Articles.views],
//
//                                )
//                        }
//                }
//                call.respond(trendingArticles)
//            }
//
//        }






        route("/articles") {

            // Create a new article
            // post {
            //     val article = call.receive<com.example.Article>()

            //     transaction {
            //         com.example.ApplicationKt.Articles.insert {
            //             it[title] = article.title
            //             it[com.example.ApplicationKt.Articles.content] = article.content
            //             it[com.example.ApplicationKt.Articles.name] = article.name
            //             it[com.example.ApplicationKt.Articles.category] = article.category
            //             it[com.example.ApplicationKt.Articles.imageUrl] = article.imageUrl ?: ""
            //             it[com.example.ApplicationKt.Articles.createdAt] = System.currentTimeMillis()
            //             it[com.example.ApplicationKt.Articles.author] = article.author
            //             it[com.example.ApplicationKt.Articles.views] = article.views
            //         }
            //     }

            //     call.respond(HttpStatusCode.Created, "Article added")
            // }

                        post {
                val article = call.receive<Article>()

                   transaction {
                   Articles.insert{
                        it[title] = article.title
                        it[content] = article.content
                        it[name] = article.name
                        it[category] = article.category
                        it[imageUrl] = article.imageUrl ?: ""
                        it[createdAt] = System.currentTimeMillis()
                        it[author] = article.author
                        it[views] = article.views
                    }
                }

                transaction {
                    Notifications.insert {
                        it[title] = article.title
                        it[name] = article.name
                        it[imageUrl] = article.imageUrl ?: ""
                        it[author] = article.author
                        it[isRead] = false
                        it[createdAt] = System.currentTimeMillis()
                    }
                }

                call.respond(HttpStatusCode.Created, "New Article and Notification added")
            }





            //  Fetch latest articles
            get("/latest") {
                val latestArticles = transaction {
                    com.example.ApplicationKt.Articles
                        .selectAll()
                        .orderBy(com.example.ApplicationKt.Articles.createdAt, SortOrder.DESC)
                        .limit(10)
                        .map {
                            com.example.Article(
                                it[com.example.ApplicationKt.Articles.id],
                                it[com.example.ApplicationKt.Articles.title],
                                it[com.example.ApplicationKt.Articles.content],
                                it[com.example.ApplicationKt.Articles.name],
                                it[com.example.ApplicationKt.Articles.category].value,
                                it[com.example.ApplicationKt.Articles.imageUrl],
                                it[com.example.ApplicationKt.Articles.createdAt],
                                it[com.example.ApplicationKt.Articles.author],
                                it[com.example.ApplicationKt.Articles.views]
                            )
                        }
                }

                call.respond(latestArticles)
            }

            //Fetch trending articles
            get("/trending") {
                val trendingArticles = transaction {
                    com.example.ApplicationKt.Articles
                        .selectAll()
                        .orderBy(com.example.ApplicationKt.Articles.views, SortOrder.DESC)
                        .limit(10)
                        .map {
                            com.example.Article(
                                it[com.example.ApplicationKt.Articles.id],
                                it[com.example.ApplicationKt.Articles.title],
                                it[com.example.ApplicationKt.Articles.content],
                                it[com.example.ApplicationKt.Articles.name],
                                it[com.example.ApplicationKt.Articles.category].value,
                                it[com.example.ApplicationKt.Articles.imageUrl],
                                it[com.example.ApplicationKt.Articles.createdAt],
                                it[com.example.ApplicationKt.Articles.author],
                                it[com.example.ApplicationKt.Articles.views]
                            )
                        }
                }

                call.respond(trendingArticles)
            }
        }

        get("/notifications") {
            val sevenDaysAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L)
//            val fifteenMinutesAgo = System.currentTimeMillis() - (15 * 60 * 1000L)
            val thirtyDaysAgo = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000)


            // Delete notifications older than 15 minutes (for testing)
            transaction {
                Notifications.deleteWhere { Notifications.createdAt less thirtyDaysAgo}
            }

            // Fetch notifications from last 7 days, sorted by latest
            val notifications = transaction {
                Notifications
                    .select { Notifications.createdAt greaterEq sevenDaysAgo }
                    .orderBy(Notifications.createdAt, SortOrder.DESC)
                    .map {
                        Notification(
                            id = it[Notifications.id].value,
                            title = it[Notifications.title],
                            name = it[Notifications.name],
                            imageUrl = it[Notifications.imageUrl],
                            author = it[Notifications.author],
                            isRead = it[Notifications.isRead],
                            createdAt = it[Notifications.createdAt],
                            views = it[Notifications.views]
                        )
                    }
            }

            call.respond(notifications)
        }


        // get("/notifications") {
        //     val notifications = transaction {
        //         Notifications.selectAll()
        //             .orderBy(Notifications.createdAt, SortOrder.DESC)
        //             .map {
        //                 Notification(
        //                     id = it[Notifications.id].value,
        //                     title = it[Notifications.title],
        //                     name = it[Notifications.name],
        //                     imageUrl = it[Notifications.imageUrl],
        //                     author = it[Notifications.author],
        //                     isRead = it[Notifications.isRead],
        //                     createdAt = it[Notifications.createdAt]
        //                 )
        //             }
        //     }

        //     call.respond(notifications)
        // }










        // Get All Articles
//        get("/articles") {
//            val articles = transaction {
//                Articles.selectAll().map {
//                    Article(
//                        it[Articles.id],
//                        it[Articles.title],
//                        it[Articles.content],
//                        it[Articles.category].value, // Extract Int value
//                        it[Articles.imageUrl],
//                        it[Articles.createdAt]
//                    )
//                }
//            }
//            call.respond(articles)
//        }

        // Get Articles by Category
//        get("/articles/{category}")
//        {
//            val categoryId = call.parameters["name"]?.toIntOrNull()
//                ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid category ID")
//
//            val articles = transaction {
//                Articles.select { Articles.name eq name }
//                    .map {
//                        Article(
//                            it[Articles.id],
//                            it[Articles.title],
//                            it[Articles.content],
//                            it[Articles.name],
//                            it[Articles.category].value, // Extract Int value
//                            it[Articles.imageUrl],
//                            it[Articles.createdAt]
//                        )
//                    }
//            }
//            call.respond(articles)
//        }


        // Get All Articles
        get("/articles") {
            val articles = org.jetbrains.exposed.sql.transactions.transaction {
                com.example.ApplicationKt.Articles.selectAll().map {
                    it[com.example.ApplicationKt.Articles.createdAt].let { it1 ->
                        com.example.Article(
                            it[com.example.ApplicationKt.Articles.id],
                            it[Articles.title],
                            it[com.example.ApplicationKt.Articles.content],
                            it[com.example.ApplicationKt.Articles.name],
                            it[com.example.ApplicationKt.Articles.category].value,
                            it[com.example.ApplicationKt.Articles.imageUrl],
                            it1,
                            it[com.example.ApplicationKt.Articles.author],
                            it[com.example.ApplicationKt.Articles.views],

                            )
                    }
                }
            }
            call.respond(articles) // TypeInfo no longer needed
        }


        // Add New Article
//        post("/articles") {
//            val article = call.receive<Article>()
//
//            transaction {
//                Articles.insert {
////                    it[id] = article.id
//                    it[title] = article.title
//                    it[content] = article.content
//                    it[category] = article.category // Direct assignment
//                    it[imageUrl] = article.imageUrl ?: ""
//                    it[createdAt] = System.currentTimeMillis()
//                }
//            }
//            call.respond(HttpStatusCode.Created, "Article added")
//        }

        // Update Article
        put("/articles/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid ID")

            val article = call.receive<Article>()

            val updatedRows = transaction {
                Articles.update({ Articles.id eq id }) {
                    it[title] = article.title
                    it[content] = article.content
                    it[name] = article.name
                    it[category] = article.category // Direct assignment
                    it[imageUrl] = article.imageUrl ?: ""
                    it[createdAt] = article.createdAt
                    it[author] = article.author
                    it[views] = article.views

                }
            }

            if (updatedRows == 0) {
                call.respond(HttpStatusCode.NotFound, "Article not found")
            } else {
                call.respond(HttpStatusCode.OK, "Article updated successfully")
            }
        }

        // Delete Article
        delete("/articles/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid ID")

            val deletedRows = transaction {
                Articles.deleteWhere { Articles.id eq id }
            }

            if (deletedRows == 0) {
                call.respond(HttpStatusCode.NotFound, "Article not found")
            } else {
                call.respond(HttpStatusCode.OK, "Article deleted successfully")
            }
        }

//        get("/articles/{id}") {
//            val idParam = call.parameters["id"]?.toIntOrNull()
//                ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing article ID")
//
//            val article = transaction {
//                val result = com.example.ApplicationKt.Articles
//                    .select { com.example.ApplicationKt.Articles.id eq idParam }
//                    .singleOrNull()
//
//                // If article is found, increment views
//                result?.let {
//                    com.example.ApplicationKt.Articles.update({ com.example.ApplicationKt.Articles.id eq idParam }) {
//                        with(SqlExpressionBuilder) {
//                            it.update(views, views + 1) // Increment views properly
//                        }
//                    }
////
//                    com.example.Article(
//                        it[com.example.ApplicationKt.Articles.id],
//                        it[com.example.ApplicationKt.Articles.title],
//                        it[com.example.ApplicationKt.Articles.content],
//                        it[com.example.ApplicationKt.Articles.name],
//                        it[com.example.ApplicationKt.Articles.category].value,
//                        it[com.example.ApplicationKt.Articles.imageUrl],
//                        it[com.example.ApplicationKt.Articles.createdAt],
//                        it[com.example.ApplicationKt.Articles.author],
//                        it[com.example.ApplicationKt.Articles.views]
//                    )
//                }
//            }
//
//            article?.let {
//                call.respond(HttpStatusCode.OK, it)
//            } ?: call.respond(HttpStatusCode.NotFound, "Article not found")
//        }

        get("/articles/{id}") {
            val idParam = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing article ID")

            val article = transaction {
                val exists = com.example.ApplicationKt.Articles
                    .select { com.example.ApplicationKt.Articles.id eq idParam }
                    .singleOrNull()

                if (exists != null) {
                    // Increment views
                    com.example.ApplicationKt.Articles.update({ com.example.ApplicationKt.Articles.id eq idParam }) {
                        with(SqlExpressionBuilder) {
                            it.update(views, views + 1)
                        }
                    }

                    // Fetch the updated article
                    com.example.ApplicationKt.Articles
                        .select { com.example.ApplicationKt.Articles.id eq idParam }
                        .map {
                            com.example.Article(
                                it[com.example.ApplicationKt.Articles.id],
                                it[com.example.ApplicationKt.Articles.title],
                                it[com.example.ApplicationKt.Articles.content],
                                it[com.example.ApplicationKt.Articles.name],
                                it[com.example.ApplicationKt.Articles.category].value,
                                it[com.example.ApplicationKt.Articles.imageUrl],
                                it[com.example.ApplicationKt.Articles.createdAt],
                                it[com.example.ApplicationKt.Articles.author],
                                it[com.example.ApplicationKt.Articles.views]
                            )
                        }.firstOrNull()
                } else null
            }

            article?.let {
                call.respond(HttpStatusCode.OK, it)
            } ?: call.respond(HttpStatusCode.NotFound, "Article not found")
        }



        // Get Articles by Category
//        get("/articles/name/{categoryName}")
        get("/{categoryName}/articles")

        {
            val categoryName = call.parameters["categoryName"]
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing category name")

            val articles = transaction {
                (com.example.ApplicationKt.Articles innerJoin com.example.ApplicationKt.Categories)
                    .select { com.example.ApplicationKt.Categories.name eq categoryName }
                    .map {
                        com.example.Article(
                            it[com.example.ApplicationKt.Articles.id],
                            it[Articles.title],
                            it[com.example.ApplicationKt.Articles.content],
                            it[com.example.ApplicationKt.Categories.name], // Fetch category name
                            it[com.example.ApplicationKt.Articles.category].value,
                            it[com.example.ApplicationKt.Articles.imageUrl],
                            it[com.example.ApplicationKt.Articles.createdAt],
                            it[com.example.ApplicationKt.Articles.author],
                            it[com.example.ApplicationKt.Articles.views],

                            )
                    }
            }

            call.respond(articles)
        }

// Search Articles
//        get("/articles/search") {
//            val query = call.request.queryParameters["q"]?.lowercase()
//                ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing search query")
//
//            val articles = transaction {
//                com.example.ApplicationKt.Articles
//                    .select {
//                        (LowerCase(Articles.title) like "%$query%") or
//                                (LowerCase (Articles.content) like "%$query%")
//                    }
//                    .map {
//                        com.example.Article(
//                            it[com.example.ApplicationKt.Articles.id],
//                            it[com.example.ApplicationKt.Articles.title],
//                            it[com.example.ApplicationKt.Articles.content],
//                            it[com.example.ApplicationKt.Articles.name],
//                            it[com.example.ApplicationKt.Articles.category].value,
//                            it[com.example.ApplicationKt.Articles.imageUrl],
//                            it[com.example.ApplicationKt.Articles.createdAt],
//                            it[com.example.ApplicationKt.Articles.author],
//                            it[com.example.ApplicationKt.Articles.views],
//                        )
//                    }
//            }
//
//            if (articles.isEmpty()) {
//                call.respond(HttpStatusCode.NotFound, "No articles found")
//            }
//            else {
//                call.respond(articles)
//            }
//        }

        get("/articles/search") {
            val query = call.request.queryParameters["q"]?.lowercase()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing search query")

            val categoryName = call.request.queryParameters["category"]?.lowercase()

            val categoryId = if (categoryName != null) {
                transaction {
                    Categories
                        .select { LowerCase(Categories.name) eq categoryName }
                        .map { it[Categories.id].value }
                        .singleOrNull()
                }
            } else null

            val articles = transaction {
                Articles
                    .select {
                        ((LowerCase(Articles.title) like "%$query%") or
                                (LowerCase(Articles.content) like "%$query%")) and
                                (if (categoryId != null) Articles.category eq categoryId else Op.TRUE)
                    }
                    .map {
                        Article(
                            id = it[Articles.id],
                            title = it[Articles.title],
                            content = it[Articles.content],
                            name = it[Articles.name],
                            category = it[Articles.category].value,
                            imageUrl = it[Articles.imageUrl],
                            createdAt = it[Articles.createdAt],
                            author = it[Articles.author],
                            views = it[Articles.views]
                        )
                    }
            }

            if (articles.isEmpty()) {
                call.respond(HttpStatusCode.NotFound, "No articles found")
            } else {
                call.respond(articles)
            }
        }



//     get("/articles/search") {
//         val query = call.request.queryParameters["q"] // Keyword search
//         val category = call.request.queryParameters["category"] // Category filter
//
//         val articles = transaction {
//             com.example.ApplicationKt.Articles
//                 .select {
//                     // Create a base condition
//                     var condition: Op<Boolean> = Op.TRUE
//
//                     // If "q" is provided, search by title or content (partial match)
//                     if (!query.isNullOrBlank()) {
//                         condition = condition and ((Articles.title like "%$query%") or
//                                 (Articles.content like "%$query%"))
//                     }
//
//                     // If "category" is provided, filter by category (Ensure correct data type)
//                     if (!category.isNullOrBlank()) {
////                         condition = condition and (Articles.category eq category)
//                         condition = condition and (Articles.category eq (Categories.name))// name is category name
//
//                     }
//
//                     condition
//                 }
//                 .map {
//                     com.example.Article(
//                         it[Articles.id],
//                         it[Articles.title],
//                         it[Articles.content],
//                         it[Articles.name],
//                         it[Articles.category].value, // If category is an enum, use `.value`
//                         it[Articles.imageUrl],
//                         it[Articles.createdAt],
//                         it[Articles.author],
//                         it[Articles.views],
//                     )
//                 }
//         }
//
//         if (articles.isEmpty()) {
//             call.respond(HttpStatusCode.NotFound, "No articles found")
//         } else {
//             call.respond(articles)
//         }
//     }



        get("/category/{name}/articles") {
            val categoryName = call.parameters["name"]?.lowercase()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing or invalid category name")

            val category = transaction {
                Categories
                    .selectAll()
                    .map { it[Categories.name].lowercase() to it[Categories.id].value }
                    .toMap()[categoryName]

//                Categories
//                    .select { LowerCase(Categories.name) eq categoryName }
//                    .map { it[Categories.id].value }
//                    .singleOrNull()
            } ?: return@get call.respond(HttpStatusCode.NotFound, "Category not found")

            val articles = transaction {
                Articles
                    .select { Articles.category eq category }
                    .map {
                        Article(
                            id = it[Articles.id],
                            title = it[Articles.title],
                            content = it[Articles.content],
                            name = it[Articles.name],
                            category = it[Articles.category].value,
                            imageUrl = it[Articles.imageUrl],
                            createdAt = it[Articles.createdAt],
                            author = it[Articles.author],
                            views = it[Articles.views]
                        )
                    }
            }

            if (articles.isEmpty()) {
                call.respond(HttpStatusCode.NotFound, "No articles found for this category")
            } else {
                call.respond(articles)
            }
        }


    }


}


