package com.github.jscancella.reader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleImmutableEntry;

import com.github.jscancella.domain.Bag;
import com.github.jscancella.domain.Version;
import com.github.jscancella.exceptions.InvalidBagMetadataException;
import com.github.jscancella.exceptions.InvalidBagitFileFormatException;
import com.github.jscancella.exceptions.MaliciousPathException;
import com.github.jscancella.exceptions.UnparsableVersionException;
import com.github.jscancella.reader.internal.BagitTextFileReader;
import com.github.jscancella.reader.internal.FetchReader;
import com.github.jscancella.reader.internal.ManifestReader;
import com.github.jscancella.reader.internal.MetadataReader;

/**
 * Responsible for reading a bag from the filesystem.
 */
public enum BagReader {; //using enum to ensure singleton
  
  /**
   * Reads the filesystem and creates a {@link Bag} object
   * 
   * @param bagDirectory the base directory of a bag on a filesystem
   * 
   * @return {@link Bag} object or throws an exception
   * 
   * @throws InvalidBagMetadataException If the metadata in the bag is improperly formatted
   * @throws IOException If there was a problem reading from the filesystem
   * @throws UnparsableVersionException If there was a problem parsing the version
   * @throws InvalidBagitFileFormatException If one of the files are formatted incorrectly
   * @throws MaliciousPathException If a manifest is trying to access a file outside the bag
   */
  public static Bag read(final Path bagDirectory) 
      throws InvalidBagMetadataException, IOException, UnparsableVersionException, InvalidBagitFileFormatException, MaliciousPathException {
    
    final Bag bag = new Bag();
    final Path bagitFile = bagDirectory.resolve("bagit.txt");
    final SimpleImmutableEntry<Version, Charset> bagitInfo = BagitTextFileReader.readBagitTextFile(bagitFile);
    bag.setVersion(bagitInfo.getKey());
    bag.setFileEncoding(bagitInfo.getValue());
    bag.setRootDir(bagDirectory);
    
    ManifestReader.readAllManifests(bagDirectory, bag);
    
    bag.getMetadata().addAll(MetadataReader.readBagMetadata(bagDirectory, bag.getFileEncoding()));
    
    final Path fetchFile = bagDirectory.resolve("fetch.txt");
    if(Files.exists(fetchFile)){
      bag.getItemsToFetch().addAll(FetchReader.readFetch(fetchFile, bag.getFileEncoding(), bag.getRootDir()));
    }
    
    return bag;
  }
}
