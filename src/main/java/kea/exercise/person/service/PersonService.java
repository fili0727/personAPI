package kea.exercise.person.service;

import kea.exercise.person.dto.AgeResponse;
import kea.exercise.person.dto.GenderResponse;
import kea.exercise.person.dto.NationalityResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class PersonService {

    private final WebClient webClient;
    private final ConcurrentHashMap<String, AgeResponse> ageCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, NationalityResponse> nationalityCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, GenderResponse> genderCache = new ConcurrentHashMap<>();


    public PersonService(WebClient webClient) {
        this.webClient = webClient;
    }


    public Mono<AgeResponse> getPersonAge(String name) {
        return Mono.justOrEmpty(ageCache.get(name))
                .switchIfEmpty(fetchAgeFromApi(name).doOnNext(response -> ageCache.put(name, response)));
    }

    private Mono<AgeResponse> fetchAgeFromApi(String name) {
        String uri = UriComponentsBuilder.fromHttpUrl("https://api.agify.io/")
                .queryParam("name", name)
                .toUriString();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(AgeResponse.class);
    }

    public Mono<NationalityResponse> getPersonNationality(String name) {
        return Mono.justOrEmpty(nationalityCache.get(name))
                .switchIfEmpty(fetchNationalityFromApi(name).doOnNext(response -> nationalityCache.put(name, response)));
    }

    private Mono<NationalityResponse> fetchNationalityFromApi(String name) {
        String uri = UriComponentsBuilder.fromHttpUrl("https://api.nationalize.io/")
                .queryParam("name", name)
                .toUriString();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(NationalityResponse.class);
    }

    public Mono<GenderResponse> getPersonGender(String name) {
        return Mono.justOrEmpty(genderCache.get(name))
                .switchIfEmpty(fetchGenderFromApi(name).doOnNext(response -> genderCache.put(name, response)));
    }

    private Mono<GenderResponse> fetchGenderFromApi(String name) {
        String uri = UriComponentsBuilder.fromHttpUrl("https://api.genderize.io/")
                .queryParam("name", name)
                .toUriString();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(GenderResponse.class);
    }

}