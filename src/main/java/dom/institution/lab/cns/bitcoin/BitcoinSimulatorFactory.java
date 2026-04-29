package dom.institution.lab.cns.bitcoin;

import dom.institution.lab.cns.bitcoin.node.BitcoinNodeFactory;
import dom.institution.lab.cns.engine.Debug;
import dom.institution.lab.cns.engine.Simulation;
import dom.institution.lab.cns.engine.SimulatorFactory;
import dom.institution.lab.cns.engine.config.Config;
import dom.institution.lab.cns.engine.event.Event_HashPowerChange;
import dom.institution.lab.cns.engine.exceptions.ConfigurationException;
import dom.institution.lab.cns.engine.node.AbstractNodeFactory;
import dom.institution.lab.cns.engine.node.IMiner;
import dom.institution.lab.cns.engine.node.INode;
import dom.institution.lab.cns.engine.node.PoWNodeSet;


/**
 * A concrete {@linkplain SimulatorFactory} for Bitcoin network simulations.
 * <p>
 * This factory defines how the node set is created for the simulation,
 * specifically differentiating between honest and malicious Bitcoin nodes.
 * It uses {@linkplain BitcoinNodeFactory} to generate nodes with specific
 * roles and properties based on configuration parameters.
 * </p>
 * <p>
 * The number of honest and malicious nodes is determined by configuration
 * properties:
 * <ul>
 *     <li>{@code net.numOfHonestNodes}</li>
 *     <li>{@code net.numOfMaliciousNodes}</li>
 * </ul>
 *
 * <p>
 * This factory also supports dynamic hashpower changes during simulation runtime.
 * Hashpower changes can be configured using:
 * <ul>
 *     <li>{@code node.hashPowerChanges} - Format: {nodeID:newHashPower:time, ...}</li>
 * </ul>
 *
 * <p>
 * Example usage:
 * <pre>
 * {@linkplain BitcoinSimulatorFactory} factory = new {@linkplain BitcoinSimulatorFactory}();
 * {@linkplain Simulation} sim = factory.createSimulation(simID);
 * </pre>
 *
 * <p>
 * This class overrides {@linkplain SimulatorFactory#createNodeSet(Simulation)}
 * to provide a concrete Bitcoin-specific implementation, and overrides
 * {@linkplain SimulatorFactory#createSimulation(int)} to add hashpower change
 * event scheduling. All other aspects of simulation setup (network, transaction
 * workload, reporting, termination) are handled by the default implementations
 * of base {@linkplain SimulatorFactory}.
 * </p>
 *
 */
public class BitcoinSimulatorFactory extends SimulatorFactory {

	/**
	 * Creates and fully configures a new {@link Simulation} instance for Bitcoin.
	 * <p>
	 * This method extends the parent's {@link SimulatorFactory#createSimulation(int)}
	 * by adding hashpower change event scheduling after the base simulation setup.
	 * </p>
	 *
	 * @param simID a unique identifier for the simulation
	 * @return a fully initialized and ready-to-run {@link Simulation}
	 */
	@Override
	public Simulation createSimulation(int simID) {
		// Call parent to create the base simulation
		Simulation s = super.createSimulation(simID);
		return s;
	}

    /**
     * Creates a {@linkplain PoWNodeSet} for a Bitcoin simulation.
     * <p>
     * This method instantiates honest nodes first, using the {@linkplain BitcoinNodeFactory}
     * with type "Honest", then switches to a factory for malicious nodes.
     * The numbers of each node type are retrieved from the configuration.
     * </p>
     *
     * @param s the {@linkplain Simulation} for which the node set is being created
     * @return a {@linkplain PoWNodeSet} containing both honest and malicious nodes
     */
    @Override
	public PoWNodeSet createNodeSet(Simulation s) {

    	int honestNodes;
    	int attackingNodes = 0;
    	
    	if (Config.hasProperty("hiddenChainAttack.attackingNodes")) {
    		attackingNodes = Config.getPropertyInt("hiddenChainAttack.attackingNodes");
    	}
    	honestNodes = Config.getPropertyInt("net.numOfNodes") - attackingNodes;
    	
    	PoWNodeSet ns = new PoWNodeSet();
    	
    	ns.setNodeFactory(new BitcoinNodeFactory("Honest", s));
		ns.addNodes(honestNodes);
		
		
		ns.setNodeFactory(new BitcoinNodeFactory("Hidden Chain Attack", s, ns));
		ns.addNodes(attackingNodes);
		
		return ns;
	}

}
