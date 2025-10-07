# ListujemeAPI

ListujemeAPI je backend projektu [Listujeme](https://github.com/huzvanec/Listujeme.git).

## Development

Požadavky:

- [Git](https://git-scm.com/downloads)
- [Java 21](https://www.oracle.com/java/technologies/downloads/#java21)

### Příprava projektu

```shell
# Klonování backend repa
git clone https://github.com/huzvanec/ListujemeAPI.git
cd ListujemeAPI

# Inicializace a aktualizace MuPDF (může trvat)
git submodule update --init --recursive

# Buildování MuPDF
cd vendor/mupdf/platform/java
# Musí být spuštěno s javac verzí <= 17 (javac --version)
make
cd ../../../..
```

### Build

```shell
./gradlew bootJar
```

Výsledný jar se nachází v `build/libs`

### Spuštění jaru

Aby se dal jar spustit, musí být načtena zbuildovaná native MuPDF knihovna (z `./vendor/mupdf/build/java/release/`).

```
# Př.: Knihovna může být vedle jaru
.
├── ListujemeAPI.jar
└── libmupdf_java64.so # Linuxová native knihovna
```

Při spuštění jaru musí `-Djava.library.path` obsahovat cestu k adresáři obsahující native MuPDF knihovnu.

```shell
# Př.: Knihovna leží vedle jaru
java -Djava.library.path=. -jar ./ListujemeAPI.jar
```

Po prvním startu vytvoří API složky `cache/` a `pdfs/`. Do složky `pdfs/` se pak přidávají PDF soubory čísel
zpravodaje.