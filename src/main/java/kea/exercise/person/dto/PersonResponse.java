package kea.exercise.person.dto;

public record PersonResponse(String name, int age,
                             String gender, double genderProbability,
                             String country, double probability) {
}