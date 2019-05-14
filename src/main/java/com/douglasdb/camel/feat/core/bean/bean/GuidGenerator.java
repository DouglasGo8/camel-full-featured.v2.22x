package com.douglasdb.camel.feat.core.bean.bean;

import java.util.Random;

/**
 * 
 * @author Administrador
 *
 */
public class GuidGenerator {
	public static int generate() {
		Random ran = new Random();
		return ran.nextInt(10000000);
	}
}
