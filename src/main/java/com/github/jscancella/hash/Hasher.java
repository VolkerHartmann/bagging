package com.github.jscancella.hash;

import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

/**
 * The interface that any supported checksum algorithm must implement. 
 * It is assumed that the implementation is a singleton and thread safe.
 */
public interface Hasher {

  /**
   * Create a HEX formatted string checksum hash of the file
   * 
   * @param path the {@link Path} (file) to hash
   * 
   * @return the hash as a hex formated string
   * 
   * @throws IOException if there is a problem reading the file
   * @throws NoSuchAlgorithmException if there is a problem setting up the algorithm for hashing
   */
  public String hash(final Path path) throws IOException, NoSuchAlgorithmException;
  
  /**
   * For calculating large file checksums it is more efficient to stream the file, thus the need to be able to update a checksum.
   * <b>NOT THREAD SAFE</b> 
   * 
   * @param bytes the bytes with which to update the checksum
   * @param length the number of bytes to update from the array
   * 
   * @throws NoSuchAlgorithmException if there is a problem setting up the algorithm for hashing
   */
  public void update(final byte[] bytes, final int length) throws NoSuchAlgorithmException;
  
  /**
   * @return the checksum of the streamed file. If no file has been streamed, returns an empty string.
   */
  public String getHash();
  
  /**
   * When streaming a file, we have no way of knowing when we are done updating. This method allows for a reset of the current stream.
   */
  public void reset();
  
  /**
   * @return the bagit formatted version of the algorithm name. For example if the hasher implements MD5, it would return md5 as the name. 
   */
  public String getBagitAlgorithmName();
}
