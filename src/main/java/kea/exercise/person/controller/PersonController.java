package kea.exercise.person.controller;

import kea.exercise.person.dto.CountryItem;
import kea.exercise.person.dto.PersonResponse;
import kea.exercise.person.service.PersonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@RestController
    @RequestMapping("/person")
    public class PersonController {
        private final PersonService personService;

        public PersonController(PersonService personService) {
            this.personService = personService;
        }


        @GetMapping

        public Mono<PersonResponse> getPersonInfo(@RequestParam String firstName, @RequestParam String lastName,
                                                  @RequestParam(required = false) String middleName) {
            String name = firstName + "+" + (middleName != null ? middleName + "+" : "") + lastName;

            return Mono.zip(
                    personService.getPersonAge(name),
                    personService.getPersonGender(name),
                    personService.getPersonNationality(name)
            ).map(tuple -> {
                List<CountryItem> nationality = tuple.getT3().country();
                CountryItem highestProbabilityCountry = nationality.stream()
                        .max(Comparator.comparing(CountryItem::countryProbability))
                        .orElse(null);
                return new PersonResponse(
                        name,
                        tuple.getT1().age(),
                        tuple.getT2().gender(),
                        tuple.getT2().probability(),
                        highestProbabilityCountry.country_id(),
                        highestProbabilityCountry.countryProbability()
                );
            });
        }


    }

