package com.diabolo.eclipse.bitbucket.views;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import com.diabolo.eclipse.bitbucket.Activator;

/*
 * By using composition, you avoid the SWTException related to subclassing
 * and maintain the necessary functionality in your application.
 */
public class ProjectsCombo extends Composite {

    private ComboViewer comboViewer;
    
    public ProjectsCombo(Composite parent, int style) {
        super(parent, style);
        
        GridLayout layout = new GridLayout(1, false);
        setLayout(layout);
        
        comboViewer = new ComboViewer(this, SWT.DROP_DOWN | SWT.READ_ONLY);
        // Set a fixed width for the Combo
        GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gridData.widthHint = 150;  // Set desired width in pixels
        comboViewer.getCombo().setLayoutData(gridData);
        comboViewer.getCombo().setText("Project");
        comboViewer.getCombo().addSelectionListener(new ProjectsComboSelectionListener());
    }

    public void fillCboProjects() {
        System.out.println("fillCboProjects");

        comboViewer.getCombo().removeAll();
        comboViewer.getCombo().add("All");

        if (Activator.getServices().projectsValues != null) {
        	Activator.getServices().projectsValues.forEach(projectValue -> {
                comboViewer.getCombo().add(projectValue.getName());
                comboViewer.getCombo().setData(projectValue.getName(), projectValue);
            });
        }
        comboViewer.getCombo().select(0);
        comboViewer.getCombo().update();
    }

    public Combo getCombo() {
        return comboViewer.getCombo();
    }
    
    public int getSelectionIndex() {
    	System.out.println(comboViewer.getCombo().getSelectionIndex());
    	return comboViewer.getCombo().getSelectionIndex();
    }
    
    public String getItem(int index) {
    	return comboViewer.getCombo().getItem(index);
    }
    
    public Object getData() {
    	return comboViewer.getCombo().getData();
    }

    public Object getData(String key) {
    	System.out.println(comboViewer.getCombo().getData(key));
    	return comboViewer.getCombo().getData(key);
    }
    
}
