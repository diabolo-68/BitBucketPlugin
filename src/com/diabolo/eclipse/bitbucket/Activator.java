package com.diabolo.eclipse.bitbucket;

import java.net.URL;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.BundleContext;

import com.diabolo.eclipse.bitbucket.views.PullRequestsView;
import com.diabolo.eclipse.bitbucket.views.ui.pullrequeststree.PullRequestTreeViewer;

public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "com.diabolo.eclipse.bitbucket"; //$NON-NLS-1$

    public static final String ICON_PROJECT = "icons/project16.gif";
    public static final String ICON_AUTHOR = "icons/person-circle24.png";
    public static final String ICON_SOURCE = "icons/sources24.png";
    public static final String ICON_BRANCHES = "icons/branches24.png";
    public static final String ICON_SYMBOLS = "icons/symbols24.png";
    public static final String ICON_COMMENT = "icons/comments24.png";
    public static final String ICON_PERSON_WITH_CROSS = "icons/person-with-cross24.png";
    public static final String ICON_PERSON_WITH_TICK = "icons/person-with-tick24.png";
    public static final String ICON_WARNINGS = "icons/warnings24.png";
    public static final String ICON_ERRORSTATE = "platform:/plugin/org.eclipse.ui/icons/full/progress/errorstate.png";
    public static final String ICON_COLLAPSEALL = "platform:/plugin/org.eclipse.ui/icons/full/elcl16/collapseall.png";
    public static final String ICON_EXPANDALL = "platform:/plugin/org.eclipse.ui/icons/full/elcl16/expandall.png";
    
    private static ImageRegistry imageRegistry;
    
	private static Activator plugin;

	private static BitBucketServices services;
	
	private static PullRequestsView pullRequestView;
	
    private static ScopedPreferenceStore store = new ScopedPreferenceStore(InstanceScope.INSTANCE, "com.diabolo.eclipse.bitbucket");

	/**
	 * @return the store
	 */
	public static ScopedPreferenceStore getStore() {
		return store;
	}

	
	/**
	 * @return the pullRequestView
	 */
	public static PullRequestsView getPullRequestView() {
		return pullRequestView;
	}

	/**
	 * @param pullRequestView the pullRequestView to set
	 */
	public static void setPullRequestView(PullRequestsView pullRequestView) {
		Activator.pullRequestView = pullRequestView;
	}

	/**
	 * @return the services
	 */
	public static BitBucketServices getServices() {
		return services;
	}

	public Activator() {
		plugin = this;
		initializeImageRegistry(getImageRegistry());
		services = new BitBucketServices();
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
        if (imageRegistry != null) {
            imageRegistry.dispose();
            imageRegistry = null;
        }		
        plugin = null;
	}

	/**
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

    
    @Override
    protected void initializeImageRegistry(ImageRegistry registry) {
    	
        imageRegistry = registry;
        registerImage(ICON_PROJECT, ICON_PROJECT);
        registerImage(ICON_AUTHOR, ICON_AUTHOR);
        registerImage(ICON_SOURCE, ICON_SOURCE);
        registerImage(ICON_BRANCHES, ICON_BRANCHES);
        registerImage(ICON_SYMBOLS, ICON_SYMBOLS);
        registerImage(ICON_COMMENT, ICON_COMMENT);
        registerImage(ICON_PERSON_WITH_CROSS, ICON_PERSON_WITH_CROSS);
        registerImage(ICON_PERSON_WITH_TICK, ICON_PERSON_WITH_TICK);
        registerImage(ICON_WARNINGS, ICON_WARNINGS);
        registerImage(ICON_ERRORSTATE);
        registerImage(ICON_COLLAPSEALL);
        registerImage(ICON_EXPANDALL);
    }


    public static ImageDescriptor getImageDescriptor(String key) {
    	
    	if (imageRegistry == null) {
            return ImageDescriptor.getMissingImageDescriptor();
        }
        return imageRegistry.getDescriptor(key);
    }

    
    private void registerImage(String key, String path) {
        ImageDescriptor descriptor = imageDescriptorFromPlugin(PLUGIN_ID, path);
        imageRegistry.put(key, descriptor);
    }

    private void registerImage(String key) {
    	try {
    		ImageDescriptor descriptor = ImageDescriptor.createFromURL(new URL(key));
    		imageRegistry.put(key, descriptor);
    	} catch (Exception e) {
    	}
    }	
    
    public static Image getImage(String key) {
    	
    	if (imageRegistry == null) {
            return null; // or return a default image if you.setBackground(new Color(255,255,255) prefer
        }
        Image image = imageRegistry.get(key);
        return image;
    }    
    
    // replace viewerPullRequests
	public static void showMessage(PullRequestTreeViewer viewerPullRequests, String message ) {
		MessageDialog.openInformation(viewerPullRequests.getControl().getShell(), "Pull Requests", message);
	}
    
 }
