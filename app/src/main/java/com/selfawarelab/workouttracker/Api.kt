package com.selfawarelab.workouttracker

import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


// https://c2ftig9uik.execute-api.us-west-1.amazonaws.com/default/lambda-microservice?TableName=exerciseTypes

object Api {
    val api by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://c2ftig9uik.execute-api.us-west-1.amazonaws.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()

        retrofit.create(AwsApi::class.java)
    }
}

interface AwsApi {
    @GET("default/lambda-microservice?TableName=exerciseTypes")
    fun getExerciseTypes(): Single<AwsTableResponse<ExerciseType>>

    @POST("default/lambda-microservice")
    fun addWorkoutDay(@Body workoutDayRequest: WorkoutDayRequest): Single<AwsTableResponse<WorkoutDay>>
}

class AwsTableResponse<T>(@SerializedName("Items") val list: List<T>)

class WorkoutDayRequest(
    @SerializedName("TableName") val tableName: String = "lambda-dynamodb-stream",
    @SerializedName("Item") val item: Item = Item("44")
)

class Item(val id: String) {
    val mg: List<ExerciseType.MuscleGroup> = mutableListOf<ExerciseType.MuscleGroup>().apply {
        this.add(ExerciseType.MuscleGroup.BICEPS)
        this.add(ExerciseType.MuscleGroup.TRICEPS)
    }
}

