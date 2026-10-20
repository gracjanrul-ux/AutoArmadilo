# AutoArmadillo — Minecraft Java 1.21.4

Mod klienta Fabric.

## Funkcje
- Klawisz **O** włącza/wyłącza automat.
- Gdy patrzysz na pancernika, mod używa na nim przedmiotu z głównej ręki.
- Gdy mod jest włączony, wysyła komendę `repair` co 40 ticków (około 2 sekundy).

## Wymagania
- Minecraft Java Edition **1.21.4**
- Java **21**
- Fabric Loader
- Fabric API dla Minecraft 1.21.4

## Budowanie pliku JAR
1. Rozpakuj ZIP.
2. Otwórz terminal w folderze projektu.
3. Uruchom `gradle build` (lub `gradlew build`, jeśli dodasz Gradle Wrapper).
4. Gotowy plik znajdziesz w `build/libs/autoarmadillo-1.0.0.jar`.
5. Włóż plik JAR do folderu `.minecraft/mods`.

## Ważne
- To projekt źródłowy, nie skompilowany plik JAR.
- Komenda `/repair` działa wyłącznie na serwerze, który udostępnia taką komendę.
- Automatyczne interakcje mogą być niedozwolone na niektórych serwerach; sprawdź ich regulamin.
