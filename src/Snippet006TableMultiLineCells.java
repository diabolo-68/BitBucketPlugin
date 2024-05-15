import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;

public class Snippet006TableMultiLineCells {

	private TableViewer viewer;

	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.FULL_SELECTION);

		viewer.setContentProvider(ArrayContentProvider.getInstance());
		createColumns();

		viewer.setLabelProvider(new OwnerDrawLabelProvider() {

			@Override
			protected void measure(Event event, Object element) {
				
				event.width = viewer.getTable().getColumn(event.index).getWidth();
				if (event.width == 0)
					return;
				
				String line = (String) element;
				
				Point size = event.gc.textExtent(line);
				int lines = size.x / event.width + 1;
				event.height = size.y * lines;	
				
				System.out.println("------------------------------");
				System.out.println("sizw.x" + size.x);
				System.out.println("event.width " + event.width);
				System.out.println("line:" + line);
				System.out.println("lines:" + lines);
				System.out.println("event.height:" + event.height);
				System.out.println("size.y:" + size.y);

			}

			@Override
			protected void paint(Event event, Object element) {

				String entry = (String) element;
				event.gc.drawText(entry, event.x, event.y, true);
			}
		});
		
		String[] entries = new String[3];
		entries[0] = "qqqqq";
		entries[1] = "abc\ndef\nghiabc\ndef\nghi";
		entries[2] = "qqqqq\nbbbbb";
		
		
		
		viewer.setInput(entries);

		GridData data = new GridData(GridData.GRAB_HORIZONTAL
				| GridData.GRAB_VERTICAL | GridData.FILL_BOTH);

		viewer.getControl().setLayoutData(data);
	}

	/**
	 * Create the columns to be used in the tree.
	 */
	private void createColumns() {
		TableLayout layout = new TableLayout();
		viewer.getTable().setLayout(layout);
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);

		TableColumn tc = new TableColumn(viewer.getTable(), SWT.NONE, 0);
		layout.addColumnData(new ColumnPixelData(350));
		tc.setText("Lines");

	}

	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display, SWT.CLOSE);
		shell.setSize(400, 400);
		shell.setLayout(new GridLayout());

		Snippet006TableMultiLineCells example = new Snippet006TableMultiLineCells();
		example.createPartControl(shell);

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}


}