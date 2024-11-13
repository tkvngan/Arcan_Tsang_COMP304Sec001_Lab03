package net.skycast.application

interface UseCase<Input, Output> {
    suspend operator fun invoke(input: Input): Output
}