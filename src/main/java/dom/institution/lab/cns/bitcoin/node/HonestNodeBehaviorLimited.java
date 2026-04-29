package dom.institution.lab.cns.bitcoin.node;

import dom.institution.lab.cns.bitcoin.structure.Block;
import dom.institution.lab.cns.engine.transaction.Transaction;

public class HonestNodeBehaviorLimited extends HonestNodeBehavior {

	public HonestNodeBehaviorLimited(BitcoinNode node) {
		super(node);
	}

	@Override
    protected boolean transactionContainedInStructure (Transaction t) {
    	return(false);
    }
	
	
	@Override
    protected void handleNewBlockReception(Block b) {
        // Add block to the blockchain
        node.getStructure().addToStructure(b);
    }
	
}
