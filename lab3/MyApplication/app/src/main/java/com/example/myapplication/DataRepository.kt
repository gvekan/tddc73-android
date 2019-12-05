package com.example.myapplication
import GetRepositoriesQuery
import GetRepositoryQuery
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.exception.ApolloException
import com.example.myapplication.ui.main.MainViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import type.SearchType
import java.lang.Exception
import java.util.concurrent.TimeUnit


/**
 * Used to do specific data fetches with GraphQL from GitHub.
 */
class DataRepository {

    fun getMostStarredRepositories(onSuccess: (List<MainViewModel.RepositoryItem>) -> Unit, lang: String = "") {
        var query = "stars:>100 sort:stars-desc"
        if (lang.isNotEmpty()) {
            query += " language:$lang"
        }
        val queryCall = GetRepositoriesQuery
            .builder()
            .query(query)
            .first(25)
            .type(SearchType.REPOSITORY)
            .build()

        apolloClient.query(queryCall).enqueue(object: ApolloCall.Callback<GetRepositoriesQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                throw e // Should do something better with the exception
            }

            override fun onResponse(response: com.apollographql.apollo.api.Response<GetRepositoriesQuery.Data>) {
                val errors = response.errors()
                if (errors.isEmpty()){
                    val edges = response.data()?.search()?.edges() ?: listOf()
                    val repositories = mutableListOf<MainViewModel.RepositoryItem>()
                    edges.forEach {
                        val repo = it.node()
                        if (repo is GetRepositoriesQuery.AsRepository) {
                            repositories.add(MainViewModel.RepositoryItem(
                                repo.name(),
                                repo.owner().login(),
                                repo.description(),
                                repo.forks().totalCount(),
                                repo.stargazers().totalCount()
                            ))
                        }
                    }
                    onSuccess(repositories)
                }
            }
        })
    }

    fun getRepository(onSuccess: (MainViewModel.RepositoryDetail) -> Unit, owner: String, name: String) {
        val queryCall = GetRepositoryQuery
            .builder()
            .owner(owner)
            .name(name)
            .build()

        apolloClient.query(queryCall).enqueue(object: ApolloCall.Callback<GetRepositoryQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                throw e // Should do something better with the exception
            }

            override fun onResponse(response: com.apollographql.apollo.api.Response<GetRepositoryQuery.Data>) {
                val errors = response.errors()
                if (errors.isEmpty()){
                    val repo = response.data()?.repository()
                    if (repo is GetRepositoryQuery.Repository) {
                        var commits = 0
                        var branches = 0
                        val target = repo.defaultBranchRef()?.target()
                        if (target is GetRepositoryQuery.AsCommit)
                            commits = target.history().totalCount()
                         repo.refs()?.let { r ->
                             branches = r.totalCount()
                         }

                        onSuccess(MainViewModel.RepositoryDetail(
                                repo.description(),
                                repo.licenseInfo()?.name(),
                                commits,
                                branches
                        ))
                    }
                }
            }
        })
    }

    companion object {

        private val GITHUB_GRAPHQL_ENDPOINT = "https://api.github.com/graphql"

        private val httpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .addNetworkInterceptor(NetworkInterceptor())
                .build()
        }


        private val apolloClient: ApolloClient by lazy {
            ApolloClient.builder()
                .serverUrl(GITHUB_GRAPHQL_ENDPOINT)
                .okHttpClient(httpClient)
                .build()
        }

        private class NetworkInterceptor: Interceptor {

            override fun intercept(chain: Interceptor.Chain?): Response {
                return chain!!.proceed(chain.request().newBuilder().header("Authorization", "Bearer 5f6274df74b3ab2be1447b34a196ce581f77872a").build()) // So good to store token here
            }
        }
    }
}