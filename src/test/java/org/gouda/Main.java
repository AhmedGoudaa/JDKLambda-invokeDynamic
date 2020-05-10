package org.gouda;

import org.gouda.data.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {


  static final Function<Source.Phone, Destination.Phone> PHONE_PHONE_FUNCTION = (Source.Phone sourcePhone) -> new Destination.Phone(sourcePhone.getNumber());
  static final Function<Map.Entry<Integer, String>, Map.Entry<Integer, Integer>> MAP_MAP_FUNCTION = (Map.Entry<Integer, String> entry) -> Map.entry(entry.getKey(), Integer.valueOf(entry.getValue()));

  public static final Rules RULES =
           Rules.builder()
//                  .rule(new Rule("Id", "getId", "setId"))
//                  .rule(new Rule("Name", "getName", "setName"))
//                  .rule(new Rule("Age", "getAge", "setAge"))
//                  .rule(new Rule("Address name", "getAddress.getName", "getAddress.setAddressName"))
                  .rule(new Rule("Phone numbers", "getPhones", "setPhoneList", PHONE_PHONE_FUNCTION))
                  .rule(new Rule("Simple map mapping", "getMapSimple", "setMapSimple"))
                  .rule(new Rule("Complicated map mapping", "getMapComplicated", "setMapComplicated", MAP_MAP_FUNCTION))
                  .build();



  public static void main(String[] args) {


//    for (int i = 1; i < 100; i++) {

    int i = 1;
      Source source = new Source();
      source.setId(i);
      source.setName("source name"+i);
      source.setAge(i);
      Source.Address  address = new Source.Address("21 st "+i, new Source.StreetName("source street name"+i));
      source.setAddress(address);
      source.setPhones(List.of(new Source.Phone(" source phone 1"),new Source.Phone(" source phone 2")));

      source.setMapSimple(Map.of(1,"source1",2,"Source2"));
      source.setMapComplicated(Map.of(1,"1",2,"2"));

      var d = i-1;
      Destination destination = new Destination();
      destination.setId(d);
      destination.setName("Any"+d);
      destination.setAge(d);
      destination.setAddress(new Destination.Address("D"+d, new Destination.StreetName("sesame street "+d)));
      System.out.println(destination);
      System.out.println(source);
    System.out.println(DynamicMapping.map(source, destination, RULES));

    System.out.println(destination.getMapComplicated());
    //    System.out.println(DynamicMapping.map(source, destination, RULES));


    if (

            (source.getId() == destination.getId() )&&
                    source.getAge() == destination.getAge() &&
                    (source.getName().equals(destination.getName() ) &&
                            (source.getAddress().getName().equals(destination.getAddress().getAddressName())))

    ) {
      System.out.println("Mapping is right");
    } else {
      System.out.println("Mapping fucked up");
    }

//    Stream.of(Main.class.getDeclaredMethods())
//            .filter(method -> method.getName().endsWith("test") && method.getParameterCount() == 1)
//            .forEach(method -> {
//              System.out.println(Arrays.toString(method.getParameters()));
//              Type parameterizedType = method.getParameters()[0].getParameterizedType();
//              System.out.println((Class<?>)((ParameterizedType) parameterizedType).getActualTypeArguments()[0]);
//            });




  }

  public static int test(List<Integer> ins){
    return ins.size();
  }
}
