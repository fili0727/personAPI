package kea.exercise.person.service;

import kea.exercise.person.dto.AgeResponse;
import kea.exercise.person.dto.GenderResponse;
import kea.exercise.person.dto.NationalityResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Service
public class PersonService {

    private final WebClient webClient;

    public PersonService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Cacheable("ages")
    public Mono<AgeResponse> getPersonAge(String name) {
        String uri = UriComponentsBuilder.fromHttpUrl("https://api.agify.io/")
                .queryParam("name", name)
                .toUriString();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(AgeResponse.class);
    }

    @Cacheable("nationalities")
    public Mono<NationalityResponse> getPersonNationality(String name) {
        String uri = UriComponentsBuilder.fromHttpUrl("https://api.nationalize.io/")
                .queryParam("name", name)
                .toUriString();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(NationalityResponse.class);
    }

    @Cacheable("genders")
    public Mono<GenderResponse> getPersonGender(String name) {
        String uri = UriComponentsBuilder.fromHttpUrl("https://api.genderize.io/")
                .queryParam("name", name)
                .toUriString();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(GenderResponse.class);
    }
}