package org.Epixcrafted.EpixServer.engine.io;

public interface IThreadedFileIO {
	/**
	 * Returns a boolean stating if the write was unsuccessful.
	 */
	boolean writeNextIO();
}
