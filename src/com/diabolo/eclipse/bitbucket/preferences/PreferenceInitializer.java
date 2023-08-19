package com.diabolo.eclipse.bitbucket.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

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
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_BASEPATH,"rest");
		store.setDefault(PreferenceConstants.P_HOST,"git.osiv.ch");
		store.setDefault(PreferenceConstants.P_PROTOCOL, UrlProtocol.https.toString());
		store.setDefault(PreferenceConstants.P_BBUSER, "");
		store.setDefault(PreferenceConstants.P_BBPASSWORD, "");
	}

}
