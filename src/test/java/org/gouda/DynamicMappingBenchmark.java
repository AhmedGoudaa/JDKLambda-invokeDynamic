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
import java.util.concurrent.TimeUnit;

@Fork(2)
@Warmup(iterations = 3, time = 2)
@Measurement(iterations = 3, time = 1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class DynamicMappingBenchmark {

  public static final Rules RULES = new Rules.RulesBuilder()
          .rule(new Rule("Name", "getName", "setName"))
          .rule(new Rule("Address name", "getAddress.getName", "getAddress.setAddressName"))
          .build();


  @Param({
          "0",
          "1"
  })
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
    destination = new Destination();
    destination.setId(2);
    destination.setName("Any");
    destination.setAge(8);
    destination.setAddress(new Destination.Address("D", new Destination.StreetName("sesame street ")));

  }



  @Benchmark
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
    return destination;
  }


  public static void main(String... args) throws IOException, RunnerException {
    Main.main(args);
  }
}
