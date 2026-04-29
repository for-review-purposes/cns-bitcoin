// java.lang.IllegalArgumentException: Invalid target value!
package dom.institution.lab.cns.bitcoin.utils;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dom.institution.lab.cns.bitcoin.utils.BitcoinDifficultyUtility;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.util.Random;

public class BitcoinDifficultyUtilityTest {
	// initial target is 0x00000000FFFF0000000000000000000000000000000000000000000000000000,
	// out of FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF possible hash values
	// so the equivalent CNS difficulty should be FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
	// divided by 00000000FFFF0000000000000000000000000000000000000000000000000000
	// using online calculators, this value is roughly 4.29503*10^9
	private static final double CNS_INITIAL_DIFF = 4295030000.0;
	// necessary to compensate for limited precision
	private static final double ERROR_BAND = 10000.0;
	private static final int MAX_BTC_DIFF = Integer.MAX_VALUE - 1;
	private static final int NUM_TRIALS = 10000;

	@Test
	public void testBTCToCNS_initialTarget() {
		double CNSDiff = BitcoinDifficultyUtility.BTCToCNS(1.0);

		assertEquals(CNSDiff, CNS_INITIAL_DIFF, 10000.0);
	}

	@Test
	public void testBTCToCNS_randomDifficulties() {
		// test random difficulty values
		Random gen = new Random();
		for(int i = 0; i < NUM_TRIALS; i++) {
			double BTCDiff = gen.nextInt(MAX_BTC_DIFF) * gen.nextDouble() + 1;
			double CNSDiff = BitcoinDifficultyUtility.BTCToCNS(BTCDiff);

			assertEquals(CNSDiff, CNS_INITIAL_DIFF*BTCDiff, ERROR_BAND*BTCDiff);
		}
	}

	@Test
	public void testCNSToBTC_initialTarget() {
		double BTCDiff = BitcoinDifficultyUtility.CNSToBTC(CNS_INITIAL_DIFF);

		assertEquals(BTCDiff, 1, ERROR_BAND/CNS_INITIAL_DIFF);
	}

	@Test
	public void testCNSToBTC_randomDifficulties() {
		// test random difficulty values
		Random gen = new Random();
		for(int i = 0; i < NUM_TRIALS; i++) {
			double factor = gen.nextInt(MAX_BTC_DIFF);
			double CNSDiff = CNS_INITIAL_DIFF*factor;
			double BTCDiff = BitcoinDifficultyUtility.CNSToBTC(CNSDiff);

			assertEquals(BTCDiff, 1*factor, (ERROR_BAND*factor)/CNS_INITIAL_DIFF);
		}
	}

	// Note on floating point underflow: IEEE754 specifies double-precision floating points as
	// 1 bit sign, 11 bits exponent, 52 bits significant (+1 implicit set bit), as well as
	// preserving certain exponent values for special values. This means that we get underflow
	// at values smaller in magnitude than +/- 2 * 2^-((2^10)-1), which should NOT affect difficulty.
	@Test
	@Tag("exclude")
	public void testUnderflow() {
		// convert difficulty to pool value, then consider smallest possible change in pool difficulty
		String BTCDiff = BitcoinDifficultyUtility.CNSToPoolTarget(CNS_INITIAL_DIFF);
		BigInteger pool = new BigInteger(BTCDiff.substring(2), 16);
		pool.subtract(new BigInteger("1", 16));

		// precision of doubles should be able to handle smallest possible change in difficulty,
		// since 1/16^64 = (2^-4)^64 = 2^-256, which is much larger than 2^-((2^10)-1) = 2^-1023
		double newDiff = BitcoinDifficultyUtility.poolTargetToCNS(pool.toString().substring(2));

		assertNotEquals(CNS_INITIAL_DIFF, newDiff);
	}
}
