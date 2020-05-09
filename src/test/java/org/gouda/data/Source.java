package org.gouda.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Source {

  int         id;
  String      name;
  int         age;
  Address     address;
  List<Phone> phones;


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
