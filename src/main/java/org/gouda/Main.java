package org.gouda;


public class Main {


  public static final Rules RULES =
          new Rules.RulesBuilder()
                  .rule(new Rule("Id", "getId", "setId"))
                  .rule(new Rule("Name", "getName", "setName"))
                  .rule(new Rule("Age", "getAge", "setAge"))
                  .rule(new Rule("Address name", "getAddress.getName", "getAddress.setAddressName"))
//                  .rule(new Rules.Rule("Phone numbers", "phoneList.*.number", "phoneList.*.num"))
                  .build();


  public static void main(String[] args) {

    DynamicMapping mapping = new DynamicMapping();

    for (int i = 1; i < 100; i++) {

      Source source = new Source();
      source.setId(i);
      source.setName("source name"+i);
      source.setAge(i);
      Source.Address  address = new Source.Address("21 st "+i, new Source.StreetName("_"+i));
      source.setAddress(address);

      var d = i-1;
      Destination destination = new Destination();
      destination.setId(d);
      destination.setName("Any"+d);
      destination.setAge(d);
      destination.setAddress(new Destination.Address("D"+d, new Destination.StreetName("sesame street "+d)));
      System.out.println(destination);
    System.out.println(mapping.map(source, destination, RULES));
    }




  }
}
