package com.diabolo.eclipse.bitbucket.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

import com.diabolo.eclipse.bitbucket.Activator;

/*
 * By using composition, we avoid the SWTException related to subclassing
 * and maintain the necessary functionality in your application.
 */
public class RepositoriesCombo extends Composite {

    private Combo combo;
    private PullRequestsView view;

    public RepositoriesCombo(Composite parent, PullRequestsView view, int style) {
        super(parent, style);
        this.view = view;
        
        // Set layout for the composite
        GridLayout layout = new GridLayout(1, false);
        setLayout(layout);
        
        // Initialize the Combo widget
        combo = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
        combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        combo.setText("Repository");
        combo.setEnabled(false);
        combo.notifyListeners(SWT.Selection, new Event());
        combo.addSelectionListener(new RepositoriesComboSelectionAdapter(view));
    }

    public void fill(ProjectsCombo cboProjects) {
        System.out.println("fillCboRepositories");

        combo.removeAll();
        combo.add("All");
        combo.select(0);

        // First combo's entry is always "All"
        if (cboProjects.getCombo().getSelectionIndex() == 0) {
            combo.setEnabled(false);
        } else {
            combo.setEnabled(true);
            com.diabolo.eclipse.bitbucket.api.Projects.Value projectValue = 
                (com.diabolo.eclipse.bitbucket.api.Projects.Value) cboProjects.getCombo().getData(
                    cboProjects.getCombo().getItem(cboProjects.getCombo().getSelectionIndex())
                );

            if (Activator.getServices().repositoriesValues != null) {
            	Activator.getServices().repositoriesValues.forEach(repository -> {
                    int projectValueId = -1;
                    if (projectValue != null) {
                        projectValueId = projectValue.getId();
                    }

                    if (repository.getProject().getId() == projectValueId || cboProjects.getCombo().getSelectionIndex() == 0) {
                        if (repository.getProject().getType().compareTo("NORMAL") == 0) {
                            String cboRepositoryValue = repository.getName();
                            combo.add(cboRepositoryValue);
                            combo.setData(cboRepositoryValue, repository);
                        }
                    }
                });
            }

            combo.update();
        }
    }

    public Combo getCombo() {
        return combo;
    }
    
    public void select(int index) {
        combo.select(index);
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
 
