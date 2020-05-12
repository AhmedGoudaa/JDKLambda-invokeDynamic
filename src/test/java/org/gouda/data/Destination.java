package org.gouda.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Destination {

  int                  id;
  String               name;
  int                  age;
  float                salary;
  Address              address;
  List<Phone>          phoneList;
  Map<Integer , String>  mapSimple;
  Map<Integer , Integer>  mapComplicated;


  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Address {
    String     addressName;
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
    String num;
  }

}


