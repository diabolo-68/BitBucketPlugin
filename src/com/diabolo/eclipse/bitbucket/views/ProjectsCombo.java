package com.diabolo.eclipse.bitbucket.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import com.diabolo.eclipse.bitbucket.Activator;
import com.diabolo.eclipse.bitbucket.BitBucketServices;

/*
 * By using composition, you avoid the SWTException related to subclassing
 * and maintain the necessary functionality in your application.
 */
public class ProjectsCombo extends Composite {

    private Combo combo;
    private PullRequestsView view;

    public ProjectsCombo(Composite parent, PullRequestsView view, int style) {
        super(parent, style);
        this.view = view;
        
        GridLayout layout = new GridLayout(1, false);
        setLayout(layout);
        
        combo = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
        combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        combo.setText("Project");
        combo.addSelectionListener(new ProjectsComboSelectionListener(view));
    }

    public void fillCboProjects() {
        System.out.println("fillCboProjects");

        combo.removeAll();
        combo.add("All");

        if (Activator.getServices().projectsValues != null) {
        	Activator.getServices().projectsValues.forEach(projectValue -> {
                combo.add(projectValue.getName());
                combo.setData(projectValue.getName(), projectValue);
            });
        }
        combo.select(0);
        combo.update();
    }

    public Combo getCombo() {
        return combo;
    }
    
    public int getSelectionIndex() {
    	return combo.getSelectionIndex();
    }
    
    public String getItem(int index) {
    	return combo.getItem(index);
    }
    
    public Object getData() {
    	return combo.getData();
    }

    public Object getData(String key) {
    	return combo.getData(key);
    }
    
}
