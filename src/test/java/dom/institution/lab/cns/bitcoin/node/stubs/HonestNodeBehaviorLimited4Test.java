package dom.institution.lab.cns.bitcoin.node.stubs;

import dom.institution.lab.cns.bitcoin.node.BitcoinNode;
import dom.institution.lab.cns.bitcoin.node.HonestNodeBehaviorLimited;
import dom.institution.lab.cns.bitcoin.structure.Block;
import dom.institution.lab.cns.engine.transaction.Transaction;
import dom.institution.lab.cns.engine.transaction.TxValuePerSizeComparator;

public class HonestNodeBehaviorLimited4Test extends HonestNodeBehaviorLimited {

	public HonestNodeBehaviorLimited4Test(BitcoinNode node) {
		super(node);
	}
	
	public boolean conflictFree(Transaction t) {
		return(true);
	}
	
    public boolean dependenciesPresent(Transaction t) {
    	return(true);
    }
    
	@Override
    protected void reconstructMiningPool() {
		node.setMiningPool(node.getPool().getTopN(5000000, 
				new TxValuePerSizeComparator()));
	}
	
	@Override
	protected Block getConflictBlock(Block b) {
		int prev = Block.getCurrID();
		Block c = new Block();
		Block.setCurrID(prev);
    	return (c);
    }
	
	
}
