package com.github.jscancella.hash.internal;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements {@link SimpleFileVisitor} to calculate the PayloadOxum
 */
public class FileCountAndTotalSizeVistor extends SimpleFileVisitor<Path> {
  private static final Logger logger = LoggerFactory.getLogger(FileCountAndTotalSizeVistor.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  
  private transient long totalSize;
  private transient long count;

  @Override
  public FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs) throws IOException{
    count++;
    final long size = Files.size(path);
    logger.debug(messages.getString("file_size_in_bytes"), path, size);
    totalSize += size;
    
    return FileVisitResult.CONTINUE;
  }

  public long getCount() {
    return count;
  }

  public long getTotalSize() {
    return totalSize;
  }
}
