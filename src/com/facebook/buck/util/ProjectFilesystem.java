/*
 * Copyright 2012-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.facebook.buck.util;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * An injectable service for interacting with the filesystem.
 */
public class ProjectFilesystem {

  private final File projectRoot;

  public ProjectFilesystem(File projectRoot) {
    this.projectRoot = Preconditions.checkNotNull(projectRoot);
    Preconditions.checkArgument(projectRoot.isDirectory());
  }

  public File getProjectRoot() {
    return projectRoot;
  }

  public File getFileForRelativePath(String pathRelativeToProjectRoot) {
    return pathRelativeToProjectRoot.isEmpty()
        ? projectRoot
        : new File(projectRoot, pathRelativeToProjectRoot);
  }

  public boolean exists(String pathRelativeToProjectRoot) {
    return getFileForRelativePath(pathRelativeToProjectRoot).exists();
  }

  public boolean isMatchingFileContents(Iterable<String> lines, String pathRelativeToProjectRoot)
      throws IOException {
    return MoreFiles.isMatchingFileContents(lines,
        getFileForRelativePath(pathRelativeToProjectRoot));
  }

  public Properties readPropertiesFile(String pathToPropertiesFileRelativeToProjectRoot)
      throws IOException {
    Properties properties = new Properties();
    File propertiesFile = getFileForRelativePath(pathToPropertiesFileRelativeToProjectRoot);
    properties.load(Files.newReader(propertiesFile, Charsets.UTF_8));
    return properties;
  }

  /**
   * Checks whether there is a normal file at the specified path.
   */
  public boolean isFile(String pathRelativeToProjectRoot) {
    return new File(pathRelativeToProjectRoot).isFile();
  }
}
