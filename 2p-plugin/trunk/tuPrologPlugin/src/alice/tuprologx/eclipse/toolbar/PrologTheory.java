package alice.tuprologx.eclipse.toolbar;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;

import alice.tuprologx.eclipse.wizards.PrologWizard;

public class PrologTheory extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		// MessageDialog.openInformation(null,null,"HelloWorld");
		// Create the wizard
		PrologWizard wizard = new PrologWizard();
		IStructuredSelection selection = null;
		wizard.init(PlatformUI.getWorkbench(), selection);

		// Create the wizard dialog
		WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), wizard);
		// Open the wizard dialog
		dialog.open();
		return null;
	}

}
