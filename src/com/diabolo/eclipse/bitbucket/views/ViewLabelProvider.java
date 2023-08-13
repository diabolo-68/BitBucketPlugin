package com.diabolo.eclipse.bitbucket.views;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;

class ViewLabelProvider extends LabelProvider {

	public String getText(Object obj) {
		return obj.toString();
	}

	public Image getImage(Object obj, IWorkbench workbench) {
		String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
		if (obj instanceof PullRequestsTreeParent)
			imageKey = ISharedImages.IMG_OBJ_FOLDER;
		return workbench.getSharedImages().getImage(imageKey);
	}
}
