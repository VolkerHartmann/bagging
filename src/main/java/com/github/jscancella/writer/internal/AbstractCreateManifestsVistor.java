package com.github.jscancella.writer.internal;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jscancella.domain.Manifest;
import com.github.jscancella.hash.Hasher;
import com.github.jscancella.internal.PathUtils;

/**
 * An implementation of the {@link SimpleFileVisitor} class that optionally avoids hidden files.
 * Mainly used in {@link BagCreator}
 */
public abstract class AbstractCreateManifestsVistor extends SimpleFileVisitor<Path>{
  private static final int _64_KB = 1024 * 64;
  private static final int CHUNK_SIZE = _64_KB;
  private static final Logger logger = LoggerFactory.getLogger(AbstractCreateManifestsVistor.class);
  private static final ResourceBundle messages = ResourceBundle.getBundle("MessageBundle");
  
  protected transient final Map<Manifest, Hasher> manifestToHasherMap;
  protected transient final boolean includeHiddenFiles;
  
  public AbstractCreateManifestsVistor(final Map<Manifest, Hasher> manifestToHasherMap, final boolean includeHiddenFiles){
    this.manifestToHasherMap = manifestToHasherMap;
    this.includeHiddenFiles = includeHiddenFiles;
  }
  
  public FileVisitResult abstractPreVisitDirectory(final Path dir, final String directoryToIgnore) throws IOException {
    if(!includeHiddenFiles && PathUtils.isHidden(dir) && !dir.endsWith(Paths.get(".bagit"))){
      logger.debug(messages.getString("skipping_hidden_file"), dir);
      return FileVisitResult.SKIP_SUBTREE;
    }
    if(dir.endsWith(directoryToIgnore)){ 
      logger.debug(messages.getString("skipping_ignored_directory"), dir);
      return FileVisitResult.SKIP_SUBTREE;
    }
    
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs)throws IOException{
    if(!includeHiddenFiles && PathUtils.isHidden(path) && !path.endsWith(".keep")){
      logger.debug(messages.getString("skipping_hidden_file"), path);
    }
    else{
      streamFile(path);
      for(final Entry<Manifest, Hasher> entry : manifestToHasherMap.entrySet()) {
        entry.getKey().getFileToChecksumMap().put(path, entry.getValue().getHash());
        entry.getValue().reset();
      }
    }
    
    return FileVisitResult.CONTINUE;
  }
  
  private void streamFile(final Path path) throws IOException {
    try(final InputStream is = new BufferedInputStream(Files.newInputStream(path, StandardOpenOption.READ))){
      final byte[] buffer = new byte[CHUNK_SIZE];
      int read = is.read(buffer);

      while(read != -1){
        for(final Hasher hasher : manifestToHasherMap.values()) {
          hasher.update(buffer, read);
        }
        read = is.read(buffer);
      }
    }
  }
}
