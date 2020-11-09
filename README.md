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
![]()

## Ekstra leveranse utover minimumskravene
- [x] Håndtering av request target "/"
- [x] Avansert datamodell (mer enn 3 tabeller)
- [x] Avansert funksjonalitet (redigering av prosjektmedlemmer, statuskategorier, prosjekter)
- [ ] Implementasjon av cookies for å konstruere sesjoner
- [ ] UML diagram som dokumenterer datamodell og/eller arkitektur (presentert i README.md)
- [x] Rammeverk rundt Http-håndtering (en god HttpMessage class med HttpRequest og HttpResponse subtyper) som gjenspeiler RFC7230
- [x] God bruk av DAO-pattern
- [x] God bruk av Controller-pattern
- [x] Korrekt håndtering av norske tegn i HTTP
- [x] Link til video med god demonstrasjon av ping-pong programmering
- [x] Automatisk rapportering av testdekning i Github Actions
- [ ] Implementasjon av Chunked Transfer Encoding
- [x] Annet


## Egenevaluering

### Erfaringene med arbeidet og løsningen

Vi startet faget med å splitte kohorten i 2 og 2. Vi tenkte det var bedre å jobbe med mindre grupper gjennom arbeidskravene. Etter siste arbeidskrav syntes vi det begynte å bli vanskeligere og tenkte at det er best med alle på kohorten sammen. Vi slo derfor sammen hele kohorten igjen for å fullføre eksamen sammen. Måten vi gjorde det på var at en programmerte mens alle så på Discord og kunne komme med innspill. 
Det fungerte godt for gruppen og samles på discord for så og jobbe virituelt på en maskin. Dette gjorde at vi raskere kom fram til løsninger, og på den måten jobbet mer effektivt en om vi satt med hver vår oppgave til enhver tid. Det var for oss bedre at 4 hoder tenkte på det samme problemet en at man sitter med det helt alene og da fortsatt måtte ha spurt de andre i gruppen om hjelp. 

### Hva har vi lært

Før vi begynte undervisning i dette faget, kunne java til tider virke litt "skremmende". Vi ble raskt kastet på dypt vann, men det var hele tiden en reddningsbøyle i nærhetem for å forhinrde at vi "druknet". Etter dette semesteret er det fortsatt en smule fryktinngytende, men vi føler at vi nå bedre kan manipulere java-kode slik vi vil. En morsom teknikk vi har lært under fagetsløp er parrprogrammering. Dette har vi benyttet oss av i stor grad under hele perioden, da det ga oss gode resultater, raskere.  