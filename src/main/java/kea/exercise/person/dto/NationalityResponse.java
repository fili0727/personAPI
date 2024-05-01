package kea.exercise.person.dto;

import java.util.List;

public record NationalityResponse(String name, int count, List<CountryItem> country) {
}