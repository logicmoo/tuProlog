package alice.tuprologx.eclipse.views;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

public class TheoryView extends ViewPart {

	private TreeViewer viewer;
	@SuppressWarnings("unused")
	private DrillDownAdapter drillDownAdapter;

	// private Action show;

	class TreeObject implements IAdaptable {

		private String name;
		private TreeParent parent;

		public TreeObject(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public TreeParent getParent() {
			return parent;
		}

		public void setParent(TreeParent parent) {
			this.parent = parent;
		}

		public Object getAdapter(Class adapter) {
			// TODO Auto-generated method stub
			return null;
		}

		public String toString() {
			return getName();
		}

	}

	class TreeParent extends TreeObject {

		private ArrayList<TreeObject> children;

		public TreeParent(String name) {
			super(name);
			children = new ArrayList<TreeObject>();
			// TODO Auto-generated constructor stub
		}

		public void addChild(TreeObject child) {
			children.add(child);
			child.setParent(this);
		}

		public void removeChild(TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}

		public TreeObject[] getChildren() {
			return children.toArray(new TreeObject[children
					.size()]);
		}

		public boolean hasChildren() {
			return children.size() > 0;
		}

	}

	class ViewContentProvider implements IStructuredContentProvider,
			ITreeContentProvider {

		private TreeParent invisibleRoot;

		public Object[] getChildren(Object parent) {
			// TODO Auto-generated method stub
			if (parent instanceof TreeParent) {
				return ((TreeParent) parent).getChildren();
			}
			return new Object[0];
		}

		public Object getParent(Object child) {
			// TODO Auto-generated method stub
			if (child instanceof TreeObject) {
				return ((TreeObject) child).getParent();
			}
			return null;
		}

		public boolean hasChildren(Object parent) {
			// TODO Auto-generated method stub
			if (parent instanceof TreeParent)
				return ((TreeParent) parent).hasChildren();
			return false;
		}

		public Object[] getElements(Object parent) {
			// TODO Auto-generated method stub
			if (parent.equals(getViewSite())) {
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}

		public void dispose() {
			// TODO Auto-generated method stub

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub

		}

		private void initialize(String theoryText) {
			invisibleRoot = new TreeParent("");
			StringTokenizer tk = new StringTokenizer(theoryText);
			int i = 0, j = 0;
			TreeParent root = new TreeParent("");
			while (tk.hasMoreTokens()) {
				String element = tk.nextToken("\n");
				if (element.contains("***")) {
					if (j != 0) {
						invisibleRoot.addChild(root);
					}
					j = 0;
					i++;
					TreeParent rootTemp = new TreeParent("Engine" + i);
					root = rootTemp;
				} else {
					if (element.equals("\n")) {
					} else {
						root.addChild(new TreeObject(element));
						j++;
					}
				}
			}
			if (!tk.hasMoreTokens()) {
				invisibleRoot.addChild(root);
			}
		}

	}

	class ViewLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			return obj.toString();
		}

		public Image getImage(Object obj) {
			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			if (obj instanceof TreeParent)
				imageKey = ISharedImages.IMG_OBJ_FOLDER;
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(imageKey);
		}

	}

	class NameSorter extends ViewerSorter {

	}

	public TheoryView() {

	}

	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());

	}

	public void setFocus() {

	}

	// metodo che verrà invocato dal builder dopo un eventuale build
	public void refresh(String theoryText) {
		((ViewContentProvider) viewer.getContentProvider())
				.initialize(theoryText);
		viewer.refresh();
	}

}
