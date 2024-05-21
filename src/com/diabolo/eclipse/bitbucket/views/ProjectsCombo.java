package com.diabolo.eclipse.bitbucket.views;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import com.diabolo.eclipse.bitbucket.Activator;
import com.diabolo.eclipse.bitbucket.preferences.PreferenceConstants;

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
        gridData.widthHint = Activator.getStore().getInt(PreferenceConstants.P_COMBOSIZE);
        comboViewer.getCombo().setLayoutData(gridData);
        comboViewer.getCombo().setText("Project");
        comboViewer.getCombo().addSelectionListener(new ProjectsComboSelectionListener());
    }

    public void fillCboProjects() {
        System.out.println("fillCboProjects");

        comboViewer.getCombo().removeAll();
        comboViewer.getCombo().add("All");
        comboViewer.getCombo().select(0);

        if (Activator.getServices().projectsValues != null) {
        	Activator.getServices().projectsValues.forEach(projectValue -> {
                comboViewer.getCombo().add(projectValue.getName());
                comboViewer.getCombo().setData(projectValue.getName(), projectValue);
            });
        	/*
        	 * Set the default value
        	 * If this corresponds to a project that no longer exists,
        	 * select "All" 
        	 */
        	try {
        		System.out.println("Project default:" + Activator.getStore().getInt(PreferenceConstants.P_DEFAULT_PROJECT));
        		comboViewer.getCombo().select(Activator.getStore().getInt(PreferenceConstants.P_DEFAULT_PROJECT));
        	} catch (Exception e) {
        		comboViewer.getCCombo().select(0);
        	}
        }
        comboViewer.getCombo().update();
    }

    public Combo getCombo() {
        return comboViewer.getCombo();
    }
    
    public int getSelectionIndex() {
    	return comboViewer.getCombo().getSelectionIndex();
    }
    
    public String getItem(int index) {
    	return comboViewer.getCombo().getItem(index);
    }
    
    public Object getData() {
    	return comboViewer.getCombo().getData();
    }

    public Object getData(String key) {
    	return comboViewer.getCombo().getData(key);
    }
    
}
