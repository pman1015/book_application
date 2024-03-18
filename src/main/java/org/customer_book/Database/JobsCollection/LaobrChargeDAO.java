package org.customer_book.Database.JobsCollection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LaobrChargeDAO {

  private double rate;
  private double hours;
  private double cost;
}
