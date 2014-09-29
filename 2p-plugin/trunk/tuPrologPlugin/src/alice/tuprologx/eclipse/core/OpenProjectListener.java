package alice.tuprologx.eclipse.core;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IProject;

import alice.tuprologx.eclipse.properties.PropertyManager;

public class OpenProjectListener implements IResourceChangeListener {

	public OpenProjectListener() {
	}

	public void resourceChanged(IResourceChangeEvent event) {

		IResourceDelta delta = event.getDelta();

		IResourceDelta[] children = null;
		int flags = 0;
		if ((delta != null)
				&& (delta.getResource().getType() == IResource.ROOT)) {
			children = delta.getAffectedChildren(IResourceDelta.CHANGED,
					IResource.PROJECT);
			if (children.length != 0)
				flags = children[0].getFlags();
		}

		if ((flags & IResourceDelta.OPEN) != 0)

			if (((IProject) children[0].getResource()).isOpen())
				PropertyManager.configureProject((IProject) children[0]
						.getResource());

	}

}
