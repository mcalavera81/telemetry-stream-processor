## Telemetry Stream Processor

#### Build the Project
```
./gradlew clean build
```

#### Use the tool
You can get the help screen with:
```
java -jar  ./build/libs/telemetry-stream-processor-1.0.jar --help
```
Output:

```
usage: [-h] -A INBOUND -B OUTBOUND -I INPUT [-O OUTPUT] --batch

required arguments:
  -A INBOUND,           Hostname for inbound connections set
  --inbound INBOUND

  -B OUTBOUND,          Hostname for outbound connections set
  --outbound OUTBOUND

  -I INPUT,             Filename to write the report to
  --input INPUT

  --batch, --tail       mode of operation


optional arguments:
  -h, --help            show this help message and exit

  -O OUTPUT,            Filename to write the report to
  --output OUTPUT

```

##### Batch Mode
Parses previously written logfiles
```
java -jar  ./build/libs/telemetry-stream-processor-1.0.jar \
    --inbound <host> --outbound <host> --input <filepath> --batch
```

##### Tail Mode
Parses logfiles being written indefinitely
```
java -jar  ./build/libs/telemetry-stream-processor-1.0.jar \
    --inbound <host> --outbound <host> --input <filepath> --tail
```

By the default the process output is redirected to the console. In case you 
want to write the report to a file you must use **--output** flag

```
java -jar  ./build/libs/telemetry-stream-processor-1.0.jar \
    --inbound pepe --outbound pepe --input ~/pepe.txt --output ~/out.txt  --batch
```
