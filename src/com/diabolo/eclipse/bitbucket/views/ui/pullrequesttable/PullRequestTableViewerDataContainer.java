package com.diabolo.eclipse.bitbucket.views.ui.pullrequesttable;

public class PullRequestTableViewerDataContainer {
	private String imageKey;
	private String key;
	private String value;
	private Integer index;
	private String url;
	

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the index
	 */
	public Integer getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(Integer index) {
		this.index = index;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public PullRequestTableViewerDataContainer(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public PullRequestTableViewerDataContainer(String key, String value, Integer index) {
		super();
		this.key = key;
		this.value = value;
		this.index = index;
	}

	public PullRequestTableViewerDataContainer(String imageKey, String key, String value, Integer index) {
		super();
		this.imageKey = imageKey;
		this.key = key;
		this.value = value;
		this.index = index;
	}

	public PullRequestTableViewerDataContainer(String imageKey, String key, String value, String url, Integer index) {
		super();
		this.imageKey = imageKey;
		this.key = key;
		this.value = value;
		this.index = index;
		this.url = url;
	}
	
	public String getImageKey() {
		return imageKey;
	}

	public void setImageKey(String imageKey) {
		this.imageKey = imageKey;
	}
	
}
