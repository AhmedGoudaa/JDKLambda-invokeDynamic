package org.gouda;

//import org.gouda.data.Destination;
//import org.gouda.data.Source;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

final public class StaticMapperFunctions {

  private final static Map<String , Function<?,?>> STATIC_MAPPER_FN ;

  static {
    STATIC_MAPPER_FN = new HashMap<>();

//    STATIC_MAPPER_FN.put("sourcePhone_desPhone", (Source.Phone sourcePhone) -> new Destination.Phone(sourcePhone.getNumber()));


  }

  private StaticMapperFunctions() {
  }

  static Function get(String key){
    return STATIC_MAPPER_FN.get(key);
  }


}
