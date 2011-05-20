package uk.ac.rhul.cs.cl1;

import static org.junit.Assert.*;

import org.junit.Test;

public class DiceSimilarityTest extends SimilarityTest {
	@Test
	public void testGetSimilarity() {
		DiceSimilarity<Set> sim = new DiceSimilarity<Set>();
		Set set1 = new Set(1, 2, 3, 4, 5, 6, 7, 8);
		Set set2 = new Set(2, 4, 6, 9, 10);
		Set set3 = new Set(9, 10, 11, 12);
		
		assertEquals(1.0, sim.getSimilarity(set1, set1), 1e-6);
		assertEquals(6/13.0, sim.getSimilarity(set1, set2), 1e-6);
		assertEquals(0.0, sim.getSimilarity(set1, set3), 1e-6);
		assertEquals(4/9.0, sim.getSimilarity(set2, set3), 1e-6);
		assertEquals(4/9.0, sim.getSimilarity(set3, set2), 1e-6);
	}
}
