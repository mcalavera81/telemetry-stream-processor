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

### Sample Run

```
java -jar  ./build/libs/telemetry-stream-processor-1.0.jar \
    --inbound brunt --outbound quark --input ./demo.txt   --batch
``` 
Output:

```
Report for  20/08/2018 - 12:00 
Inbound connections: [garak] for Host: brunt
Outbound connections: [garak] for Host: quark
Host garak generated max conns (2)
----------
Report for  20/08/2018 - 01:00 
Inbound connections: [garak] for Host: brunt
Outbound connections: [quark] for Host: quark
Host brunt generated max conns (4)
----------
Report for  20/08/2018 - 02:00 
Inbound connections: [] for Host: brunt
Outbound connections: [] for Host: quark
Host lustro generated max conns (3)
----------
Report for  20/08/2018 - 03:00 
Inbound connections: [] for Host: brunt
Outbound connections: [] for Host: quark
Host maria generated max conns (1)
----------
Used Memory: 18 Mb
```