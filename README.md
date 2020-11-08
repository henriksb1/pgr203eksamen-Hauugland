![Java CI with Maven](https://github.com/kristiania/pgr203eksamen-Hauugland/workflows/Java%20CI%20with%20Maven/badge.svg)

## Bygge, konfigurere og kjøre løsningen

1) Kjør Maven -> Lifecycle -> clean for å fjerne /target. Kjør package deretter for å
bygge prosjektet og opprette .jar fil som kan kjøres 
2) Det må lages en konfigurasjonsfil som heter pgr203.properties, som må inneholde følgende:
``` properties
    dataSource.url=...
    dataSource.username=...
    dataSource.password=...
```

Eksempel:
``` properties
    dataSource.url=jdbc:postgresql://localhost:5432/eksamen
    dataSource.username=eksamen
    dataSource.password=e761@dasu72-234ksja
```

## Designet på løsningen

## Ekstra leveranse utover minimumskravene
- [x] Håndtering av request target "/"
- [ ] Avansert datamodell (mer enn 3 tabeller)
- [ ] Avansert funksjonalitet (redigering av prosjektmedlemmer, statuskategorier, prosjekter)
- [ ] Implementasjon av cookies for å konstruere sesjoner
- [ ] UML diagram som dokumenterer datamodell og/eller arkitektur (presentert i README.md)
- [ ] Rammeverk rundt Http-håndtering (en god HttpMessage class med HttpRequest og HttpResponse subtyper) som gjenspeiler RFC7230
- [ ] God bruk av DAO-pattern
- [ ] God bruk av Controller-pattern
- [x] Korrekt håndtering av norske tegn i HTTP
- [ ] Link til video med god demonstrasjon av ping-pong programmering
- [ ] Automatisk rapportering av testdekning i Github Actions
- [x] Implementasjon av Chunked Transfer Encoding
- [ ] Annet


## Egenevaluering