# JDKLambda-invokeDynamic

playing with java byteCode instruction `invokeDynamic` that is used to create lambda functions `(int a,int b)-> a+b`
and many more and used a lot by graalvm  

    
    
    
```java
var rule1 = new Rule("Name", "getName", "setName");
var rule2 = new Rule("Address name", "getAddress.getName", "getAddress.setAddressName"))
````
 
|			Benchmark                                  | (rule) | Mode | Cnt |  Score   | Error | Units|
| ---------------------------------------------------- | ------ | ---- | --- | -------- | ----- | ---- |
|DynamicMappingBenchmark.invokeDynamicWithAllTheRules  |      1 | avgt |   6 | 54.023 ± |20.012 | ns/op|
|DynamicMappingBenchmark.invokeDynamicWithAllTheRules  |      2 | avgt |   6 | 51.146 ± | 0.942 | ns/op|
|DynamicMappingBenchmark.invokeDynamic_SingleRule      |      1 | avgt |   6 | 15.549 ± | 0.189 | ns/op|
|DynamicMappingBenchmark.invokeDynamic_SingleRule      |      2 | avgt |   6 | 25.589 ± | 0.427 | ns/op|
|DynamicMappingBenchmark.invokeVirtual                 |      1 | avgt |   6 | 10.134 ± | 0.629 | ns/op|
|DynamicMappingBenchmark.invokeVirtual                 |      2 | avgt |   6 |  9.870 ± | 0.452 | ns/op|


