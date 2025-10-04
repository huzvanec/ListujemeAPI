# ListujemeAPI

This is the backend REST API for the [Listujeme project](https://listu.jeme.cz).

## Development

### Prepare the build environment

```shell
# Clone the repo
git clone https://github.com/huzvanec/ListujemeAPI.git
cd ListujemeAPI

# Init and update dependencies (may take a while)
git submodule update --init --recursive

# Build MuPDF
# This has to be run using javac version <= 17 (javac --version)
cd vendor/mupdf/platform/java
make
cd ../../../..
```

### Building

```shell
./gradlew bootJar
```

The output jar is located in `build/libs`

### Execution

To run the jar file, the native MuPDF library file
(from `vendor/mupdf/build/java/release/`)
must be placed in the same folder as the jar.

```
# Linux structure example
.
├── ListujemeAPI-1.0.0.jar
└── libmupdf_java64.so
```

To run the jar file, modify the VM options to find the library.

```bash
java -Djava.library.path=. -jar ./ListujemeAPI-0.0.1-SNAPSHOT.jar
```

The program will create directories next to the jar when ran for the first time.
The `pdfs` folder should be filled with newspaper PDFs
