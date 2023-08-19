package com.diabolo.eclipse.bitbucket.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.diabolo.eclipse.bitbucket.Activator;

import org.eclipse.ui.IWorkbench;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class BitBucketPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public BitBucketPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("BitBucket Configuration");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		addField(
				new StringFieldEditor(PreferenceConstants.P_PROTOCOL, "Protocol:", getFieldEditorParent()));

		addField(
				new StringFieldEditor(PreferenceConstants.P_HOST, "BitBucket URL:", getFieldEditorParent()));
		
		addField(
				new StringFieldEditor(PreferenceConstants.P_BASEPATH, "API Base Path:", getFieldEditorParent()));

		addField(
				new StringFieldEditor(PreferenceConstants.P_BBUSER, "BitBucket Username:", getFieldEditorParent()));

		addField(
				new StringFieldEditor(PreferenceConstants.P_BBPASSWORD, "BitBucket Password:", getFieldEditorParent()) {

			@Override
			    protected void doFillIntoGrid(Composite parent, int numColumns) {
			        super.doFillIntoGrid(parent, numColumns);

			        getTextControl().setEchoChar('*');
			    }

			});
		
		System.out.println(PreferenceConstants.P_HOST);
		System.out.println(PreferenceConstants.P_PROTOCOL);
		System.out.println(PreferenceConstants.P_BBUSER);
		System.out.println(PreferenceConstants.P_BBPASSWORD);
		System.out.println(PreferenceConstants.P_BASEPATH);

	}
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}