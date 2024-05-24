package com.diabolo.eclipse.bitbucket.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import com.diabolo.eclipse.bitbucket.Activator;
import com.diabolo.eclipse.bitbucket.UrlProtocol;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		Activator.getStore().setDefault(PreferenceConstants.P_BASEPATH,"rest");
		Activator.getStore().setDefault(PreferenceConstants.P_HOST,"");
		Activator.getStore().setDefault(PreferenceConstants.P_PROTOCOL, UrlProtocol.https.toString());
		Activator.getStore().setDefault(PreferenceConstants.P_BBUSER, "");
		Activator.getStore().setDefault(PreferenceConstants.P_BBPASSWORD, "");
		Activator.getStore().setDefault(PreferenceConstants.P_COMBOSIZE, 150);
		Activator.getStore().setDefault(PreferenceConstants.P_DEFAULT_PROJECT, "All");
		Activator.getStore().setDefault(PreferenceConstants.P_DEFAULT_REPOSITORY, "All");
	}

}
