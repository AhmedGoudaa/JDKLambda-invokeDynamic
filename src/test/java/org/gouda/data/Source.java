package org.gouda.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Source {

  int                   id;
  String                name;
  int                   age;
  Address               address;
  List<Phone>           phones;
  Map<Integer , String>  mapSimple;
  Map<Integer , String>  mapComplicated;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Address {
    String     name;
    StreetName streetName;

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class StreetName {
    String stName;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Phone {
    String number;
  }

}
