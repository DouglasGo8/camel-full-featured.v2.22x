package com.douglasdb.camel.feat.core.domain.jpa;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 *
 */
@Data
@Entity
@NoArgsConstructor
public class PurchaseOrder implements Serializable {
    private String name;
    private double amount;
    private String customer;
}
