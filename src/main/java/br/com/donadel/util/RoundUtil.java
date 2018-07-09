package br.com.donadel.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RoundUtil {

	/**
	 * Trunca em 2 casas decimais
	 * 
	 * @param value
	 *            valor a ser truncado
	 * @return valor com apenas 2 casas decimais
	 */
	public static Double currencyTruncate(Double value) {
		BigDecimal truncate = BigDecimal.valueOf(value);
		truncate = truncate.setScale(2, RoundingMode.HALF_DOWN);
		return truncate.doubleValue();
	}

}
