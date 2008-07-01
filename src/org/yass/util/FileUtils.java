package org.yass.util;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class FileUtils {

	/**
	 * 
	 * @param root
	 * @param filter
	 * @return a collection containing all the files matching the specified
	 *         filter
	 */
	public final static Collection<File> getFiles(final File root, final FileFilter filter) {
		final FileFilter folderFilter = new FileFilter() {

			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		};
		final Collection<File> files = new HashSet<File>();
		for (final File folder : root.listFiles(folderFilter))
			files.addAll(getFiles(folder, filter));
		files.addAll(Arrays.asList(root.listFiles(filter)));
		return files;
	}

	/**
	 * 
	 * @param extensions
	 * @return a {@link FileFilter} that will make the match for the specified
	 *         extensions
	 */
	public final static FileFilter getExtensionFilter(final String extensions) {
		final String[] exts = extensions.split(",");
		return new FileFilter() {

			public boolean accept(final File file) {
				for (final String ext : exts)
					if (file.getName().endsWith(".".concat(ext)))
						return true;
				return false;
			}
		};
	}
}
