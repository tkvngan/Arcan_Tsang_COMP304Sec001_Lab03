package net.skycast.application

interface UseCase<Input, Output> {

    suspend fun execute(input: Input): Output
}