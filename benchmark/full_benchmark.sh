#!/bin/bash

# 8KB
./gradlew benchmark --args="larjsonMemoryBlueprint beers-8-kb.json" | tee benchmark/results/larjsonMemoryBlueprint-8kb.txt
./gradlew benchmark --args="larjsonFileBlueprint beers-8-kb.json" | tee benchmark/results/larjsonFileBlueprint-8kb.txt
./gradlew benchmark --args="jackson beers-8-kb.json" | tee benchmark/results/jackson-8kb.txt
./gradlew benchmark --args="gson beers-8-kb.json" | tee benchmark/results/gson-8kb.txt

# 1MB
./gradlew generateJsonData --args="1024"
./gradlew benchmark --args="larjsonMemoryBlueprint beers-1_024-kb.json" | tee benchmark/results/larjsonMemoryBlueprint-1mb.txt
./gradlew benchmark --args="larjsonFileBlueprint beers-1_024-kb.json" | tee benchmark/results/larjsonFileBlueprint-1mb.txt
./gradlew benchmark --args="jackson beers-1_024-kb.json" | tee benchmark/results/jackson-1mb.txt
./gradlew benchmark --args="gson beers-1_024-kb.json" | tee benchmark/results/gson-1mb.txt
rm build/resources/benchmark/json-data/beers-1_024-kb.json

# 256MB
./gradlew generateJsonData --args="262144"
./gradlew benchmark --args="larjsonMemoryBlueprint beers-262_144-kb.json" | tee benchmark/results/larjsonMemoryBlueprint-256mb.txt
./gradlew benchmark --args="larjsonFileBlueprint beers-262_144-kb.json" | tee benchmark/results/larjsonFileBlueprint-256mb.txt
./gradlew benchmark --args="jackson beers-262_144-kb.json" | tee benchmark/results/jackson-256mb.txt
./gradlew benchmark --args="gson beers-262_144-kb.json" | tee benchmark/results/gson-256mb.txt
rm build/resources/benchmark/json-data/beers-262_144-kb.json

# 1GB
./gradlew generateJsonData --args="1048576"
./gradlew benchmark --args="larjsonMemoryBlueprint beers-1_048_576-kb.json" | tee benchmark/results/larjsonMemoryBlueprint-1gb.txt
./gradlew benchmark --args="larjsonFileBlueprint beers-1_048_576-kb.json" | tee benchmark/results/larjsonFileBlueprint-1gb.txt
./gradlew benchmark --args="jackson beers-1_048_576-kb.json" | tee benchmark/results/jackson-1gb.txt
./gradlew benchmark --args="gson beers-1_048_576-kb.json" | tee benchmark/results/gson-1gb.txt
rm build/resources/benchmark/json-data/beers-1_048_576-kb.json

# 4GB
./gradlew generateJsonData --args="4194304"
./gradlew benchmark --args="larjsonMemoryBlueprint beers-4_194_304-kb.json" | tee benchmark/results/larjsonMemoryBlueprint-4gb.txt
./gradlew benchmark --args="larjsonFileBlueprint beers-4_194_304-kb.json" | tee benchmark/results/larjsonFileBlueprint-4gb.txt
./gradlew benchmark --args="jackson beers-4_194_304-kb.json" | tee benchmark/results/jackson-4gb.txt
./gradlew benchmark --args="gson beers-4_194_304-kb.json" | tee benchmark/results/gson-4gb.txt
rm build/resources/benchmark/json-data/beers-4_194_304-kb.json

# 16GB
./gradlew generateJsonData --args="16777216"
./gradlew benchmark --args="larjsonMemoryBlueprint beers-16_777_216-kb.json" | tee benchmark/results/larjsonMemoryBlueprint-16gb.txt
./gradlew benchmark --args="larjsonFileBlueprint beers-16_777_216-kb.json" | tee benchmark/results/larjsonFileBlueprint-16gb.txt
./gradlew benchmark --args="jackson beers-16_777_216-kb.json" | tee benchmark/results/jackson-16gb.txt
./gradlew benchmark --args="gson beers-16_777_216-kb.json" | tee benchmark/results/gson-16gb.txt
rm build/resources/benchmark/json-data/beers-16_777_216-kb.json

# 128GB
./gradlew generateJsonData --args="134217728"
./gradlew benchmark --args="larjsonMemoryBlueprint beers-134_217_728-kb.json" | tee benchmark/results/larjsonMemoryBlueprint-128gb.txt
./gradlew benchmark --args="larjsonFileBlueprint beers-134_217_728-kb.json" | tee benchmark/results/larjsonFileBlueprint-128gb.txt
./gradlew benchmark --args="jackson beers-134_217_728-kb.json" | tee benchmark/results/jackson-128gb.txt
./gradlew benchmark --args="gson beers-134_217_728-kb.json" | tee benchmark/results/gson-128gb.txt
rm build/resources/benchmark/json-data/beers-134_217_728-kb.json
