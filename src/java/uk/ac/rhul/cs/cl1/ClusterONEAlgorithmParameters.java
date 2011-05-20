package uk.ac.rhul.cs.cl1;

import java.io.Serializable;

import uk.ac.rhul.cs.cl1.seeding.EveryNodeSeedGenerator;
import uk.ac.rhul.cs.cl1.seeding.SeedGenerator;

/**
 * Stores the parameters of a ClusterONE algorithm instance.
 * 
 * There is a wide variety of parameters for the algorithm, but the default
 * settings are more or less sensible and suitable for biological scenarios.
 * 
 * See the documentation of the class variables for more details on the parameters.
 *   
 * @author tamas
 */
public class ClusterONEAlgorithmParameters implements Serializable {
	/** Minimum size of the clusters that will be returned */
	protected int minSize = 3;
	
	/** Minimum density of the clusters that will be returned */
	protected double minDensity = 0.3;
	
	/**
	 * Overlap threshold value.
	 * No pair of complexes will have an overlap larger than this
	 * in the result
	 */
	protected double overlapThreshold = 0.8;
	
	/**
	 * Haircut threshold value.
	 * 
	 * After generating cohesive subgroups, vertices having an internal weight
	 * less than the average internal weight times this threshold will be
	 * removed from the subgroups. If negative, no haircut will be performed.
	 */
	protected double haircutThreshold = 0;
	
	/**
	 * k-core threshold value.
	 * 
	 * After generating cohesive subgroups, those which do not contain a
	 * k-core may be thrown away. k is specified by this threshold value.
	 * If it is zero or negative, the filter will obviously be disabled.
	 */
	protected int kCoreThreshold = 0;

	/**
	 * Node penalty.
	 * 
	 * When nonzero, each node is assumed to have an extra external weight equal
	 * to this amount, no matter what the other internal nodes are. This can
	 * be used to account for noise in the initial data; see {@link CohesivenessFunction}
	 * for more details.
	 */
	protected double nodePenalty = 2.0;
	
	/**
	 * Whether to fluff the clusters.
	 * 
	 * After generating cohesive subgroups, external boundary vertices
	 * connected to more than 2/3 of the internal vertices will be added to
	 * the subgroups if this is true.
	 */
	protected boolean fluffClusters = false;
	
	/**
	 * Complex merging method.
	 * 
	 * Possible values:
	 * <ul>
	 * <li>dice: Dice similarity</li>
	 * <li>jaccard: Jaccard similarity</li>
	 * <li>match: match coefficient</li>
	 * <li>meet/min or simpson: Simpson coefficient</li>
	 * </ul>
	 */
	protected String mergingMethod = "match";
	
	/**
	 * The seed generation method.
	 */
	protected SeedGenerator seedGenerator = new EveryNodeSeedGenerator();
	
	/**
	 * Returns the k-core threshold used by the algorithm
	 * 
	 * @return the k-core threshold
	 */
	public int getKCoreThreshold() {
		return kCoreThreshold;
	}
	
	/**
	 * Returns the haircut threshold used by the algorithm
	 * 
	 * @return the haircut threshold
	 */
	public double getHaircutThreshold() {
		return haircutThreshold;
	}
	
	/**
	 * Returns the name of the merging method used by the algorithm
	 * 
	 * @return the name of the merging method
	 */
	public String getMergingMethodName() {
		return mergingMethod;
	}

	/**
	 * Returns the minimum density of clusters
	 * @return the minimum density of clusters
	 */
	public double getMinDensity() {
		return minDensity;
	}

	/**
	 * Returns the minimum size of the clusters that will be returned
	 * @return the minimum size
	 */
	public int getMinSize() {
		return minSize;
	}

	/**
	 * Returns the penalty value associated with each node.
	 * 
	 * See {@link CohesivenessFunction} for more details about what it is.
	 * 
	 * @return  the penalty value
	 */
	public double getNodePenalty() {
		return nodePenalty;
	}
	
	/**
	 * Returns the overlap threshold of the algorithm.
	 * 
	 * The overlap threshold controls whether two given clusters will be merged in the final
	 * result set. The complexes will be merged if their matching ratio or meet/min
	 * coefficient (depending on the current {@link mergingMethod}) is larger than
	 * this ratio.
	 * 
	 * @return the overlap threshold
	 */
	public double getOverlapThreshold() {
		return overlapThreshold;
	}
	
	/**
	 * Returns the quality function that will be used by the algorithm.
	 * 
	 * @return  the quality function
	 */
	public QualityFunction getQualityFunction() {
		return new CohesivenessFunction(nodePenalty);
	}
	
	/**
	 * Returns the seed generation method of the algorithm.
	 * @return the seed generation method
	 */
	public SeedGenerator getSeedGenerator() {
		return seedGenerator;
	}
	
	/**
	 * Returns the similarity function used to compare clusters.
	 * 
	 * This is indirectly specified by the value of {@link mergingMethod}.
	 * 
	 * @throws ClusterONEException if the merging method is unknown
	 */
	public SimilarityFunction<NodeSet> getSimilarityFunction() throws ClusterONEException {
		if (mergingMethod.equals("match"))
			return new MatchingScore<NodeSet>();
		if (mergingMethod.equals("meet/min") || mergingMethod.equals("simpson"))
			return new SimpsonCoefficient<NodeSet>();
		if (mergingMethod.equals("jaccard"))
			return new JaccardSimilarity<NodeSet>();
		if (mergingMethod.equals("dice"))
			return new DiceSimilarity<NodeSet>();
		throw new ClusterONEException("Unknown merging method: " + mergingMethod);
	}
	
	/**
	 * Returns whether we will fluff the clusters or not.
	 * 
	 * Yes, this is a funny name, but I wanted to keep this class compatible with
	 * JavaBean naming conventions, which prefers an "is" prefix for boolean
	 * getters.
	 */
	public boolean isFluffClusters() {
		return this.fluffClusters;
	}
	
	/**
	 * Sets the k-core threshold.
	 * 
	 * @param  kCoreThreshold  the new k-core threshold
	 */
	public void setKCoreThreshold(int kCoreThreshold) {
		this.kCoreThreshold = kCoreThreshold;
	}
	
	/**
	 * Sets whether we want to fluff the clusters or not.
	 * 
	 * @param  fluffClusters  whether we want to fluff the clusters or not.
	 */
	public void setFluffClusters(boolean fluffClusters) {
		this.fluffClusters = fluffClusters;
	}
	
	/**
	 * Sets the name of the merging method that will be used by the algorithm.
	 * 
	 * @param mergingMethod the merging method to use
	 */
	public void setMergingMethodName(String mergingMethod) {
		this.mergingMethod = mergingMethod.toLowerCase();
	}

	/**
	 * Sets the minimum density of clusters that can be considered acceptable.
	 * @param minDensity the minDensity to set
	 */
	public void setMinDensity(double minDensity) {
		this.minDensity = Math.max(0, minDensity);
	}

	/**
	 * Sets the minimum size of the clusters that will be returned
	 * @param minSize the minimum size
	 */
	public void setMinSize(int minSize) {
		this.minSize = Math.max(1, minSize);
	}
	
	/**
	 * Sets the penalty value associated with each node.
	 * 
	 * See {@link CohesivenessFunction} for more details about what it is.
	 * 
	 * @param  penalty  the penalty value
	 */
	public void setNodePenalty(double penalty) {
		this.nodePenalty = penalty;
	}
	
	/**
	 * Sets the overlap threshold of the algorithm.
	 * 
	 * @param overlapThreshold the new overlap threshold
	 * @see getOverlapThreshold()
	 */
	public void setOverlapThreshold(double overlapThreshold) {
		this.overlapThreshold = Math.max(0, overlapThreshold);
	}

	/**
	 * Sets the seed generation method of the algorithm from a string specification
	 * 
	 * @param seedMethod the new seed generation method. Must be a specification
	 *                   that is understood by {@link SeedGenerator.fromString}
	 */
	public void setSeedGenerator(String seedMethodSpec) throws InstantiationException {
		this.seedGenerator = SeedGenerator.fromString(seedMethodSpec); 
	}

	/**
	 * Sets the seed generation method of the algorithm
	 * 
	 * @param  seedGenerator  the new seed generation method.
	 */
	public void setSeedGenerator(SeedGenerator seedGenerator) {
		this.seedGenerator = seedGenerator; 
	}

	/**
	 * Sets the haircut threshold of the algorithm.
	 * 
	 * @param haircutThreshold  the new haircut threshold
	 */
	public void setHaircutThreshold(double haircutThreshold) {
		this.haircutThreshold = haircutThreshold;
	}

	/**
	 * Returns whether a haircut operation will be needed.
	 */
	public boolean isHaircutNeeded() {
		return (haircutThreshold > 0.0 && haircutThreshold <= 1.0);
	}
	
	/**
	 * Returns a nice string summary of the algorithm parameters.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Minimum size: " + minSize + "\n");
		sb.append("Minimum density: " + minDensity + "\n");
		sb.append("Overlap threshold: " + overlapThreshold + "\n");
		sb.append("Haircut threshold: " + haircutThreshold + "\n");
		sb.append("K-core threshold: " + kCoreThreshold + "\n");
		sb.append("Node penalty: " + nodePenalty + "\n");
		sb.append("Merging method: " + mergingMethod + "\n");
		sb.append("Seed generator: " + seedGenerator + "\n");
		
		return sb.toString();
	}
}
