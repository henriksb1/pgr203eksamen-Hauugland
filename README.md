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

## Egenevaluering