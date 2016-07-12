package com.agiliumlabs.smev.ws.ds.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

public class FileUtils {

	public static byte[] readFile(String fileName) throws IOException {
		Path path = Paths.get(fileName);
		byte[] data = Files.readAllBytes(path);
		return data;
	}

	public static String readUTF8File(String fileName) throws IOException {
		Reader is = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
		try {
			final int bufsize = 4096;
			char[] data = new char[bufsize];
			int used = 0;
			while (true) {
				if (data.length - used < bufsize) {
					char[] newData = new char[data.length << 1];
					System.arraycopy(data, 0, newData, 0, used);
					data = newData;
				}
				int got = is.read(data, used, data.length - used);
				if (got <= 0)
					break;
				used += got;
			}
			return new String(data, 0, used);
		} finally {
			is.close();
		}
	}

	public static void deleteFolder(String folderPath) {
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				listOfFiles[i].delete();
			}
		}
		new File(folderPath).delete();
	}
	
	public static void writeFile(String fileName, byte[] bytes) throws IOException {
		Path path = Paths.get(fileName);
		Files.write(path, bytes, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
	}

	public static void addFile2Zip(ZipOutputStream zip, String fileName) throws FileNotFoundException, IOException {
		File file = new File(fileName);
		String name = file.getName();
		ZipEntry zipEntry = new ZipEntry(name);
		zip.putNextEntry(zipEntry);
		FileInputStream fis = new FileInputStream(file);
		IOUtils.copy(fis, zip);
		zip.closeEntry();
		fis.close();
	}

}
