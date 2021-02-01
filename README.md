# LarJson

LarJson is a Java library that can be used to map huge JSON files to Java model objects efficiently with small memory
footprint and few allocated objects. For example, JSON files that weigh hundreds of gigabytes can be mapped using a JVM 
heap of just 256 MB or less.

The LarJson mapper builds a blueprint of the entire JSON file. This blueprint is used for fast random access to any JSON 
value within the file. Depending on the JSON file size and the mapper's configuration, the blueprint may be stored either 
directly in memory or in a temporary file to limit the memory usage.

Theoretically, the maximum JSON file size that can be processed by the LarJson mapper is 1 exabytes 
(~ 1 billion gigabytes). In practical terms, the mapper is limited by hard disk space (used for storing the blueprint), 
hard disk performance (the time needed to read fragments of the file into memory), and CPU time (to process the JSON file 
for the first time).

More information about the functionality provided by this mapper and its configuration can be found in the Javadoc
of the class `com.aminebag.larjson.mapper.LarJsonTypedMapper`.

## Benchmarks

### Configuration
The following benchmarks have been executed on a MacBook Air computer with the following configuration:
* Processor: 2,2 GHz Intel Core i7, 1 processor, 2 cores
* Memory: 8 GB 1600 MHz DDR3
* Hard drive: APPLE SSD SM0512G

### Description
The benchmark test starts by mapping a JSON file using a JSON mapper to a Java object in order to measure the time 
needed to deserialize and the memory space occupied by the object. Then, the object is serialized in another JSON file
in order to measure the time needed to access all the values stored in the object.

### Mappers
The following mappers are compared:
* **LarJson M.B.**: The LarJsonTypedMapper using a blueprint that is entirely in memory. With the current version 
of this mapper, a memory blueprint cannot exceed 2 GB.
* **LarJson F.B.**: The LarJsonTypedMapper using a blueprint that is entirely on the hard drive. There's no limit on the
size of the file other than the capacity of the hard drive.
* **Jackson**: The ObjectMapper provided by the Jackson library.
* **Gson**: The Gson mapper provided by the Gson library.

### JSON Data
JSON files tested in these benchmarks are based on data provided by the Punk API https://punkapi.com/documentation/v2.

### Metrics
The following metrics are measured in these benchmarks:
* **Reading duration** : the time needed to map the JSON file to the Java object.
* **Heap space**: the heap space that is labeled as used by the JVM after the reading is completed.
* **Hard drive**: the additional hard drive space that is occupied as a result of reading the Java object 
(the blueprint file in the case of the LarJson F.B. mapper).
* **Writing duration**: the time needed to write the Java object to a different JSON file.

### Results
Different JSON file sizes were tested:

* **8 Kilobytes JSON file**

| JSON Mapper | Gson (Xmx4g)  | Jackson (Xmx4g) | LarJson M.B. (Xmx4g) | LarJson F.B. (Xmx4g) |
|-------------|------------- | ------------- | ------------- | ------------- |
| Reading duration | 51 ms | 176 ms | 166 ms | 91 ms |
| Heap space | 0.77 MB | 1.54 MB | 1.50 MB | 1.53 MB |
| Hard drive | 0 | 0 | 0 | 1.51 kB |
| Writing duration | 20 ms | 43 ms | 50 ms | 36 ms |

* **1 Megabyte JSON file**

| JSON Mapper | Gson (Xmx4g)  | Jackson (Xmx4g) | LarJson M.B. (Xmx4g) | LarJson F.B. (Xmx4g) |
|-------------|------------- | ------------- | ------------- | ------------- |
| Reading duration | 126 ms | 235 ms | 220 ms | 262 ms |
| Heap space | 2.86 MB | 3.69 MB | 1.90 MB | 1.77 MB |
| Hard drive | 0 | 0 | 0 | 173 kB |
| Writing duration | 122 ms | 133 ms | 361 ms | 370 ms |

* **256 Megabytes JSON file**

| JSON Mapper | Gson (Xmx4g)  | Jackson (Xmx4g) | LarJson M.B. (Xmx4g) | LarJson F.B. (Xmx4g) |
|-------------|------------- | ------------- | ------------- | ------------- |
| Reading duration | 4.03 s | 3.54 s | 6.11 s | 6.19 s |
| **Heap space** | **539.11 MB** | **539.95 MB** | **45.15 MB** | **1.63 MB** |
| Hard drive | 0 | 0 | 0 | 43.54 MB |
| Writing duration | 4.47 s | 2.93 s | 11.38 s | 11.54 s |

* **1 Gigabyte JSON file**

| JSON Mapper | Gson (Xmx4g)  | Jackson (Xmx4g) | LarJson M.B. (Xmx4g) | LarJson F.B. (Xmx4g) |
|-------------|------------- | ------------- | ------------- | ------------- |
| Reading duration | 15.52 s | 11.38 s | 21.80 s | 20.81 s |
| **Heap space** | **2.10 GB** | **2.10 GB** | **175.76 MB** | **1.64 MB** |
| Hard drive | 0 | 0 | 0 | 174.16 MB |
| Writing duration | 17.03 s | 21.66 s | 42.86 s | 45.79 s |

* **4 Gigabytes JSON file**

| JSON Mapper | Gson (Xmx4g)  | Jackson (Xmx4g) | LarJson M.B. (Xmx4g) | LarJson F.B. (Xmx4g) |
|-------------|------------- | ------------- | ------------- | ------------- |
| Reading duration | OutOfMemoryError | OutOfMemoryError | 85.48 s | 83.59 s |
| **Heap space** | - | - | **698.26 MB** | **1.63 MB** |
| Hard drive | - | - | 0 | 696.65 MB |
| Writing duration | - | - | 10 min 51 s | 13 min 00 s |

* **16 Gigabytes JSON file**

| JSON Mapper | LarJson M.B. (Xmx4g) | LarJson F.B. (Xmx4g) | LarJson F.B. (Xmx128m) |
|-------------|------------- | ------------- | ------------- |
| Reading duration | 7 min 1 s | 5 min 27 s | 5 min 52 s |
| **Heap space** | 2.72 GB | **1.64 MB** | **1.52 MB** |
| Hard drive | 0 | 2.72 GB | 2.72 GB |
| Writing duration | 44 min 5 s | 55 min 3 s | 50 min 15 s |

* **128 Gigabytes JSON file**

| JSON Mapper | LarJson M.B. (Xmx4g) | LarJson F.B. (Xmx4g) | LarJson F.B. (Xmx128m) |
|-------------|------------- | ------------- | ------------- |
| Reading duration | OutOfMemoryError | 44 min 27 s | 44 min 42 s  |
| **Heap space** | - | **1.52 MB** | **1.52 MB** |
| Hard drive | - | 21.77 GB | 21.77 GB |
| Writing duration | - | 7 h 25 min | 7 h 31 min |

### Run
You can run a benchmark on a machine of your choice by following these steps:
1. Generate the JSON file to use. 
You need to indicate the target file size in Kilobytes: `./gradlew generateJsonData --args="1024"`. 
The generated file can be found at `build/resources/benchmark/json-data/`.
2. Run the benchmark using one of the available mappers and one of the generated JSON files:
    * *LarJson M.B.*: `./gradlew benchmark --args="larjsonMemoryBlueprint beers-8-kb.json"`
    * *LarJson F.B.*: `./gradlew benchmark --args="larjsonFileBlueprint beers-8-kb.json"`
    * *Jackson*: `./gradlew benchmark --args="jackson beers-8-kb.json"`
    * *Gson*: `./gradlew benchmark --args="gson beers-8-kb.json"`

Or, you can simply run the script `benchmark/full_benchmark.sh` from the project's root directory to start a suite of 
benchmark tests similar to those listed above. Depending on the performance of your computer the entire run may take 
hours or days. At least 140 GB of hard disk space is required to run all tests.

## Usage
### Dependency
#### Maven
```xml
<dependency>
  <groupId>com.aminebag.larjson</groupId>
  <artifactId>larjson-mapper</artifactId>
  <version>1.0.0</version>
</dependency>
```

#### Gradle
```groovy
implementation 'com.aminebag.larjson:larjson-mapper:1.0.0'
```

### Hello World
```java
LarJsonTypedMapper<Movie> mapper = new LarJsonTypedMapper<>(Movie.class,
        new LarJsonTypedReadConfiguration.Builder()
            .enableJsonPropertyAnnotation()
            .enableJsonFormatAnnotation()
            .enableValidation()
            .setLocalDateFormat("yyyy-MM-dd")
            .setLenient(false)
            .setCharset(StandardCharsets.UTF_8)
            .setUnknownJsonAttributeAllowed(true)
            .setMaxMemoryBlueprintSize(256 * 1024 * 1024) //256 MB
            .setCacheSize(512) //512 maximum cached values
            .setMutable(true)
            .setThreadSafe(false)
            .setCamelCase()
            .build());

Movie movie = mapper.readObject(new File("movie.json"));
try {
    //manipulate object
} finally {
    mapper.close(movie); //release resources
}
```

```java
interface Movie {
    @JsonProperty(required = true)
    String getTitle();

    LocalDate getReleaseDate();
    long getRevenue();
    Collection<Genre> getGenres();
    List<Review> getReviews();

    @JsonProperty("avg-rating")
    Double getAverageRating();

    int getRunningTime();
    Iterable<String> getProductionCountries();
    Director getDirector();

    boolean isReleased();

    void setReleased(boolean released);
}
```

```java
interface Director {
    String getName();

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    Date getBirthDate();

    String getBiography();
}
```

```java
interface Review {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime getReviewDate();

    String getReviewer();
    int getRating();

    @NotBlank
    String getReview();
}
```

```java
enum Genre {
    COMEDY, DRAMA, FAMILY, ANIMATION, ADVENTURE, ACTION
}
```

```json
{
  "title": "Inception",
  "releaseDate": "2010-07-15",
  "revenue": 825532764,
  "genres": ["ACTION", "ADVENTURE"],
  "released": true,
  "reviews": [
    {
      "reviewer": "plaminio",
      "reviewDate": "2013-05-22 17:55:42",
      "review": "Awesome !",
      "rating": 9
    },
    {
      "reviewer": "joseph",
      "reviewDate": "2016-12-08 10:37:06",
      "review": "A piece of art"
    }
  ],
  "avg-rating": 8.3,
  "runningTime": 148,
  "productionCountries": ["UK", "USA"],
  "director": {
    "name": "Christopher Nolan",
    "birthDate": 18190800000,
    "biography": "A British-American film director.\nBorn in Westminster, London, England"
  },
  "language": "english"
}
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.
