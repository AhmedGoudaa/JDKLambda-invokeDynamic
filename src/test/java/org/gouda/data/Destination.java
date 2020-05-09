package org.gouda.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Destination {

  int         id;
  String      name;
  int         age;
  Address     address;
  List<Phone> phoneList;


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


