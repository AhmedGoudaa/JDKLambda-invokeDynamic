package org.gouda;

import org.gouda.data.Destination;
import org.gouda.data.Source;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.RunnerException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Fork(2)
@Warmup(iterations = 4, time = 2)
@Measurement(iterations = 4, time = 1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class DynamicMappingBenchmark {

  static final Function<Source.Phone, Destination.Phone>                         PHONE_PHONE_FUNCTION = (Source.Phone sourcePhone) -> new Destination.Phone(sourcePhone.getNumber());
  static final   Function<Map.Entry<Integer, String>, Map.Entry<Integer, Integer>> MAP_MAP_FUNCTION     = (Map.Entry<Integer, String> entry) -> Map.entry(entry.getKey(), Integer.valueOf(entry.getValue()));

  public static final Rules RULES = new Rules.RulesBuilder()
          .rule(new Rule("Name", "getName", "setName"))
          .rule(new Rule("Address name", "getAddress.getName", "getAddress.setAddressName"))
          .rule(new Rule("Phone numbers", "getPhones", "setPhoneList", PHONE_PHONE_FUNCTION))
          .rule(new Rule("Simple map mapping", "getMapSimple", "setMapSimple"))
          .rule(new Rule("Complicated map mapping", "getMapComplicated", "setMapComplicated", MAP_MAP_FUNCTION))
          .build();


//  @Param({
//          "0",
//          "1"
//  })
  int index;

  Source      source;
  Destination destination;

  @Setup
  public void setup() {

    source = new Source();
    source.setId(1);
    source.setName("source name");
    source.setAge(1);
    Source.Address address = new Source.Address("21 st ", new Source.StreetName("_"));
    source.setAddress(address);
    source.setPhones(List.of(new Source.Phone(" source phone 1"),new Source.Phone(" source phone 2")));

    source.setMapSimple(Map.of(1,"source1",2,"Source2"));
    source.setMapComplicated(Map.of(1,"1",2,"2"));

    destination = new Destination();
    destination.setId(2);
    destination.setName("Any");
    destination.setAge(8);
    destination.setAddress(new Destination.Address("D", new Destination.StreetName("sesame street ")));

  }



//  @Benchmark
  public Object invokeDynamic_SingleRule() {
    return DynamicMapping.map(source,destination, RULES.getRules().get(index));
  }

  @Benchmark
  public Object invokeDynamicWithAllTheRules() {
    return DynamicMapping.map(source,destination, RULES);
  }

  @Benchmark
  public Object invokeVirtual() {
    destination.setName(source.getName());
    destination.getAddress().setAddressName(source.getAddress().getName());
    List<Destination.Phone> phoneList = source.getPhones()
            .stream()
            .map(phone -> new Destination.Phone(phone.getNumber()))
            .collect(Collectors.toList());
    destination.setPhoneList(phoneList);

    destination.setMapSimple(source.getMapSimple());

    Map<Integer, Integer> integerIntegerMap = source.getMapComplicated()
            .entrySet()
            .stream()
            .map(MAP_MAP_FUNCTION)
            .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue(), (o, o2) -> o2));

    destination.setMapComplicated(integerIntegerMap);
    return destination;
  }


  public static void main(String... args) throws IOException, RunnerException {
    Main.main(args);
  }
}
