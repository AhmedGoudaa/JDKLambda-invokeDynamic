# JDKLambda-invokeDynamic

playing with java byteCode instruction `invokeDynamic` that is used to create lambda functions `(int a,int b)-> a+b`
and many more and used a lot by graalvm  

    
    
    
```java
var rule1 = new Rule("Name", "getName", "setName");
var rule2 = new Rule("Address name", "getAddress.getName", "getAddress.setAddressName");
var rule3 = new Rule("Phone numbers", "getPhones", "setPhoneList", PHONE_PHONE_FUNCTION);
var rule4 = new Rule("Simple map mapping", "getMapSimple", "setMapSimple");
var rule5 = new Rule("Complicated map mapping", "getMapComplicated", "setMapComplicated", MAP_MAP_FUNCTION);
````
 

|Benchmark                                            |(index) |Mode  | Cnt |  Score    | Error | Units  |
| --------------------------------------------------- | ------ | ---- | --- | --------- | ----- | ------ |
|DynamicMappingBenchmark.invokeDynamicWithAllTheRules |    N/A |avgt  | 8   | 402.480 ± |30.886 | ns/op |
|DynamicMappingBenchmark.invokeDynamic_SingleRule     |      1 |avgt  | 6   |  15.633 ± | 0.504 | ns/op |
|DynamicMappingBenchmark.invokeDynamic_SingleRule     |      2 |avgt  | 6   |  32.697 ± |21.061 | ns/op |
|DynamicMappingBenchmark.invokeVirtual                |    N/A |avgt  | 8   | 279.842 ± |30.260 | ns/op |
