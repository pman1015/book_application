package org.customer_book.Database.JobsCollection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LaborChargeDAO {

  private double rate;
  private double hours;
  private double cost;
  private String name;

  public LaborChargeDAO(String name, double rate, double hours) {
    this.name = name;
    this.rate = rate;
    this.hours = hours;
    this.cost = rate * hours;
  }

  
}
