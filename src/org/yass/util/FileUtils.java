package org.yass.util;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;

public class FileUtils {

	public final static File createFolder(final String path) {
		final File file = new File(path);
		if (!file.exists())
			file.mkdir();
		return file;
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

	/**
	 * 
	 * @param root
	 * @param filter
	 * @return a collection containing all the files matching the specified filter
	 */
	public final static Collection<File> getFiles(final File root, final FileFilter filter) {
		final FileFilter folderFilter = new FileFilter() {

			public boolean accept(final File pathname) {
				return pathname.isDirectory();
			}
		};
		final Collection<File> files = new HashSet<File>();
		File[] rootFiles = root.listFiles(folderFilter);
		for (final File folder : rootFiles)
			files.addAll(getFiles(folder, filter));
		rootFiles = root.listFiles(filter);
		files.addAll(Arrays.asList(rootFiles));
		return files;
	}

	@SuppressWarnings("unchecked")
	public static void sortFiles(final File[] rootFiles) {
		Arrays.sort(rootFiles, new Comparator() {

			public int compare(final Object o1, final Object o2) {
				return ((File) o1).getPath().compareTo(((File) o2).getPath());
			}
		});
	}
}
