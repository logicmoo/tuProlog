package alice.tuprologx.eclipse.scanners;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.presentation.IPresentationDamager;
import org.eclipse.jface.text.presentation.IPresentationRepairer;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;

/**
 * An implementation of a syntax driven presentation damager and presentation
 * repairer. Used in list partition returns the entire partition.
 */
public class ListDamagerRepairer extends DefaultDamagerRepairer implements
		IPresentationDamager, IPresentationRepairer {

	/**
	 * Creates a damager/repairer that uses the given scanner and returns the
	 * given default text attribute if the current token does not carry a text
	 * attribute.
	 */
	public ListDamagerRepairer(ITokenScanner scanner) {
		super(scanner);
	}

	// ---- IPresentationDamager
	/*
	 * @see IPresentationDamager#getDamageRegion(ITypedRegion, DocumentEvent,
	 * boolean)
	 */
	public IRegion getDamageRegion(ITypedRegion partition, DocumentEvent e,
			boolean documentPartitioningChanged) {
		return partition;
	}
}
