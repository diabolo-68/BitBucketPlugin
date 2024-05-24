package com.diabolo.eclipse.bitbucket.views;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import com.diabolo.eclipse.bitbucket.Activator;
import com.diabolo.eclipse.bitbucket.preferences.PreferenceConstants;

/*
 * By using composition, we avoid the SWTException related to subclassing
 * and maintain the necessary functionality in the application.
 */
public class RepositoriesCombo extends Composite {

    private ComboViewer comboViewer;
    private ScopedPreferenceStore store = new ScopedPreferenceStore(InstanceScope.INSTANCE, "com.diabolo.eclipse.bitbucket");
    
    public RepositoriesCombo(Composite parent, int style) {
        super(parent, style);
        
        // Set layout for the composite
        GridLayout layout = new GridLayout(1, false);
        setLayout(layout);
        
        // Initialize the Combo widget
        
        comboViewer = new ComboViewer(this, SWT.DROP_DOWN | SWT.READ_ONLY);
        // Set a fixed width for the Combo
        GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gridData.widthHint = store.getInt(PreferenceConstants.P_COMBOSIZE);
        comboViewer.getCombo().setLayoutData(gridData);
        comboViewer.getCombo().setText("Repositories");
        
        comboViewer.getCombo().notifyListeners(SWT.Selection, new Event());
        comboViewer.getCombo().addSelectionListener(new RepositoriesComboSelectionListener());
        comboViewer.getCombo().setEnabled(false);
    }

    public void fill(ProjectsCombo cboProjects) {
        		
        comboViewer.getCombo().removeAll();
        comboViewer.add("All");
        comboViewer.getCombo().select(0);
        
        // First combo's entry is always "All"
        if (cboProjects.getCombo().getSelectionIndex() != 0) {
        	
        	
	        com.diabolo.eclipse.bitbucket.api.Projects.Value projectValue = 
	            (com.diabolo.eclipse.bitbucket.api.Projects.Value) cboProjects.getCombo().getData(
	                cboProjects.getCombo().getItem(cboProjects.getCombo().getSelectionIndex())
	            );
	
	        if (Activator.getServices().repositoriesValues != null) {
	        	comboViewer.getCombo().setEnabled(true);
	        	Activator.getServices().repositoriesValues.forEach(repository -> {
	                int projectValueId = -1;
	                if (projectValue != null) {
	                    projectValueId = projectValue.getId();
	                }
	
	                if (repository.getProject().getId() == projectValueId || cboProjects.getCombo().getSelectionIndex() == 0) {
	                    if (repository.getProject().getType().compareTo("NORMAL") == 0) {
	                        String cboRepositoryValue = repository.getName();
	                        comboViewer.add(cboRepositoryValue);
	                        comboViewer.setData(cboRepositoryValue, repository);
	                    }
	                }
	            });
	        	/*
	        	 * Set the default value
	        	 * If this corresponds to a repository that no longer exists,
	        	 * select "All" 
	        	 */
	        	if (Activator.isCboRepositoriesInitialited() == false) {
	        		try {
	        			comboViewer.getCombo().select(Activator.getStore().getInt(PreferenceConstants.P_DEFAULT_REPOSITORY));
	        			Activator.setCboRepositoriesInitialited(true);
	        		} catch (Exception e) {
	        			comboViewer.getCCombo().select(0);
	        		}
	        	}
            }
        } else {
        	comboViewer.getCombo().setEnabled(false);
        }
        
        comboViewer.getCombo().update();
    }

    public Combo getCombo() {
        return comboViewer.getCombo();
    }
    
    public void select(int index) {
        comboViewer.getCombo().select(index);
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
    	return comboViewer.getData(key);
    }
    
}   
 
