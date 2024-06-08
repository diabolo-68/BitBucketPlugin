package com.diabolo.eclipse.bitbucket.views.ui.pullrequeststree;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.program.Program;

import com.diabolo.eclipse.bitbucket.api.pullrequestforrepository.Value;

public class PullRequestTreeViewerDoubleClickListener implements IDoubleClickListener{

	@Override
	  public void doubleClick(DoubleClickEvent event) {
  	    IStructuredSelection selection = (IStructuredSelection) event.getSelection();
        Object firstElement = selection.getFirstElement();
        if (firstElement != null) {
            PullRequestTreeViewerDataContainer prValueData = (PullRequestTreeViewerDataContainer) firstElement;
			if (prValueData.getData() instanceof Value) {
				Value prValue = ((Value) prValueData.getData());
				String url = prValue.getLinks().getSelf().get(0).getHref();
                Program.launch(url);
			}
        }
  	  }

}
