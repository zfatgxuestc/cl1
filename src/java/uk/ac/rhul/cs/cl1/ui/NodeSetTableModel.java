package uk.ac.rhul.cs.cl1.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import uk.ac.rhul.cs.cl1.NodeSet;

/**
 * Table model that can be used to show a list of {@link NodeSet} objects
 * in a JTable
 * 
 * @author tamas
 */
public class NodeSetTableModel extends AbstractTableModel {
	/** Column headers for the simple mode */
	String[] simpleHeaders = { "Cluster", "Details" };
	
	/** Column classes for the simple mode */
	@SuppressWarnings("unchecked")
	Class[] simpleClasses = { String.class, NodeSetDetails.class };
	
	/** Column headers for the detailed mode */
	String[] detailedHeaders = { "Cluster", "Nodes", "Density",
			"In-weight", "Out-weight", "Quality" };
	
	/** Column classes for the detailed mode */
	@SuppressWarnings("unchecked")
	Class[] detailedClasses = {
		String.class, Integer.class, Double.class, Double.class, Double.class, Double.class
	};
	
	/** Column headers for the current mode */
	String[] currentHeaders = null;
	
	/** Column classes for the current mode */
	@SuppressWarnings("unchecked")
	Class[] currentClasses = null;
	
	/**
	 * The list of {@link NodeSet} objects shown in this model
	 */
	protected List<NodeSet> nodeSets = null;
	
	/**
	 * The list of {@link NodeSetDetails} objects to avoid having to calculate
	 * the Details column all the time
	 */
	protected List<NodeSetDetails> nodeSetDetails = new ArrayList<NodeSetDetails>();
	
	/**
	 * Whether the model is in detailed mode or simple mode
	 * 
	 * In the simple mode, only two columns are shown: the cluster members
	 * and some basic properties (in a single column). In the detailed mode,
	 * each property has its own column
	 */
	boolean detailedMode;
	
	/**
	 * Constructs a new table model backed by the given list of nodesets
	 */
	public NodeSetTableModel(List<NodeSet> nodeSets) {
		this.nodeSets = new ArrayList<NodeSet>(nodeSets);
		updateNodeSetDetails();
		this.setDetailedMode(false);
	}

	public int getColumnCount() {
		return currentHeaders.length;
	}

	public int getRowCount() {
		return nodeSets.size();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Class getColumnClass(int col) {
		return currentClasses[col];
	}
	
	@Override
	public String getColumnName(int col) {
		return currentHeaders[col];
	}
	
	public Object getValueAt(int row, int col) {
		NodeSet nodeSet = this.nodeSets.get(row);
		if (nodeSet == null)
			return null;
		
		if (col == 0)
			return nodeSet.toString();
		
		if (!detailedMode) {
			/* Simple mode, column 1 */
			return this.nodeSetDetails.get(row);
		}
		
		/* Detailed mode */
		if (col == 1)
			return nodeSet.size();
		if (col == 2)
			return nodeSet.getDensity();
		if (col == 3)
			return nodeSet.getTotalInternalEdgeWeight();
		if (col == 4)
			return nodeSet.getTotalBoundaryEdgeWeight();
		if (col == 5)
			return nodeSet.getQuality();
		
		return "TODO";
	}
	
	/**
	 * Returns the {@link NodeSet} shown in the given row.
	 * 
	 * @param row   the row index
	 * @return   the corresponding {@link NodeSet}
	 */
	public NodeSet getNodeSetByIndex(int row) {
		return nodeSets.get(row);
	}
	
	/**
	 * Returns whether the table model is in detailed mode
	 */
	public boolean isInDetailedMode() {
		return detailedMode;
	}
	
	/**
	 * Returns whether the table model is in detailed mode
	 */
	public void setDetailedMode(boolean mode) {
		currentHeaders = detailedMode ? detailedHeaders : simpleHeaders;
		currentClasses = detailedMode ? detailedClasses : simpleClasses;
		
		if (mode == detailedMode)
			return;
		
		detailedMode = mode;
		this.fireTableStructureChanged();
	}
	
	private void updateNodeSetDetails() {
		nodeSetDetails.clear();
		for (NodeSet nodeSet: nodeSets) {
			nodeSetDetails.add(new NodeSetDetails(nodeSet));
		}
	}
}