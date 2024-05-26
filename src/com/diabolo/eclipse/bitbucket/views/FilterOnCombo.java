package com.diabolo.eclipse.bitbucket.views;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import com.diabolo.eclipse.bitbucket.Activator;
import com.diabolo.eclipse.bitbucket.preferences.PreferenceConstants;

/*
 * By using composition, you avoid the SWTException related to subclassing
 * and maintain the necessary functionality in the application.
 */
public class FilterOnCombo extends Composite {

    private ComboViewer comboViewer;
	
    public FilterOnCombo(Composite parent, int style) {
        super(parent, style);
      
        GridLayout layout = new GridLayout(1, false);
        setLayout(layout);

        comboViewer = new ComboViewer(this, SWT.DROP_DOWN | SWT.READ_ONLY);
        comboViewer.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
        comboViewer.getCombo().setItems(new String[] {
			FilterOnComboValues.PULLREQUEST_TITLE,
			FilterOnComboValues.PULLREQUEST_SOURCE, 
			FilterOnComboValues.PULLREQUEST_TARGET, 
			FilterOnComboValues.PULLREQUEST_AUTHOR, 
			FilterOnComboValues.PULLREQUEST_REVIEWER
		});
		
        comboViewer.getCombo().setToolTipText("Select on which element the filter must apply to");
		
    	if (Activator.isCboFilterOnInitialized() == false) {
    		try {
    			comboViewer.getCombo().select(Activator.getStore().getInt(PreferenceConstants.P_DEFAULT_FILTERON));
    			Activator.setCboFilterOnInitialized(true);
    		} catch (Exception e) {
    			comboViewer.getCombo().select(0);
    		}
    	}		
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
