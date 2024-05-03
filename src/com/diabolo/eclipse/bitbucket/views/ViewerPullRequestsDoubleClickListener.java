package com.diabolo.eclipse.bitbucket.views;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;

import com.diabolo.eclipse.bitbucket.api.pullrequestforrepository.Value;

public class ViewerPullRequestsDoubleClickListener implements IDoubleClickListener {
	
	public ViewerPullRequests viewerPullRequests;
	
	public void doubleClick(DoubleClickEvent event) {
	
		if(event.getSelection() instanceof IStructuredSelection) {
		
			IStructuredSelection selection = viewerPullRequests.getStructuredSelection();						
			PullRequestsTreeObject obj = (PullRequestsTreeObject) selection.getFirstElement();
			System.out.println("-1");
			if (obj != null) {
			   
			   System.out.println("0");
			   
			   if (obj.getData() instanceof Value) {
				   try {
					   System.out.println("1");
					   IWebBrowser browser = PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser();
	    				   
					   ((Value) obj.getData()).getLinks().getSelf().forEach(currentSelf -> {
						   URL url;
						   try {
							   url = new URL(currentSelf.getHref().toString());
							   browser.openURL(url);
						   } catch (MalformedURLException | PartInitException e) {
							   e.printStackTrace();
						   }
					   });
	    				   
				   } catch (Exception e) {
					   System.out.println(e);
				   }
			   }
			}
		}
	}
}