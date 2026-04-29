package dom.institution.lab.cns.bitcoin.node.stubs;


import dom.institution.lab.cns.bitcoin.node.BitcoinNode;
import dom.institution.lab.cns.bitcoin.structure.Blockchain;
import dom.institution.lab.cns.engine.Simulation;
import dom.institution.lab.cns.engine.event.Event_ContainerValidation;
import dom.institution.lab.cns.engine.transaction.ITxContainer;
import dom.institution.lab.cns.engine.transaction.Transaction;
import dom.institution.lab.cns.engine.transaction.TransactionGroup;

public class BitcoinNode4Test extends BitcoinNode {

	public BitcoinNode4Test(Simulation sim) {
        this.sim = sim;
        this.pool = new TransactionGroup();
        this.ID = 1;
		
		this.blockchain = new Blockchain();
		this.miningPool = new TransactionGroup();
		this.minValueToMine = 0;
		this.minSizeToMine = 0;
		this.operatingDifficulty = 0d;
	}
	
	@Override
	public long scheduleValidationEvent(ITxContainer b, long time) {
		Event_ContainerValidation e = new Event_ContainerValidation(b, this, time);
		this.nextValidationEvent = e;
		return(time);
	}
	
	@Override
	public void broadcastTransaction(Transaction t, long time) {
		//Just sit back, relax, have a sip of tea... 
	}
	
	@Override
	public void broadcastContainer(ITxContainer txc, long time) {
		//Or maybe take a nap!
	}
}
