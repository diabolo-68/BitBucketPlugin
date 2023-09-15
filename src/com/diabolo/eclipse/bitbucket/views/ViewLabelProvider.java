package com.diabolo.eclipse.bitbucket.views;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

class ViewLabelProvider extends LabelProvider {

    private Image expandedIcon = ImageDescriptor.createFromFile(ViewLabelProvider.class, "/icons/full/elcl16/expandall.png").createImage();
    private Image collapsedIcon = ImageDescriptor.createFromFile(ViewLabelProvider.class, "/icons/full/elcl16/collapseall.png").createImage();
    
	public String getText(Object obj) {
		return obj.toString();
	}

	@Override
	public Image getImage(Object obj) {
		
		if (obj instanceof PullRequestsTreeParent) {
			
                return collapsedIcon;
		}
		
		return null;
	}
}
