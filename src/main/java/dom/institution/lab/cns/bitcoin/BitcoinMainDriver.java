package dom.institution.lab.cns.bitcoin;

import java.io.IOException;

import dom.institution.lab.cns.bitcoin.reporter.BitcoinReporter;
import dom.institution.lab.cns.bitcoin.structure.Block;
import dom.institution.lab.cns.engine.Simulation;
import dom.institution.lab.cns.engine.config.Config;
import dom.institution.lab.cns.engine.config.ConfigInitializer;
import dom.institution.lab.cns.engine.node.PoWNode;
import dom.institution.lab.cns.engine.transaction.Transaction;


public class BitcoinMainDriver {

    public static void main(String[] args) {
        BitcoinMainDriver b = new BitcoinMainDriver();
        b.run(args);
    }


    private void run(String[] args) {
    	
        Package pkg = dom.institution.lab.cns.engine.config.Config.class.getPackage();
        System.out.println("\n  * CNS Engine Version: " + pkg.getImplementationVersion());
        
        System.out.println("  * Current directory: " + System.getProperty("user.dir"));
    	System.out.println("  * Initializing Configurator");

    	// Initialize Config
        try{
            ConfigInitializer.initialize(args);
        } catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }

        
        // Initialize Bitcoin reporter
        BitcoinReporter.initialize();
        
        
        // Get the number of simulations to run
        int numSimulations = Config.getPropertyInt("sim.numSimulations");
        int simFrom = Config.getPropertyInt("sim.numSimulations.From");
        int simTo = Config.getPropertyInt("sim.numSimulations.To");

        if ((simFrom == -1) || (simTo == -1)) {
            for (int simID = 1; simID <= numSimulations; simID++) {
                runSingleSimulation(simID);
            }
        } else {
            for (int simID = simFrom; simID <= simTo; simID++) {
                runSingleSimulation(simID);
            }
        }
        
        BitcoinReporter.flushAll();
    }

    private void runSingleSimulation(int simID) {
    	
        BitcoinSimulatorFactory sf = new BitcoinSimulatorFactory();

        System.out.println("\n  * Setting Up Simulation #" + simID);
        Simulation s = sf.createSimulation(simID);

        System.out.println("\n  * Running Simulation #" + simID);
        s.run();

        System.out.println(s.getStatistics());

        s.closeNodes();

        
        //
        //
        // Reset Statics
        //
        //
        PoWNode.resetCurrID();
        Transaction.resetCurrID();
        Block.resetCurrID();
        
        
    }


}