package com.aspirephile.physim.core;

public class PhySimOptions {
	private static class defaults {
		public static int FPS = 60;
		public static boolean useGL2 = true;
		public static final boolean useFullScreen = true;
	}

	private int FPS = defaults.FPS;
	private boolean useGL2 = defaults.useGL2;
	private boolean useFullScreen = defaults.useFullScreen;;

	public PhySimOptions() {
		setDefaults();
	}

	private void setDefaults() {
		setFPS(defaults.FPS);
		setUseGL2(defaults.useGL2);
	}

	public int getFPS() {
		return FPS;
	}

	public boolean getGL2() {
		return useGL2;
	}

	public boolean useFullScreen() {
		return useFullScreen;
	}

	public void setFPS(int FPS) {
		if (FPS > 0)
			this.FPS = FPS;
	}

	public void setUseGL2(boolean useGL2) {
		this.useGL2 = useGL2;
	}

	public void setUseFullScreen(boolean useFullScreen) {
		this.useFullScreen = useFullScreen;
	}

}
